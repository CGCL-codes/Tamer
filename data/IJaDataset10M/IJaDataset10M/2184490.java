package net.sourceforge.mailprobe.util;

import java.util.Date;

/**
 *
 * @author Thomas Tesche <thomas.tesche@clustersystems.de>
 */
public class ExecStats {

    private boolean success;

    private boolean innerTimeoutOccured;

    private Date startDate;

    private Date endDate;

    private String errorMessage = "";

    public ExecStats(Date startDate) {
        this.success = false;
        this.innerTimeoutOccured = false;
        this.startDate = startDate;
    }

    /**
   * @return the success
   */
    public boolean isSuccess() {
        return success;
    }

    /**
   * @param success the success to set
   */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
   * @return the startDate
   */
    public Date getStartDate() {
        return startDate;
    }

    /**
   * @param startDate the startDate to set
   */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
   * @return the endDate
   */
    public Date getEndDate() {
        return endDate;
    }

    /**
   * @param endDate the endDate to set
   */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
   * @return the innerTimeoutOccured
   */
    public boolean isInnerTimeoutOccured() {
        return innerTimeoutOccured;
    }

    /**
   * @param innerTimeoutOccured the innerTimeoutOccured to set
   */
    public void setInnerTimeoutOccured(boolean innerTimeoutOccured) {
        this.innerTimeoutOccured = innerTimeoutOccured;
    }

    /**
   * @return the errorMessage
   */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
   * @param errorMessage the errorMessage to set
   */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
