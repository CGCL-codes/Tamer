package com.companyname.common.system.util;

public class CompanyNameSystemException extends RuntimeException {

    public CompanyNameSystemException(String message) {
        super(message);
    }

    public CompanyNameSystemException(Throwable cause) {
        super(cause);
    }

    public CompanyNameSystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
