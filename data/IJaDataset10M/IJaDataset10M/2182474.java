package com.patientis.framework.logging;

import com.patientis.model.common.DateTimeModel;

/**
 * One line class description
 *
 */
public class LogMessage {

    /**
	 * 
	 */
    private String errorType = null;

    /**
	 * 
	 */
    private String message = null;

    /**
	 * 
	 */
    private DateTimeModel dateTime = null;

    /**
	 * @return the errorType
	 */
    public String getErrorType() {
        return errorType;
    }

    /**
	 * @param errorType the errorType to set
	 */
    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    /**
	 * @return the message
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * @param message the message to set
	 */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
	 * @return the dateTime
	 */
    public DateTimeModel getDateTime() {
        return dateTime;
    }

    /**
	 * @param dateTime the dateTime to set
	 */
    public void setDateTime(DateTimeModel dateTime) {
        this.dateTime = dateTime;
    }
}
