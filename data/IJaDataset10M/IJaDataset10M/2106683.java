package net.sf.derquinsej;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;

/**
 * Utility methods for Methods.
 * @author Andres Rodriguez
 */
public final class Methods {

    /**
	 * Not instantiable.
	 */
    private Methods() {
        throw new AssertionError();
    }

    /**
	 * Returns the methods of a class as an iterable.
	 * @param klass Class to process.
	 * @return The methods of the class.
	 * @throws NullPointerException if the argument is null.
	 */
    public static List<Method> getMethods(Class<?> klass) {
        Preconditions.checkNotNull(klass, "A class must be provided");
        return Arrays.asList(klass.getMethods());
    }

    public static Predicate<Method> withParameters(final int numParameters) {
        final Predicate<Method> predicate = new Predicate<Method>() {

            public boolean apply(Method input) {
                return input.getParameterTypes().length == numParameters;
            }
        };
        return predicate;
    }

    public static Predicate<Method> withParameterOfType(final int index, final Class<?> type) {
        final Predicate<Method> predicate = new Predicate<Method>() {

            public boolean apply(Method input) {
                return input.getParameterTypes()[index].equals(type);
            }
        };
        return predicate;
    }

    public static Predicate<Method> withParameterTypeAssignableTo(final int index, final Class<?> type) {
        final Predicate<Method> predicate = new Predicate<Method>() {

            public boolean apply(Method input) {
                return type.isAssignableFrom(input.getParameterTypes()[index]);
            }
        };
        return predicate;
    }

    public static Predicate<Method> withReturnTypeAssignableTo(final int index, final Class<?> type) {
        final Predicate<Method> predicate = new Predicate<Method>() {

            public boolean apply(Method input) {
                return type.isAssignableFrom(input.getReturnType());
            }
        };
        return predicate;
    }

    public static Predicate<Method> annotated(final Class<? extends Annotation> annotation) {
        final Predicate<Method> predicate = new Predicate<Method>() {

            public boolean apply(Method input) {
                return input.isAnnotationPresent(annotation);
            }
        };
        return predicate;
    }

    private static final Function<Method, Class<?>> RETURN_TYPE = new Function<Method, Class<?>>() {

        public Class<?> apply(Method input) {
            return input.getReturnType();
        }

        ;
    };

    public static Function<Method, Class<?>> returnType() {
        return RETURN_TYPE;
    }

    public static Function<Method, Class<?>> parameterType(final int parameter) {
        return new Function<Method, Class<?>>() {

            public Class<?> apply(Method input) {
                return input.getParameterTypes()[parameter];
            }

            ;
        };
    }
}
