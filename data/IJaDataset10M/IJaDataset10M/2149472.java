package com.minderupt.micrtools.validator;

public class FieldValidationException extends Exception {

    private String fieldName;

    public FieldValidationException() {
        super();
    }

    public FieldValidationException(String message) {
        super(message);
    }

    public FieldValidationException(Throwable cause) {
        super(cause);
    }

    public FieldValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public void setFieldName(String val) {
        fieldName = val;
    }

    public String getFieldName() {
        return (fieldName);
    }
}
