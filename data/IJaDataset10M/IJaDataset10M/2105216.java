package org.nicocube.airain.domain.client.exception;

import java.io.Serializable;

public class DomainException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    protected DomainException() {
    }

    public DomainException(String e) {
        super(e);
    }
}
