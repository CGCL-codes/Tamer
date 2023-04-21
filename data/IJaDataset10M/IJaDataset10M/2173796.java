package org.restlet.ext.jaxrs.internal.exceptions;

/**
 * Thrown if a value for an instance field or a parameter for a constructor, a
 * resource method or a bean setter could not be instantiated.
 * 
 * @author Stephan Koops
 */
public class ConvertParameterException extends JaxRsException {

    private static final long serialVersionUID = 951579935427584482L;

    /**
     * Throws a message, that the given String value could not be converted to a
     * primitive.
     * 
     * @param paramType
     * @param unparseableValue
     * @param cause
     * @return (static the created ConvertParameterException for the
     *         compiler)
     * @throws ConvertParameterException
     */
    public static ConvertParameterException object(Class<?> paramType, Object unparseableValue, Throwable cause) throws ConvertParameterException {
        throw new ConvertParameterException("Could not convert " + unparseableValue + " to a " + paramType.getName(), cause);
    }

    /**
     * Throws a message, that the given String value could not be converted to a
     * primitive.
     * 
     * @param paramType
     * @param unparseableValue
     * @param cause
     * @return (static the created ConvertParameterException for the
     *         compiler)
     * @throws ConvertParameterException
     */
    public static ConvertParameterException primitive(Class<?> paramType, String unparseableValue, Throwable cause) throws ConvertParameterException {
        throw new ConvertParameterException("Could not convert the String \"" + unparseableValue + "\" to a " + paramType, cause);
    }

    /**
     * @param message
     */
    private ConvertParameterException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    private ConvertParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    private ConvertParameterException(Throwable cause) {
        super(cause);
    }
}
