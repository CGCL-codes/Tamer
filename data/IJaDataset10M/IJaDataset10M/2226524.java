package org.koossery.adempiere.core.contract.dto.user;

import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.user.IAD_User_OrgAccessDTO;

public class AD_User_OrgAccessDTO extends KTADempiereBaseDTO implements IAD_User_OrgAccessDTO {

    private static final long serialVersionUID = 1L;

    private int ad_User_ID;

    private String isReadOnly;

    private String isActive;

    public int getAd_User_ID() {
        return ad_User_ID;
    }

    public void setAd_User_ID(int ad_User_ID) {
        this.ad_User_ID = ad_User_ID;
    }

    public String getIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(String isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
