package com.fernleaf.server.exception;

/**
 * This exception will be thrown, when the application to identify a LogBag that does not exist in the temp store
 *
 * @author Jaydeep Nargund
 * @since 1.0
 * @version 1.0
 *
 */
public class LogBagDoesNotExistException extends LogBagException {

    /** 
	 * This attribute is used to specify the LogBag that was closed when the LogBag application attempted
	 * to write to it.
	 */
    private String logBagID;

    /**
	 * Creates an Instance of the LogBagClosedException
	 */
    public LogBagDoesNotExistException(String logBagID) {
        super(LogBagServerErrorCodes.LBS_ERR_LOGBAG_NOT_FOUND);
        this.logBagID = logBagID;
    }

    /**
	 * @return Returns the logBagID.
	 */
    public String getLogBagID() {
        return logBagID;
    }

    /**
	 * @param logBagID The logBagID to set.
	 */
    public void setLogBagID(String logBagID) {
        this.logBagID = logBagID;
    }

    /** 
	 * This method gives the String representation of the Object
	 */
    public String toString() {
        StringBuffer buff = new StringBuffer();
        buff.append("LogBagDoesNotExistException [");
        buff.append("\n errorCode :: " + this.getErrorCode());
        buff.append("\n message :: " + this.getMessage());
        buff.append("\n logBagID :: " + this.logBagID);
        buff.append("\n ]");
        return buff.toString();
    }
}
