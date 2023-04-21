package com.freebase.api;

import com.freebase.json.JSON;

public class FreebaseException extends RuntimeException {

    private static final long serialVersionUID = 7521456388426850609L;

    private JSON result;

    public FreebaseException(String message, JSON result) {
        super(message);
        this.result = result;
    }

    public FreebaseException(String msg, Exception e) {
        super(msg);
    }

    public FreebaseException(String msg) {
        super(msg);
    }

    public FreebaseException(Exception e) {
        super(e);
    }

    public JSON getResult() {
        return result;
    }
}
