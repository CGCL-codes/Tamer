package com.hk.svr.box.exception;

import com.hk.svr.exception.HkException;

public class BeginTimeException extends HkException {

    private static final long serialVersionUID = 6904588270399256409L;

    public BeginTimeException(String message) {
        super(message);
    }
}
