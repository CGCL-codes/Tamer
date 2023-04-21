package org.epo.jbps.heart.events;

import org.epo.jbps.bpaInterface.*;
import java.sql.Timestamp;

/**
 * Acknowledger In Queue event
 * @author Infotel Conseil
 */
public class AckEvtInQueue extends AckEvent {

    private BpaRequest req = null;

    private String printerName = null;

    private String userId = null;

    private String jobName = null;

    private int nbPages = 0;

    private Timestamp timestamp = null;

    private String ticketNumber = null;

    /**
 * AckEvtInQueue constructor .
 * @param theReq BpaRequest
 * @param theTicketNumber String
 * @param theTimestamp Timestamp
 * @param thePrinterName String
 * @param theUserId String
 * @param theJobName String
 * @param theNbPages int
 */
    public AckEvtInQueue(BpaRequest theReq, String theTicketNumber, Timestamp theTimestamp, String thePrinterName, String theUserId, String theJobName, int theNbPages) {
        super();
        setReq(theReq);
        setTicketNumber(theTicketNumber);
        setTimestamp(theTimestamp);
        setPrinterName(thePrinterName);
        setUserId(theUserId);
        setJobName(theJobName);
        setNbPages(theNbPages);
    }

    /**
 * errMessage variable accesser
 * @return java.lang.String
 */
    public String getErrMessage() {
        return null;
    }

    /**
 * jobName variable accesser
 * @return java.lang.String
 */
    public String getJobName() {
        return jobName;
    }

    /**
 * nbPages variable accesser
 * @return int
 */
    public int getNbPages() {
        return nbPages;
    }

    /**
 * printerName variable accesser
 * @return java.lang.String
 */
    public String getPrinterName() {
        return printerName;
    }

    /**
 * req variable accesser
 * @return epo.jbps.bpaInterface.BpaRequest
 */
    public BpaRequest getReq() {
        return req;
    }

    /**
 * ticketNumber variable accesser
 * @return java.lang.String
 */
    public String getTicketNumber() {
        return ticketNumber;
    }

    /**
 * timestamp variable accesser
 * @return java.lang.String
 */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
 * Returns the type of the acknowledger event
 * @return char
 */
    public char getType() {
        return INQUEUE;
    }

    /**
 * userId variable accesser
 * @return java.lang.String
 */
    public String getUserId() {
        return userId;
    }

    /**
 * jobName variable setter
 * @param newValue java.lang.String
 */
    public void setJobName(String newValue) {
        this.jobName = newValue;
    }

    /**
 * nbPages variable setter
 * @param newValue int
 */
    public void setNbPages(int newValue) {
        this.nbPages = newValue;
    }

    /**
 * printerName variable setter
 * @param newValue java.lang.String
 */
    public void setPrinterName(String newValue) {
        this.printerName = newValue;
    }

    /**
 * req variable setter
 * @param newValue epo.jbps.bpaInterface.BpaRequest
 */
    public void setReq(BpaRequest newValue) {
        this.req = newValue;
    }

    /**
 * ticketNumber variable setter
 * @param newValue java.lang.String
 */
    public void setTicketNumber(String newValue) {
        this.ticketNumber = newValue;
    }

    /**
 * timestamp variable setter
 * @param newValue java.lang.String
 */
    private void setTimestamp(Timestamp newValue) {
        this.timestamp = newValue;
    }

    /**
 * userId variable setter
 * @param newValue java.lang.String
 */
    public void setUserId(String newValue) {
        this.userId = newValue;
    }
}
