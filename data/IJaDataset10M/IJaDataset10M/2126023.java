package org.kablink.teaming.remoting.ws.model;

import java.util.Calendar;

public class ReleaseInfo {

    private String productName;

    private String productVersion;

    private int buildNumber;

    private Calendar buildDate;

    private Calendar serverStartTime;

    private boolean licenseRequiredEdition;

    private String contentVersion;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductVersion() {
        return productVersion;
    }

    public void setProductVersion(String productVersion) {
        this.productVersion = productVersion;
    }

    public int getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(int buildNumber) {
        this.buildNumber = buildNumber;
    }

    public Calendar getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(Calendar buildDate) {
        this.buildDate = buildDate;
    }

    public Calendar getServerStartTime() {
        return serverStartTime;
    }

    public void setServerStartTime(Calendar serverStartTime) {
        this.serverStartTime = serverStartTime;
    }

    public boolean isLicenseRequiredEdition() {
        return licenseRequiredEdition;
    }

    public void setLicenseRequiredEdition(boolean licenseRequiredEdition) {
        this.licenseRequiredEdition = licenseRequiredEdition;
    }

    public String getContentVersion() {
        return contentVersion;
    }

    public void setContentVersion(String contentVersion) {
        this.contentVersion = contentVersion;
    }
}
