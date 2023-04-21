package com.tinywebgears.tuatara.framework.resource;

public class MessageFormatException extends RuntimeException {

    public MessageFormatException(String msg) {
        super(msg);
    }

    public MessageFormatException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
