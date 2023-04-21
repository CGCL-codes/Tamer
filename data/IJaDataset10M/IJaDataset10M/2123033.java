package com.angel.architecture.persistence.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import com.angel.architecture.persistence.base.PersistentObject;

/**
 * @author William
 */
@Entity
public class ErrorMessage extends PersistentObject {

    /**
	 *
	 */
    private static final long serialVersionUID = -3645062364220669707L;

    @Column(nullable = false, unique = false, length = 1000)
    private String message;

    @Column(nullable = false, unique = false, length = 1000)
    private String stacktrace;

    @Column(nullable = false, unique = false, length = 1000)
    private String otherDetails;

    @Column(nullable = false, unique = false)
    private String errorID;

    public ErrorMessage() {
        super();
    }

    public ErrorMessage(Exception exception, String otherDetails) {
        super();
        this.message = exception.getMessage();
        this.stacktrace = exception.getStackTrace().toString();
        this.otherDetails = otherDetails;
        this.errorID = exception.getCause().getMessage();
    }

    public String getStacktrace() {
        return stacktrace;
    }

    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }

    public String getErrorID() {
        return errorID;
    }

    public void setErrorID(String errorID) {
        this.errorID = errorID;
    }
}
