package com.koossery.adempiere.fe.beans.security.changeAudit;

public class M_ChangeNoticeBean {

    private static final long serialVersionUID = 1L;

    private String description;

    private String detailInfo;

    private String help;

    private int m_ChangeNotice_ID;

    private String name;

    private String isApproved;

    private String isProcessed;

    private String isProcessing;

    private String isActive;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(String detailInfo) {
        this.detailInfo = detailInfo;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public int getM_ChangeNotice_ID() {
        return m_ChangeNotice_ID;
    }

    public void setM_ChangeNotice_ID(int m_ChangeNotice_ID) {
        this.m_ChangeNotice_ID = m_ChangeNotice_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsProcessed() {
        return isProcessed;
    }

    public void setIsProcessed(String isProcessed) {
        this.isProcessed = isProcessed;
    }

    public String getIsProcessing() {
        return isProcessing;
    }

    public void setIsProcessing(String isProcessing) {
        this.isProcessing = isProcessing;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
