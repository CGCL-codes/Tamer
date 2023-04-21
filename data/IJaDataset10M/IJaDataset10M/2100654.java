package org.pausequafe.data.item;

public class ItemAttribute {

    private int attributeID;

    private String attributeName;

    private String attributeCategory;

    private double value;

    private String unit;

    private int unitID;

    public ItemAttribute(int attributeID, String attributeName, String attributeCategory, double value, String unit, int unitID) {
        this.attributeID = attributeID;
        this.attributeName = attributeName;
        this.attributeCategory = attributeCategory;
        this.unit = unit;
        this.value = value;
        this.setUnitID(unitID);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public String getAttributeCategory() {
        return attributeCategory;
    }

    public double getValue() {
        return value;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setUnitID(int unitID) {
        this.unitID = unitID;
    }

    public int getUnitID() {
        return unitID;
    }

    public void setAttributeID(int attributeID) {
        this.attributeID = attributeID;
    }

    public int getAttributeID() {
        return attributeID;
    }
}
