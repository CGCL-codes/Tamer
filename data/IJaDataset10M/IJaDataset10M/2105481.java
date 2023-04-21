package com.kescom.matrix.core.comm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class CommMessageBase implements ICommMessage {

    private String body;

    private List<ICommParty> recipients = new ArrayList<ICommParty>();

    private ICommParty sender;

    private Date timeSent;

    public List<ICommParty> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<ICommParty> recipients) {
        this.recipients = recipients;
    }

    public ICommParty getSender() {
        return sender;
    }

    public void setSender(ICommParty sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }
}
