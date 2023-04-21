package org.pojosoft.ria.gwt.client.service;

import com.google.gwt.user.client.rpc.SerializableException;

/**
 * Thrown by a remote service method when a runtime exception occurred during the invocation of the method
 */
public class ServiceInvocationException extends SerializableException {

    private String msg;

    public ServiceInvocationException() {
        super();
    }

    public ServiceInvocationException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public String getMessage() {
        return this.msg;
    }
}
