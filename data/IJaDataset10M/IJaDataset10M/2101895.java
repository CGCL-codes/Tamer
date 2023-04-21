package com.hk.svr.invite.exception;

import com.hk.svr.exception.HkException;

public class OutOfInviteLimitException extends HkException {

    private static final long serialVersionUID = 6056790981824301321L;

    public OutOfInviteLimitException(String message) {
        super(message);
    }
}
