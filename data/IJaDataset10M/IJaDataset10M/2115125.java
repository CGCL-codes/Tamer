package edu.caltech.sbw;

/**
 * Exception thrown if a module can't be found during an operation that requires
 * looking up a specific module.
 * <p>
 * All exceptions used in the SBW API are derived from {@link SBWException},
 * which is in turn derived from the Java {@link java.lang.Exception} class.
 * {@link SBWException} inherits the {@link java.lang.Exception#getMessage}
 * method for obtaining the message associated with an exception, and it also
 * provides other methods such as {@link SBWException#getDetailedMessage} and
 * {@link SBWException#handleWithDialog}.
 * <p>
 * All exceptions have a numerical code associated with them; this code is used
 * to match up exceptions in other SBW language bindings (in particular, for the
 * C language). The method {@link #getCode} returns the integer code assigned to
 * this particular exception class.
 * <p>
 * 
 * @see SBWException
 * 
 * @author Michael Hucka
 * @author $Author: fbergmann $
 * @version $Revision: 1.2 $
 */
public class SBWModuleNotFoundException extends SBWException {

    /**
	 * Construct an exception with a diagnostic message and an optional detailed
	 * diagnostic message.
	 * 
	 * @param m
	 *            a brief message
	 * @param dm
	 *            a detailed message
	 */
    SBWModuleNotFoundException(String m, String dm) {
        super(m, dm);
    }

    /**
	 * A unique numeric code that identifies this exception.
	 */
    static final byte CODE = 16;

    /**
	 * Returns the unique numeric code that identifies this exception in the SBW
	 * framework.
	 */
    public byte getCode() {
        return CODE;
    }

    static {
        Config.recordClassVersion(SBWModuleNotFoundException.class, "$Id: SBWModuleNotFoundException.java,v 1.2 2007/07/24 23:08:20 fbergmann Exp $");
    }
}
