package com.sts.webmeet.content.client;

public interface StatusUI {

    public void addStatusArea(String strName, String strImage);

    public void addStatusInfo(StatusInfo si);

    public void setStatusImage(String strName, String strImage);

    public void setStatusActive(StatusInfo si);

    public void setStatusInactive(StatusInfo si);

    public void setStatus(StatusInfo si, boolean bActive);

    public void setStatusImage(String strName, java.awt.Image image);

    public void removeStatusArea(String strName);

    public void addStatusPanel(String strName, java.awt.Panel panel, int iIndex);
}
