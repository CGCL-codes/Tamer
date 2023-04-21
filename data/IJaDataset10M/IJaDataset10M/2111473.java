package exfex.common.security;

/** Exception for bad bean format.
 * 
 * This exception is thrown if authentication bean doesn't contain all necessary
 * data for verifying. 
 * 
 * <p>
 * <pre>
 * Changes:
 * 25.11.2005	msts -	created
 * </pre>
 *
 * @author msts
 */
public class BeanFormatException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    /** Constructor with message.
	 * 
	 * @param msg Message of the exception.
	 */
    public BeanFormatException(String msg) {
        super(msg);
    }

    /** Constructor with message and cause.
	 * 
	 * @param msg Message of the exception.
	 * @param cause Lowlevel exception which caused this exception.
	 */
    public BeanFormatException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
