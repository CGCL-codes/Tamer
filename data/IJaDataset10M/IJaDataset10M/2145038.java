package net.sf.irunninglog.util;

/**
 * Exception thrown when a value cannot be converted from one type to another.
 *
 * @author <a href="mailto:allan_e_lewis@yahoo.com">Allan Lewis</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2005/06/23 01:49:03 $
 * @since iRunningLog 1.0
 */
public class ConversionException extends Exception {

    /**
     * Create a new exception indicating a failed conversion.
     *
     * @param value The value that failed conversion
     * @param fromClass The class of the value
     * @param toClass The class that the value could not be converted to
     */
    public ConversionException(Object value, Class fromClass, Class toClass) {
        super(buildMessage(value, fromClass, toClass));
    }

    /**
     * Build up the detail message for the exception.
     *
     * @param value The value that failed conversion
     * @param fromClass The class of the value
     * @param toClass The class that the value could not be converted to
     * @return The detail message to use in creating the exception
     */
    private static String buildMessage(Object value, Class fromClass, Class toClass) {
        StringBuffer buf = new StringBuffer();
        buf.append("Error performing conversion: unable to convert '");
        buf.append(value);
        buf.append("' from ");
        buf.append(fromClass.getName());
        buf.append(" to ");
        buf.append(toClass.getName());
        return buf.toString();
    }
}
