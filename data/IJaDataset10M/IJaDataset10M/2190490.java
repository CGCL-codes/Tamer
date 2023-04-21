package de.tuc.in.sse.weit.export.openoffice.impl;

import com.foursoft.component.Config;

public class OpenOfficeConfigImpl implements Config {

    private String openOfficePath = null;

    private int openOfficePort = 8100;

    public void setOpenOfficePath(String openOfficePath) {
        assert (openOfficePath != null);
        this.openOfficePath = openOfficePath;
    }

    public String getOpenOfficePath() {
        return this.openOfficePath;
    }

    public void setOpenOfficePort(int port) {
        openOfficePort = port;
    }

    public int getOpenOfficePort() {
        return openOfficePort;
    }
}
