package com.entelience.objects.asset;

import java.util.Date;

public class VendorHistory implements java.io.Serializable {

    public VendorHistory() {
    }

    private VendorId vendorId;

    private boolean active;

    private boolean hidden;

    private String modifier;

    private Date changeDate;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public VendorId getVendorId() {
        return vendorId;
    }

    public void setVendorId(VendorId vendorId) {
        this.vendorId = vendorId;
    }
}
