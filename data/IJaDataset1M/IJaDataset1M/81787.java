package org.koossery.adempiere.core.contract.dto.comptabilite;

import java.sql.Date;
import java.math.*;

public class GLCategoryDTO {

    BigDecimal AccessLevel = BigDecimal.valueOf(2);

    public static final int Table_ID = 218;

    public static final String Table_Name = "GL_Category";

    public static int getTable_ID() {
        return Table_ID;
    }

    public static String getTable_Name() {
        return Table_Name;
    }

    public int ad_Client_ID;

    public int getAd_Client_ID() {
        return ad_Client_ID;
    }

    public void setAd_Client_ID(int ad_Client_ID) {
        this.ad_Client_ID = ad_Client_ID;
    }

    public int ad_Org_ID;

    public int getAd_Org_ID() {
        return ad_Org_ID;
    }

    public void setAd_Org_ID(int ad_Org_ID) {
        this.ad_Org_ID = ad_Org_ID;
    }

    public String isactive;

    public String getIsactive() {
        return isactive;
    }

    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String categoryType;

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String CategoryType) {
        this.categoryType = CategoryType;
    }

    public String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String Description) {
        this.description = Description;
    }

    public int gL_Category_ID;

    public int getGL_Category_ID() {
        return gL_Category_ID;
    }

    public void setGL_Category_ID(int GL_Category_ID) {
        this.gL_Category_ID = GL_Category_ID;
    }

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public String isDefault;

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Date created;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int createdby;

    public int getCreatedby() {
        return createdby;
    }

    public void setCreatedby(int createdby) {
        this.createdby = createdby;
    }

    public Date updated;

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int updatedby;

    public int getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(int updatedby) {
        this.updatedby = updatedby;
    }

    public int getId() {
        return getGL_Category_ID();
    }
}
