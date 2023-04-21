package org.limaloa;

/**
 * Encapsulates a method parameter.
 * 
 * Designed as an immutable value object.
 * 
 * @author Chris Nappin
 */
public final class Parameter {

    /** The class of the parameter. */
    private Class<?> parameterClass;

    /**
	 * Creates a new parameter.
	 * @param parameterClass The class of the parameter
	 */
    public Parameter(Class<?> parameterClass) {
        this.parameterClass = parameterClass;
    }

    /**
	 * Creates a new parameter.
	 * @param type	The class or primitive type of the parameter
	 * @throws ClassNotFoundException  If class not found
	 */
    public Parameter(String type) throws ClassNotFoundException {
        this(convertToClass(type));
    }

    /**
	 * Get the parameter's class.
	 * @return The Class
	 */
    public Class<?> getParameterClass() {
        return parameterClass;
    }

    /**
	 * Converts a classname or primitive type name to Class instance.
	 * @param type	The type name
	 * @return The Class instance
	 * @throws ClassNotFoundException If classname not found
	 */
    private static Class<?> convertToClass(String type) throws ClassNotFoundException {
        if ("boolean".equals(type)) {
            return Boolean.TYPE;
        } else if ("byte".equals(type)) {
            return Byte.TYPE;
        } else if ("short".equals(type)) {
            return Short.TYPE;
        } else if ("int".equals(type)) {
            return Integer.TYPE;
        } else if ("long".equals(type)) {
            return Long.TYPE;
        } else if ("float".equals(type)) {
            return Float.TYPE;
        } else if ("double".equals(type)) {
            return Double.TYPE;
        } else if ("char".equals(type)) {
            return Character.TYPE;
        } else {
            return Class.forName(type);
        }
    }

    /**
	 * Value equality.
	 * @param object	The object to compare
	 * @return <code>true</code> if of equal value
	 */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Parameter)) {
            return false;
        }
        Parameter other = (Parameter) object;
        return this.getParameterClass() == other.getParameterClass();
    }

    /**
	 * Hash code, consistent with value equality.
	 * @return The hash code
	 */
    @Override
    public int hashCode() {
        return 37 * (17 + this.getParameterClass().hashCode());
    }

    /**
	 * Converts an instance to a string.
	 * @return The string representation
	 */
    @Override
    public String toString() {
        return new StringBuilder(this.getClass().getName()).append("[class: ").append(getParameterClass()).append("]").toString();
    }
}
