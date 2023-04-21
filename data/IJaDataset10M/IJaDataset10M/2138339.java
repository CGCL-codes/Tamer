package com.hk.svr.company.exception;

import com.hk.svr.exception.HkException;

public class NoEnoughMoneyException extends HkException {

    private static final long serialVersionUID = 3872540979616791696L;

    public NoEnoughMoneyException(String message) {
        super(message);
    }
}
