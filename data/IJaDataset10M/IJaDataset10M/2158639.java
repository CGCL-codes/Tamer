package com.safi.asterisk.handler.mbean;

public class LicenseException extends Exception {

    /**
   * 
   */
    private static final long serialVersionUID = 3790327102029124019L;

    private String productID = "";

    private String desc = "";

    private String url = "";

    private String macaddress = "";

    public LicenseException(String license) {
        super(license);
    }

    public LicenseException(LicenseDesc license) {
        super(license.licenseID + " " + license.licenseDesc + " " + license.url);
        this.productID = license.licenseID;
        this.desc = license.licenseDesc;
        this.url = license.url;
        this.macaddress = license.macaddress;
        try {
            if (this.url.contains("macvalue")) {
                this.url = this.url.replace("macvalue", this.macaddress);
            }
        } catch (Exception ex) {
        }
    }

    public String getDesc() {
        return this.desc;
    }

    public String getUrl() {
        return this.url;
    }

    public String getProductID() {
        return this.productID;
    }

    public String getMacaddress() {
        return this.macaddress;
    }
}
