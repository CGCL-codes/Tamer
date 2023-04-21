package net.sf.beanlib.provider.finder;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import net.sf.beanlib.spi.BeanMethodFinder;

/**
 * Supports finding JavaBean reader method which is either public or protected.
 *   
 * @author Joe D. Velopar
 */
public class ProtectedReaderMethodFinder implements BeanMethodFinder {

    public Method find(final String propertyName, Object bean) {
        String s = propertyName;
        if (Character.isLowerCase(propertyName.charAt(0))) {
            s = propertyName.substring(0, 1).toUpperCase();
            if (propertyName.length() > 1) s += propertyName.substring(1);
        }
        Class<?> beanClass = bean.getClass();
        final int maxLengthOfGetterMethodName = s.length() + 3;
        while (beanClass != Object.class) {
            for (Method m : beanClass.getDeclaredMethods()) {
                final int mod = m.getModifiers();
                if (qualified(m, mod)) {
                    final String methodName = m.getName();
                    if (methodName.length() == maxLengthOfGetterMethodName) {
                        if (methodName.endsWith(s) && methodName.startsWith("get")) return m;
                    } else if (methodName.length() == maxLengthOfGetterMethodName - 1) {
                        if (methodName.endsWith(s) && methodName.startsWith("is")) return m;
                    }
                }
            }
            beanClass = beanClass.getSuperclass();
        }
        return null;
    }

    protected boolean qualified(Method m, int mod) {
        return (Modifier.isPublic(mod) || Modifier.isProtected(mod)) && !Modifier.isStatic(mod) && m.getParameterTypes().length == 0;
    }
}
