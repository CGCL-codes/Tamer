package edu.caltech.sbw;

/**
 * Thrown if a signature string does not contain a valid SBW signature.  In
 * the Java API, this is only thrown by method {@link Service#getMethod}.
 * <p>
 * All exceptions used in the SBW API are derived from {@link SBWException},
 * which is in turn derived from the Java {@link java.lang.Exception} class.
 * {@link SBWException} inherits the {@link java.lang.Exception#getMessage}
 * method for obtaining the message associated with an exception, and it
 * also provides other methods such as {@link SBWException#getDetailedMessage}
 * and {@link SBWException#handleWithDialog}.
 * <p>
 * All exceptions have a numerical code associated with them; this code is
 * used to match up exceptions in other SBW language bindings (in particular,
 * for the C language).  The method {@link #getCode} returns the integer code
 * assigned to this particular exception class.
 * <p>
 * @see SBWException
 *
 * @author Michael Hucka
 * @author $Author: fbergmann $
 * @version $Revision: 1.2 $
 **/
public class SBWSignatureSyntaxException extends SBWException {

    /**
	 * Construct an exception with a diagnostic message and an optional
	 * detailed diagnostic message.
	 *
	 * @param m a brief message
	 * @param dm a detailed message
	 **/
    SBWSignatureSyntaxException(String m, String dm) {
        super(m, dm);
    }

    /**
	 * Returns the unique numeric code that identifies this exception
	 * in the SBW framework.
	 *
	 * @return a byte giving the code
	 **/
    public byte getCode() {
        return CODE;
    }

    /**
	 * A unique numeric code that identifies this exception.
	 **/
    static final byte CODE = 14;

    static {
        Config.recordClassVersion(SBWSignatureSyntaxException.class, "$Id: SBWSignatureSyntaxException.java,v 1.2 2007/07/24 23:08:23 fbergmann Exp $");
    }
}
