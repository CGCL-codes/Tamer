package org.koossery.adempiere.core.contract.criteria.ad;

import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;

public class AD_ReferenceCriteria extends KTADempiereBaseCriteria {

    private static final long serialVersionUID = 1L;

    private int ad_Reference_ID;

    private String description;

    private String entityType;

    private String help;

    private String name;

    private String validationType;

    private String vformat;

    private String isActive;

    public int getAd_Reference_ID() {
        return ad_Reference_ID;
    }

    public void setAd_Reference_ID(int ad_Reference_ID) {
        this.ad_Reference_ID = ad_Reference_ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidationType() {
        return validationType;
    }

    public void setValidationType(String validationType) {
        this.validationType = validationType;
    }

    public String getVformat() {
        return vformat;
    }

    public void setVformat(String vformat) {
        this.vformat = vformat;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
