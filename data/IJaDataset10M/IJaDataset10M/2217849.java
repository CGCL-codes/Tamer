package ru.satseqsys.model;

import java.util.Date;

public class SMSReceivedMessage extends DataBaseObject {

    private static final long serialVersionUID = 1L;

    private Long smsReceivedMessageId;

    private String text;

    private Long simCardId;

    private Date receivedDate;

    private Date processedDate;

    public Long getSmsReceivedMessageId() {
        return smsReceivedMessageId;
    }

    public void setSmsReceivedMessageId(Long smsReceivedMessageId) {
        this.smsReceivedMessageId = smsReceivedMessageId;
    }

    public Long getSimCardId() {
        return simCardId;
    }

    public void setSimCardId(Long simCardId) {
        this.simCardId = simCardId;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    public Date getProcessedDate() {
        return processedDate;
    }

    public void setProcessedDate(Date processedDate) {
        this.processedDate = processedDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
