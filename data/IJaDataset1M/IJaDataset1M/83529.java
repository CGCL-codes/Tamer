package org.bpaul.rtalk.protocol;

public class CSTypingNotifyResponse extends Response {

    private String from;

    private String to;

    public CSTypingNotifyResponse(String from, String to) {
        super(RESPONSE_ID_TYPING_NOTIFY);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }
}
