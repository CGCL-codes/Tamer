package org.apache.jackrabbit.demo.blog.exception;

import javax.servlet.ServletException;

/**
 * Exception class used to denote the errors occur in jackrabbit-jcr-demo
 */
public class JackrabbitJCRDemoException extends ServletException {

    /**
	 * Serial version id
	 */
    private static final long serialVersionUID = 3186068994617847342L;

    private String errorCode;

    private String errorMessage;

    public JackrabbitJCRDemoException(String errorCode, Exception e) {
        this.errorCode = errorCode;
        this.errorMessage = ErrorConfig.getErrorMessage(errorCode);
        if (ErrorConfig.DEBUG) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
