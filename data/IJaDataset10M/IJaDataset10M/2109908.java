package nl.beesting.beangenerator.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import nl.beesting.beangenerator.BeanFactory;
import nl.beesting.beangenerator.config.ClassDescriptor;
import nl.beesting.beangenerator.config.FieldDescriptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.jxpath.JXPathContext;

/**
 * @author Erik Jan (latest modification by $Author: $)
 * @version $Revision: $ $Date: $
 */
public class DaoBeanCreationMethodInterceptor implements MethodInterceptor {

    private Cache cacheManager;

    private BeanFactory factory;

    public void setBeanFactory(BeanFactory factory) {
        this.factory = factory;
    }

    public void setCacheManager(Cache cacheManager) {
        this.cacheManager = cacheManager;
    }

    @SuppressWarnings("unchecked")
    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (invocation.getMethod().getName().startsWith("set")) {
            return invocation.proceed();
        }
        Object result = null;
        if (cacheManager != null) {
            Class<?> foundClass = factory.findClass(invocation);
            Element cacheElement = cacheManager.get(foundClass);
            if (cacheElement != null && cacheElement.getValue() != null) {
                Method method = invocation.getMethod();
                ClassDescriptor classDescriptor = factory.getClassDescriptor(invocation.getClass(), method);
                if (classDescriptor != null) {
                    Map<String, FieldDescriptor> fieldDefinitions = classDescriptor.getFieldDefinitions();
                    FieldDescriptor fieldDescriptor = (FieldDescriptor) fieldDefinitions.get(method.getName());
                    JXPathContext context = JXPathContext.newContext((Collection<?>) cacheElement.getValue());
                    for (int i = 0; i < invocation.getArguments().length; i++) {
                        Object argument = invocation.getArguments()[i];
                        context.getVariables().declareVariable("att" + (i + 1), argument);
                    }
                    result = context.getValue(fieldDescriptor.getQuery());
                    if (method.getReturnType().isAssignableFrom(Collection.class) && !result.getClass().isAssignableFrom(Collection.class)) {
                        Set<Object> set = new HashSet<Object>();
                        set.add(result);
                        result = set;
                    }
                } else {
                    if (method.getReturnType().isAssignableFrom(Collection.class)) {
                        result = cacheElement.getValue();
                    } else {
                        result = randomGetElement((Collection<Object>) cacheElement.getValue());
                    }
                }
            } else {
                Collection generatedCollection = instanciateBean(invocation);
                cacheElement = new Element(foundClass, (Serializable) generatedCollection);
                cacheManager.put(cacheElement);
                if (invocation.getMethod().getReturnType().isAssignableFrom(Collection.class)) {
                    result = generatedCollection;
                } else {
                    result = randomGetElement(generatedCollection);
                }
            }
        }
        return result;
    }

    private Object randomGetElement(Collection<Object> cachedCollection) {
        Object result = null;
        int index = RandomUtil.initRandomIntBetween(1, cachedCollection.size() - 1);
        Iterator<Object> iterator = cachedCollection.iterator();
        for (int i = 0; i <= index; i++) {
            result = iterator.next();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private Collection<Object> instanciateBean(MethodInvocation invocation) throws BeanInstantiationException {
        Class<?> foundClass = factory.findClass(invocation);
        Collection<Object> result = null;
        Method method = invocation.getMethod();
        if (foundClass != null) {
            ClassDescriptor classDescriptor = factory.getClassDescriptor(foundClass, method);
            int count = 10;
            if (classDescriptor != null) {
                count = classDescriptor.getCollectionSize();
                try {
                    result = (Collection<Object>) classDescriptor.getCollectionType().newInstance();
                } catch (Exception e) {
                    throw new BeanInstantiationException("Could not create object (type: '" + classDescriptor.getCollectionType() + "') using default constructor make sure it's a POJO", e);
                }
            } else {
                result = new HashSet();
            }
            for (int i = 0; i < count; i++) {
                ((Collection) result).add(factory.instanciateBean(foundClass, method));
            }
        }
        return result;
    }
}
