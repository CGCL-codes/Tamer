package cryptix.sasl.sm2;

import javax.security.sasl.SaslException;

/**
 * A checked exception thrown to indicate that a designated SM2 session
 * is, or has become, invalid.
 *
 * @version $Revision: 1.2 $
 * @since draft-naffah-cat-sasl-sm2-00
 */
public class SM2InvalidSessionException extends SaslException {

    /**
	 * Constructs a <tt>SM2InvalidSessionException</tt> with the specified
	 * detail message. In the case of this exception, the detail message
	 * designates the session identifier.
	 *
	 * @param sid the session identifier.
	 */
    public SM2InvalidSessionException(String sid) {
        super(sid);
    }
}
