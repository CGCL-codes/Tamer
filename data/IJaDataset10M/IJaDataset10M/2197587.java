package org.ikasan.tools.errormanager.model;

import java.util.Date;
import org.apache.log4j.Logger;

public class BusinessError {

    private Long id;

    private String errorMessage;

    private Date timeReceived;

    private String originatingSystem;

    private boolean resubmittable = true;

    private static Logger logger = Logger.getLogger(BusinessError.class);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setExternalReference(String externalReference) {
        this.externalReference = externalReference;
    }

    private String externalReference;

    public BusinessError(String originatingSystem, String externalReference, String errorMessage) {
        this.originatingSystem = originatingSystem;
        this.externalReference = externalReference;
        this.errorMessage = errorMessage;
    }

    public String getExternalReference() {
        return externalReference;
    }

    public String getErrorSummary() {
        String result;
        String firstLine = errorMessage;
        int endOfFirstLine = errorMessage.indexOf("\n");
        if (endOfFirstLine > -1) {
            firstLine = errorMessage.substring(0, endOfFirstLine);
        }
        result = firstLine;
        if (result.length() > 80) {
            result = result.substring(0, 77) + "...";
        }
        return result;
    }

    public void setOriginatingSystem(String originatingSystem) {
        this.originatingSystem = originatingSystem;
    }

    public String getOriginatingSystem() {
        return originatingSystem;
    }

    @Override
    public String toString() {
        return "BusinessError [" + ", errorSummary=" + getErrorSummary() + ", externalReference=" + externalReference + ", id=" + id + "]";
    }

    public boolean isResubmittable() {
        return resubmittable;
    }

    public void setTimeReceived(Date timeReceived) {
        this.timeReceived = timeReceived;
    }

    public Date getTimeReceived() {
        return timeReceived;
    }
}
