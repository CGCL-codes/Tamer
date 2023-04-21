package cn.liangent.travlib;

import java.net.*;

public class HttpException extends ProtocolException {

    public HttpException() {
        super();
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }

    public HttpException(Throwable cause) {
        initCause(cause);
    }
}
