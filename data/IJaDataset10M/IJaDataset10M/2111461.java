package com.ivis.xprocess.framework.exceptions;

/**
 * Used to indicate a problem with a datasource's minimum compatibility version
 * number when it does not match the minimum compatibility version of Xprocess
 *
 */
public class MinimumVersionException extends FrameworkException {

    private static final long serialVersionUID = -717675978225038188L;

    public MinimumVersionException() {
        super();
    }

    public MinimumVersionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public MinimumVersionException(String arg0) {
        super(arg0);
    }

    public MinimumVersionException(Throwable arg0) {
        super(arg0);
    }
}
