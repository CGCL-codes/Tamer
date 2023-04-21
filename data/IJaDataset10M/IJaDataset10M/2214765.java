package net.sourceforge.chimeralibrary.core.util;

import java.lang.reflect.Method;
import net.sourceforge.chimeralibrary.core.reflect.ReflectionUtility;

/**
 * Utility class for performing common object invocation tasks.
 * 
 * @author Christian Cruz
 * @version 1.0.000
 * @since 1.0.000
 */
public class ObjectUtility {

    /**
	 * Copies common properties from the source object to the destination object even if the two objects are not directly related to each other.
	 * 
	 * @param source the source object that contains the property values
	 * @param destination the destination object
	 * @param baseClass specifies the depth of methods that will be copied to the other side
	 */
    public static void copyCommonProperties(final Object source, final Object destination, final Class<?> baseClass) {
        try {
            for (final Method sourceMethod : source.getClass().getMethods()) {
                if ((sourceMethod.getParameterTypes().length == 0) && baseClass.isAssignableFrom(sourceMethod.getDeclaringClass())) {
                    final String propertyName = ReflectionUtility.findPropertyNameForMethod(sourceMethod);
                    if (propertyName != null) {
                        final Method destinationMethod = ReflectionUtility.getSetterMethod(destination, propertyName);
                        if ((destinationMethod != null) && (destinationMethod.getParameterTypes()[0] == sourceMethod.getReturnType())) {
                            final Object value = sourceMethod.invoke(source, new Object[] {});
                            if (value != null) {
                                destinationMethod.invoke(destination, new Object[] { value });
                            }
                        }
                    }
                }
            }
        } catch (final Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private ObjectUtility() {
    }
}
