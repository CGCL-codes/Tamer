package org.nexopenframework.management.spring.support;

import java.lang.reflect.Method;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Utility class for determine Spring version</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class Spring25xSupport {

    private static boolean is25xVersion = ClassUtils.isPresent("org.springframework.stereotype.Service", ClassUtils.getDefaultClassLoader());

    private static boolean is256Version = ClassUtils.isPresent("org.springframework.jmx.access.MBeanConnectFailureException", ClassUtils.getDefaultClassLoader());

    /**
	 * <p>Avoid creation of an instance of this support class</p>
	 */
    private Spring25xSupport() {
        super();
    }

    /**
	 * <p>if Spring version is <code>2.5.0</code> or higher. Otherwise, it will return <code>false</code></p>
	 * 
	 * @return If it is <code>2.5.0</code> returns <code>true</code>. If it is lower than 2.5.0 (2.0.x or lower) 
	 * 		   is returned <code>false</code>
	 */
    public static boolean is250AtLeast() {
        return is25xVersion;
    }

    public static boolean is256AtLeast() {
        return is256Version;
    }

    /**
	 * Determine whether the given method is an "equals" method.
	 * @see java.lang.Object#equals
	 */
    public static boolean isEqualsMethod(final Method method) {
        if (is256AtLeast()) {
            return ReflectionUtils.isEqualsMethod(method);
        }
        if (method == null || !method.getName().equals("equals")) {
            return false;
        }
        final Class<?>[] paramTypes = method.getParameterTypes();
        return (paramTypes.length == 1 && paramTypes[0] == Object.class);
    }

    /**
	 * Determine whether the given method is a "hashCode" method.
	 * @see java.lang.Object#hashCode
	 */
    public static boolean isHashCodeMethod(final Method method) {
        if (is256AtLeast()) {
            return ReflectionUtils.isHashCodeMethod(method);
        }
        return (method != null && method.getName().equals("hashCode") && method.getParameterTypes().length == 0);
    }

    /**
	 * Determine whether the given method is a "toString" method.
	 * @see java.lang.Object#toString()
	 */
    public static boolean isToStringMethod(final Method method) {
        if (is256AtLeast()) {
            return ReflectionUtils.isToStringMethod(method);
        }
        return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
    }
}
