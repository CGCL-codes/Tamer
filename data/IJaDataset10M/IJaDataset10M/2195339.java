package com.once.mail;

import com.once.BaseException;

public class MailException extends BaseException {

    static final long serialVersionUID = 1;

    public MailException() {
        super();
    }

    public MailException(String message) {
        super(message);
    }

    public MailException(Throwable cause) {
        super(cause);
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }
}
