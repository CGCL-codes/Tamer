package it.simplerecords.exceptions;

import java.lang.annotation.Annotation;

public class RecordValidationException extends Exception {

    private static final long serialVersionUID = -482732651587304129L;

    private String fieldName;

    private Object fieldValue;

    private Annotation annotation;

    public RecordValidationException() {
    }

    public RecordValidationException(String message) {
        super(message);
    }

    public RecordValidationException(Throwable cause) {
        super(cause);
    }

    public RecordValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }
}
