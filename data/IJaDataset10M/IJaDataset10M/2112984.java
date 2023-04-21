package javax.management.openmbean;

/**
 * This runtime exception is thrown to indicate that the <i>open type</i> of an <i>open data</i> value
 * is not the one expected.
 *
 * @version     3.19  05/11/17
 * @author      Sun Microsystems, Inc.
 *
 * @since 1.5
 * @since.unbundled JMX 1.1
 */
public class InvalidOpenTypeException extends IllegalArgumentException {

    private static final long serialVersionUID = -2837312755412327534L;

    /** An InvalidOpenTypeException with no detail message.  */
    public InvalidOpenTypeException() {
        super();
    }

    /**
     * An InvalidOpenTypeException with a detail message.
     *
     * @param msg the detail message.
     */
    public InvalidOpenTypeException(String msg) {
        super(msg);
    }
}
