package org.openkonnect.accessor.openbravo;

import org.openbravo.base.secureApp.VariablesSecureApp;

public class OpenKonnectSession extends VariablesSecureApp {

    private String adUserId = null;

    private String adGroupId = null;

    private String adClientId = null;

    private String adOrgId = null;

    private String countryId = null;

    private String regionId = null;

    public OpenKonnectSession(String strUser, String strClient, String strOrganisation) {
        super(strUser, strClient, strOrganisation);
        this.adUserId = "14400002";
        this.adClientId = "14400000";
        this.adOrgId = "14400000";
        this.adGroupId = "1000004";
        this.countryId = "101";
        this.regionId = "255";
    }

    public String getAdUserId() {
        return adUserId;
    }

    public String getAdGroupId() {
        return adGroupId;
    }

    public String getAdClientId() {
        return adClientId;
    }

    public String getAdOrgId() {
        return adOrgId;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getRegionId() {
        return regionId;
    }
}
