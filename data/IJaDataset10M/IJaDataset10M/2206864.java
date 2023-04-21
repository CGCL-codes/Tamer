package org.architecture.common.transaction;

import org.apache.commons.lang.exception.NestableRuntimeException;

public class TransactionException extends NestableRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -1164988026542809073L;

    public TransactionException(String msg) {
        super(msg);
    }

    public TransactionException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
