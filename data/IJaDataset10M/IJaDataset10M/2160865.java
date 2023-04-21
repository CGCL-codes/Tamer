package com.apusic.ebiz.framework.core.exception;

public class BaseBussinessException extends Exception {

    private String code;

    private String description;

    public BaseBussinessException(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
