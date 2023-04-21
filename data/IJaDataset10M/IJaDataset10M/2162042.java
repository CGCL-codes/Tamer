package org.koossery.adempiere.core.contract.criteria.client;

import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;
import org.koossery.adempiere.core.contract.itf.client.IAD_ClientShareDTO;

public class AD_ClientShareCriteria extends KTADempiereBaseCriteria implements IAD_ClientShareDTO {

    private static final long serialVersionUID = 1L;

    private int ad_ClientShare_ID;

    private int ad_Table_ID;

    private String description;

    private String name;

    private String shareType;

    private String isActive;

    public int getAd_ClientShare_ID() {
        return ad_ClientShare_ID;
    }

    public void setAd_ClientShare_ID(int ad_ClientShare_ID) {
        this.ad_ClientShare_ID = ad_ClientShare_ID;
    }

    public int getAd_Table_ID() {
        return ad_Table_ID;
    }

    public void setAd_Table_ID(int ad_Table_ID) {
        this.ad_Table_ID = ad_Table_ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
