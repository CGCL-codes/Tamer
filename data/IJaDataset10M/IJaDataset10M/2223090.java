package org.bpaul.rtalk.protocol;

public class CSMsgMobileUserResponse extends Response {

    private String from;

    private String to;

    private String message;

    public CSMsgMobileUserResponse() {
        super(RESPONSE_ID_MSGMOBILEUSER);
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
