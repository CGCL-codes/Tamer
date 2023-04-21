package com.enoram.training.gwt.admin.shared;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8569525615197509050L;

    private String message;

    private Date startedDate;

    private Date endDate;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(Date startedDate) {
        this.startedDate = startedDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
