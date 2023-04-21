package de.hauschild.dbc4j.util;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;

/**
 * Utility class for {@link JoinPoint}.
 * 
 * @since 1.0.0
 * @author Klaus Hauschild
 */
public final class JoinPointUtils {

    /**
   * Returns the {@link Method} matched by the {@link JoinPoint}.
   * 
   * @param joinPoint
   *          the join point
   * @return the method
   */
    public static Method getMethod(final JoinPoint joinPoint) {
        return ReflectionUtils.getMethod(joinPoint.getSignature().getDeclaringType(), joinPoint.getSignature().getName(), getTypesForArgs(joinPoint));
    }

    private static Class<?> determineByName(final JoinPoint joinPoint, final int i) {
        final String signature = joinPoint.toLongString();
        final String parameterString = signature.split("\\(")[2].split("\\)\\)")[0];
        final String[] parameters = parameterString.split(",");
        try {
            return Class.forName(parameters[i].trim());
        } catch (final ClassNotFoundException e) {
            return null;
        }
    }

    private static Class<?>[] getTypesForArgs(final JoinPoint joinPoint) {
        if (joinPoint.getArgs() == null) {
            return new Class<?>[0];
        }
        final Class<?>[] types = new Class<?>[joinPoint.getArgs().length];
        for (int i = 0; i < types.length; i++) {
            if (joinPoint.getArgs()[i] != null) {
                types[i] = joinPoint.getArgs()[i].getClass();
            } else {
                types[i] = determineByName(joinPoint, i);
            }
        }
        return types;
    }

    /**
   * Instantiates a new {@link JoinPointUtils}.
   */
    private JoinPointUtils() {
    }
}
