package com.xpresso.utils.maps;

import java.io.UnsupportedEncodingException;

public class MapException extends Exception {

    protected String description;

    protected Exception exception;

    public MapException(String description, Exception e) {
        this.description = description;
        this.exception = e;
    }

    public void setException(UnsupportedEncodingException e) {
        this.exception = e;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
