package net.sourceforge.smokestack.ejb;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import net.sourceforge.smokestack.ejb.internal.ClassFinder;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class MockEJBContainer {

    protected Map<Class<?>, Object> beans = new HashMap<Class<?>, Object>();

    private ClassFinder classFinder;

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    /**
	 * Setup and return an EJB instance
	 * @param <T>
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
    @SuppressWarnings("unchecked")
    public <T> T getInstance(Class<T> clazz) throws MockEJBContainerException {
        try {
            if (beans.containsKey(clazz)) {
                return (T) beans.get(clazz);
            }
            T instance = clazz.newInstance();
            if (clazz.isAnnotationPresent(Stateless.class) || clazz.isAnnotationPresent(Stateful.class) || clazz.isAnnotationPresent(MessageDriven.class)) {
                beans.put(clazz, instance);
            }
            injectMembers(clazz, instance);
            if (clazz.isAnnotationPresent(Stateless.class) || clazz.isAnnotationPresent(Stateful.class) || clazz.isAnnotationPresent(MessageDriven.class)) {
                callMethodAnnotated(PostConstruct.class, clazz, instance);
            }
            return instance;
        } catch (InstantiationException e) {
            throw new MockEJBContainerException("for " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            throw new MockEJBContainerException("for " + clazz.getName(), e);
        } catch (IllegalArgumentException e) {
            throw new MockEJBContainerException("for " + clazz.getName(), e);
        } catch (ClassNotFoundException e) {
            throw new MockEJBContainerException("for " + clazz.getName(), e);
        }
    }

    /**
	 * Inject values for member variables
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
    protected <T> void injectMembers(Class<T> clazz, T instance) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Field[] allFields = getAllFields(clazz);
        for (Field f : allFields) {
            f.setAccessible(true);
            if (f.isAnnotationPresent(EJB.class)) {
                Class<?> ejbClass = f.getType();
                if (ejbClass.isInterface() || Modifier.isAbstract(ejbClass.getModifiers())) {
                    ClassFinder finder = getClassFinder();
                    Vector<Class<?>> impls = finder.findSubclasses(ejbClass);
                    assertThat("expected single implementation", impls.size(), is(1));
                    f.set(instance, this.getInstance(impls.get(0)));
                } else {
                    f.set(instance, this.getInstance(ejbClass));
                }
            } else if (f.isAnnotationPresent(PersistenceContext.class)) {
                String unitName = f.getName();
                PersistenceContext pc = f.getAnnotation(PersistenceContext.class);
                String pun = pc.unitName();
                if (StringUtils.isNotEmpty(pun)) {
                    unitName = pun;
                }
                EntityManagerFactory factory = Persistence.createEntityManagerFactory(unitName, System.getProperties());
                f.set(instance, factory.createEntityManager());
            } else if (f.isAnnotationPresent(PersistenceUnit.class)) {
                String unitName = f.getName();
                PersistenceUnit pc = f.getAnnotation(PersistenceUnit.class);
                String pun = pc.unitName();
                if (StringUtils.isNotEmpty(pun)) {
                    unitName = pun;
                }
                f.set(instance, Persistence.createEntityManagerFactory(unitName, System.getProperties()));
            }
        }
    }

    private ClassFinder getClassFinder() {
        if (classFinder == null) {
            classFinder = new ClassFinder();
        }
        return classFinder;
    }

    /**
	 * Recursively get all fields
	 */
    protected <T> Field[] getAllFields(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            fields = (Field[]) ArrayUtils.addAll(fields, getAllFields(superClass));
        }
        return fields;
    }

    public void cleanInstances() {
        for (Class clazz : beans.keySet()) {
            Object instance = beans.get(clazz);
            callMethodAnnotated(PreDestroy.class, clazz, instance);
        }
        beans.clear();
    }

    private void callMethodAnnotated(Class<? extends Annotation> annotatedWith, Class clazz, Object instance) {
        for (Method m : getAllMethods(clazz)) {
            if (m.isAnnotationPresent(annotatedWith)) {
                try {
                    m.setAccessible(true);
                    m.invoke(instance, new Object[] {});
                } catch (Exception e) {
                    throw new MockEJBContainerException("call to @" + annotatedWith.toString() + " failed", e);
                }
            }
        }
    }

    /**
	 * Recursively get all fields
	 */
    protected <T> Method[] getAllMethods(Class<T> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null) {
            methods = (Method[]) ArrayUtils.addAll(methods, getAllMethods(superClass));
        }
        return methods;
    }
}
