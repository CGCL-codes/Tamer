package com.beimin.evedata.model.dgm;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Candle
 */
@Entity
@Table(name = "dgmAttributeTypes")
public class DgmAttributeType implements Serializable {

    @Id
    private int attributeID;

    String attributeName;

    String description;

    int iconID;

    Double defaultValue;

    int published;

    String displayName;

    Integer unitID;

    int stackable;

    int highIsGood;

    int categoryID;

    public int getAttributeID() {
        return attributeID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public Double getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Double defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getHighIsGood() {
        return highIsGood;
    }

    public void setHighIsGood(int highIsGood) {
        this.highIsGood = highIsGood;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public int getPublished() {
        return published;
    }

    public void setPublished(int published) {
        this.published = published;
    }

    public int getStackable() {
        return stackable;
    }

    public void setStackable(int stackable) {
        this.stackable = stackable;
    }

    public Integer getUnitID() {
        return unitID;
    }

    public void setUnitID(Integer unitID) {
        this.unitID = unitID;
    }
}
