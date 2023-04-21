package org.jcompany.jdoc.web;

import java.io.Serializable;

public class PlcErrorPage implements Serializable {

    private String errorCode;

    private String exceptionType;

    private String location;

    public PlcErrorPage() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String newErrorCode) {
        errorCode = newErrorCode;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(String newExceptionType) {
        exceptionType = newExceptionType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String newLocation) {
        location = newLocation;
    }
}
