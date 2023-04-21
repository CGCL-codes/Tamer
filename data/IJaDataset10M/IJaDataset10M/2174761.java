package com.liferay.util;

/**
 * <a href="EncryptorException.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.6 $
 *
 */
public class EncryptorException extends Exception {

    public EncryptorException() {
        super();
    }

    public EncryptorException(String msg) {
        super(msg);
    }

    public EncryptorException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public EncryptorException(Throwable cause) {
        super(cause);
    }
}
