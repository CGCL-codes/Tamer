package com.siberhus.hswing.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import model.Dog;
import ognl.DefaultClassResolver;
import ognl.Ognl;
import ognl.OgnlContext;
import ognl.OgnlException;
import ognl.OgnlRuntime;
import org.apache.commons.lang.builder.ToStringBuilder;

public class OgnlUtils {

    /** Provides a synchronized cache of OGNL expressions. */
    private static final Map OGNL_EXPRESSION_CACHE = new ConcurrentHashMap();

    public static Object getValueOgnl(Object source, String name) throws OgnlException {
        return getValueOgnl(source, name, new HashMap());
    }

    /**
     * Return the property value for the given object and property name using
     * the OGNL library.
     * <p/>
     * This method is thread-safe, and caches parsed OGNL expressions in an
     * internal synchronized cache.
     *
     * @param source the source object
     * @param name the name of the property
     * @param context the OGNL context, do NOT modify this object
     * @return the property value for the given source object and property name
     * @throws OgnlException if an OGN error occurs
     */
    public static Object getValueOgnl(Object source, String name, Map context) throws OgnlException {
        Object expression = OGNL_EXPRESSION_CACHE.get(name);
        if (expression == null) {
            expression = Ognl.parseExpression(name);
            OGNL_EXPRESSION_CACHE.put(name, expression);
        }
        return Ognl.getValue(expression, context, source);
    }

    public static void setValueOgnl(Object target, String name, Object value) throws OgnlException {
        setValueOgnl(target, name, value, new HashMap());
    }

    /**
     * Return the property value for the given object and property name using
     * the OGNL library.
     * <p/>
     * This method is thread-safe, and caches parsed OGNL expressions in an
     * internal synchronized cache.
     *
     * @param target the target object to set the property of
     * @param name the name of the property to set
     * @param value the property value to set
     * @param context the OGNL context, do NOT modify this object
     * @throws OgnlException if an OGN error occurs
     */
    public static void setValueOgnl(Object target, String name, Object value, Map context) throws OgnlException {
        Object expression = OGNL_EXPRESSION_CACHE.get(name);
        if (expression == null) {
            expression = Ognl.parseExpression(name);
            OGNL_EXPRESSION_CACHE.put(name, expression);
        }
        Ognl.setValue(expression, context, target, value);
    }

    /**
     * This method ensures that the object can safely be navigated according
     * to the specified path.
     * <p/>
     * If any object in the graph is null, a new instance of that object class
     * is instantiated.
     *
     * @param object the object which path must be navigatable without
     * encountering null values
     * @param path the navigation path
     */
    public static void ensureObjectPathNotNull(Object object, String path) {
        final int index = path.indexOf('.');
        if (index == -1) {
            return;
        }
        String property = path.substring(0, index);
        final int arrayIndex = property.indexOf('[');
        if (arrayIndex != -1) {
            property = property.substring(0, arrayIndex);
        }
        Method getterMethod = BeanReflectionUtils.findGetter(object, property, path);
        Object result = BeanReflectionUtils.invokeGetter(getterMethod, object, property, path);
        if (result == null) {
            Class targetClass = getterMethod.getReturnType();
            Constructor constructor = null;
            try {
                constructor = targetClass.getConstructor();
            } catch (NoSuchMethodException e) {
                StringBuilder buffer = new StringBuilder();
                buffer.append("Attempt to construct instance of class '");
                buffer.append(targetClass.getName()).append("' resulted in error: '");
                buffer.append(targetClass.getName()).append("' does not seem");
                buffer.append(" to have a default no argument constrcutor.");
                buffer.append(" Please note another common problem is that the");
                buffer.append(" class is either not public or not static.");
                throw new RuntimeException(buffer.toString(), e);
            }
            try {
                result = constructor.newInstance(new Object[] {});
            } catch (Exception e) {
                StringBuilder buffer = new StringBuilder();
                buffer.append("Result: could not create");
                buffer.append(" object with constructor '");
                buffer.append(constructor.getName()).append("'.");
                throw new RuntimeException(buffer.toString(), e);
            }
            Method setterMethod = BeanReflectionUtils.findSetter(object, property, targetClass, path);
            BeanReflectionUtils.invokeSetter(setterMethod, object, result, property, path);
        }
        String remainingPath = path.substring(index + 1);
        ensureObjectPathNotNull(result, remainingPath);
    }

    public static void main(String[] args) throws OgnlException {
        Dog d = new Dog();
        Map context = Ognl.createDefaultContext(d, new DefaultClassResolver(), new CommonsOgnlTypeConverter());
        OgnlUtils.ensureObjectPathNotNull(d, "owner.name");
        System.out.println(context);
        System.out.println(ToStringBuilder.reflectionToString(d));
        System.out.println(OgnlUtils.getValueOgnl(d, "owner.name", context));
    }
}
