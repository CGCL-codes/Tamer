package org.koossery.adempiere.core.contract.dto.data.utility;

import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.data.utility.IAD_PreferenceDTO;

public class AD_PreferenceDTO extends KTADempiereBaseDTO implements IAD_PreferenceDTO {

    private static final long serialVersionUID = 1L;

    private int ad_Preference_ID;

    private int ad_User_ID;

    private int ad_Window_ID;

    private String attribute;

    private String value;

    private String isActive;

    public int getAd_Preference_ID() {
        return ad_Preference_ID;
    }

    public void setAd_Preference_ID(int ad_Preference_ID) {
        this.ad_Preference_ID = ad_Preference_ID;
    }

    public int getAd_User_ID() {
        return ad_User_ID;
    }

    public void setAd_User_ID(int ad_User_ID) {
        this.ad_User_ID = ad_User_ID;
    }

    public int getAd_Window_ID() {
        return ad_Window_ID;
    }

    public void setAd_Window_ID(int ad_Window_ID) {
        this.ad_Window_ID = ad_Window_ID;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
