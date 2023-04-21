package org.rr.jsendfile.util;

/**
 * Exception for the ErrorCodes in ReplyMessage (Code starts with 3xx,4xx,5xx)
 * Throws the full ReplyMessage (with number)
 */
public class ReplyMessageException extends Exception {

    String errNum;

    /**
     * Creates a new instance of ReplyMessageException
     * @param inErrNum Error Number of the Exception
     */
    public ReplyMessageException(String inErrNum) {
        super(inErrNum);
        errNum = inErrNum;
    }

    /**
     * Get the full message
     * @return complete Errormessage like 5xx Error not Found
     */
    public String getErrMessage() {
        return ReplyMessage.getRepMsg(errNum);
    }

    /**
     * Get the number of the message
     * @return number of the message
     */
    public String getErrCode() {
        return ReplyMessage.getErrCode(errNum);
    }
}
