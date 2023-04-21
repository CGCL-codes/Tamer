package com.abra.j2xb.beans.model;

/**
 * @author Yoav Abrahami
 * @version 1.0, May 1, 2008
 * @since   JDK1.5
 */
public class MOValueValidationResult {

    private boolean isValid;

    private String message;

    private Class validationAgainstClass;

    public MOValueValidationResult(boolean isValid, Class validationAgainstClass, String message) {
        this.isValid = isValid;
        this.message = message;
        this.validationAgainstClass = validationAgainstClass;
    }

    public MOValueValidationResult() {
        this.isValid = true;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {
        return message;
    }

    public Class getType() {
        return validationAgainstClass;
    }
}
