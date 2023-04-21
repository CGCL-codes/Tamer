package org.piuframework.service.handler.jms;

import java.io.Serializable;

/**
 * TODO
 *
 * @author Dirk Mascher
 */
public class ResponseObject implements Serializable {

    private static final long serialVersionUID = 5034583079050452292L;

    private Throwable t;

    private Serializable returnValue;

    public ResponseObject(Throwable t) {
        this.t = t;
    }

    public ResponseObject(Serializable returnValue) {
        this.returnValue = returnValue;
    }

    public Serializable getReturnValue() {
        return returnValue;
    }

    public Throwable getException() {
        return t;
    }
}
