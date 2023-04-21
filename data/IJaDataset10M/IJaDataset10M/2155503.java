package org.koossery.adempiere.core.contract.dto.ad;

import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;

public class AD_PrintColorDTO extends KTADempiereBaseDTO {

    private static final long serialVersionUID = 1L;

    private int ad_PrintColor_ID;

    private String code;

    private String name;

    private String isDefault;

    private String isActive;

    public int getAd_PrintColor_ID() {
        return ad_PrintColor_ID;
    }

    public void setAd_PrintColor_ID(int ad_PrintColor_ID) {
        this.ad_PrintColor_ID = ad_PrintColor_ID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
