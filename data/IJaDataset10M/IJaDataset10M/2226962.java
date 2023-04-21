package com.google.code.jahath.common.http;

import java.io.IOException;

public class HttpConnectionException extends HttpException {

    private static final long serialVersionUID = 569661394138562942L;

    public HttpConnectionException(IOException cause) {
        super(cause);
    }
}
