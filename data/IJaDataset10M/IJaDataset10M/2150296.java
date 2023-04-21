package com.myopa.vo;

import java.util.ArrayList;
import java.util.Date;

public class MailingCampaignVo {

    private int campaignId;

    private boolean isMaintenanceCampaign = false;

    private short communicationMethodCode;

    private String communicationMethodDesc;

    Date dateSent;

    private ArrayList<MailingListVo> mailingLists;

    private ArrayList<MailingResponseVo> mailingResponses;

    public MailingCampaignVo() {
    }

    public MailingCampaignVo(int campaignId, boolean isMaintenanceCampaign, short communicationMethodCode, String communicationMethodDesc, Date dateSent, ArrayList<MailingListVo> mailingLists, ArrayList<MailingResponseVo> mailingResponses) {
        super();
        this.campaignId = campaignId;
        this.isMaintenanceCampaign = isMaintenanceCampaign;
        this.communicationMethodCode = communicationMethodCode;
        this.communicationMethodDesc = communicationMethodDesc;
        this.dateSent = dateSent;
        this.mailingLists = mailingLists;
        this.mailingResponses = mailingResponses;
    }

    public int getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(int campaignId) {
        this.campaignId = campaignId;
    }

    public short getCommunicationMethodCode() {
        return communicationMethodCode;
    }

    public void setCommunicationMethodCode(short communicationMethodCode) {
        this.communicationMethodCode = communicationMethodCode;
    }

    public String getCommunicationMethodDesc() {
        return communicationMethodDesc;
    }

    public void setCommunicationMethodDesc(String communicationMethodDesc) {
        this.communicationMethodDesc = communicationMethodDesc;
    }

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public boolean isMaintenanceCampaign() {
        return isMaintenanceCampaign;
    }

    public void setMaintenanceCampaign(boolean isMaintenanceCampaign) {
        this.isMaintenanceCampaign = isMaintenanceCampaign;
    }

    public ArrayList<MailingListVo> getMailingLists() {
        return mailingLists;
    }

    public void setMailingLists(ArrayList<MailingListVo> mailingLists) {
        this.mailingLists = mailingLists;
    }

    public ArrayList<MailingResponseVo> getMailingResponses() {
        return mailingResponses;
    }

    public void setMailingResponses(ArrayList<MailingResponseVo> mailingResponses) {
        this.mailingResponses = mailingResponses;
    }
}
