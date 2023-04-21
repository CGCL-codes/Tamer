package com.sts.webmeet.common;

public class OpenMeetingMessagePlayback extends OpenMeetingMessage {

    public OpenMeetingMessagePlayback(String strCustomerID, String strMeetingID, String strMeetingKey) {
        super(strCustomerID, strMeetingID, strMeetingKey);
        this.strCustomerID = strCustomerID;
        this.strMeetingID = strMeetingID;
        this.strMeetingKey = strMeetingKey;
    }

    public String getCustomerID() {
        return strCustomerID;
    }

    public String getMeetingID() {
        return strMeetingID;
    }

    public String getMeetingKey() {
        return strMeetingKey;
    }

    private String strMeetingID;

    private String strCustomerID;

    private String strMeetingKey;
}
