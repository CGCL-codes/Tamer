package com.sshtools.common.ui;

import java.lang.reflect.*;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.13 $
 */
public class SshToolsApplicationException extends Exception {

    /**
* Creates a new SshToolsApplicationException object.
*/
    public SshToolsApplicationException() {
        this(null, null);
    }

    /**
* Creates a new SshToolsApplicationException object.
*
* @param msg
*/
    public SshToolsApplicationException(String msg) {
        this(msg, null);
    }

    /**
* Creates a new SshToolsApplicationException object.
*
* @param cause
*/
    public SshToolsApplicationException(Throwable cause) {
        this(null, cause);
    }

    /**
* Creates a new SshToolsApplicationException object.
*
* @param msg
* @param cause
*/
    public SshToolsApplicationException(String msg, Throwable cause) {
        super(msg);
        if (cause != null) {
            try {
                Method m = getClass().getMethod("initCause", new Class[] { Throwable.class });
                m.invoke(this, new Object[] { cause });
            } catch (Exception e) {
            }
        }
    }
}
