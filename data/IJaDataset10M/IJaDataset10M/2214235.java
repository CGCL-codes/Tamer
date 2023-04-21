package org.lwjgl;

/**
 * Exception thrown by library loader if operating system is not supported.
 *
 * @author 	Jens von Pilgrim (developer@jevopi.de)
 * @since 	Jan 30, 2011
 */
public class OSNotSupportedException extends Exception {

    String strOSName;

    String strOSArch;

    /**
	 * @param i_strOSName
	 * @param i_strOSArch
	 */
    public OSNotSupportedException(String i_strOSName, String i_strOSArch) {
        super();
        strOSName = i_strOSName;
        strOSArch = i_strOSArch;
    }

    /** 
	 * {@inheritDoc}
	 * @see java.lang.Throwable#getMessage()
	 */
    @Override
    public String getMessage() {
        return strOSName + " (" + strOSArch + ") not supported by LWJGL.";
    }

    /** 
	 * {@inheritDoc}
	 * @see java.lang.Throwable#toString()
	 */
    @Override
    public String toString() {
        return getMessage();
    }
}
