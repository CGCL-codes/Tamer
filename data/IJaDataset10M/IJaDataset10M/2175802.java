package com.koossery.adempiere.fe.beans.systemRules;

import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.error.IAD_ErrorDTO;

public class AD_ErrorBean {

    private static final long serialVersionUID = 1L;

    private int ad_Error_ID;

    private String ad_Language;

    private String code;

    private String name;

    private String isActive;

    public int getAd_Error_ID() {
        return ad_Error_ID;
    }

    public void setAd_Error_ID(int ad_Error_ID) {
        this.ad_Error_ID = ad_Error_ID;
    }

    public String getAd_Language() {
        return ad_Language;
    }

    public void setAd_Language(String ad_Language) {
        this.ad_Language = ad_Language;
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

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
