package org.koossery.adempiere.core.contract.criteria.role;

import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;
import org.koossery.adempiere.core.contract.itf.role.IAD_Column_AccessDTO;

public class AD_Column_AccessCriteria extends KTADempiereBaseCriteria implements IAD_Column_AccessDTO {

    private static final long serialVersionUID = 1L;

    private int ad_Column_ID;

    private int ad_Role_ID;

    private int ad_Table_ID;

    private String isExclude;

    private String isReadOnly;

    private String isActive;

    public int getAd_Column_ID() {
        return ad_Column_ID;
    }

    public void setAd_Column_ID(int ad_Column_ID) {
        this.ad_Column_ID = ad_Column_ID;
    }

    public int getAd_Role_ID() {
        return ad_Role_ID;
    }

    public void setAd_Role_ID(int ad_Role_ID) {
        this.ad_Role_ID = ad_Role_ID;
    }

    public int getAd_Table_ID() {
        return ad_Table_ID;
    }

    public void setAd_Table_ID(int ad_Table_ID) {
        this.ad_Table_ID = ad_Table_ID;
    }

    public String getIsExclude() {
        return isExclude;
    }

    public void setIsExclude(String isExclude) {
        this.isExclude = isExclude;
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
