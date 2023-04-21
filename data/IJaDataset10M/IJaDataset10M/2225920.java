package com.sleepsocial.api.error;

public class ApiError {

    private final String message;

    public ApiError(String message) {
        super();
        this.message = message;
    }

    public ApiError(Exception e) {
        this.message = e.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
