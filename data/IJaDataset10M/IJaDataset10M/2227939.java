package org.openxava.test.model;

/**
 * Data object for DrivingLicence.
 */
public class DrivingLicenceData extends java.lang.Object implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private int level;

    private java.lang.String _Description;

    private java.lang.String type;

    public DrivingLicenceData() {
    }

    public DrivingLicenceData(DrivingLicenceData otherData) {
        setLevel(otherData.getLevel());
        set_Description(otherData.get_Description());
        setType(otherData.getType());
    }

    public org.openxava.test.model.DrivingLicenceKey getPrimaryKey() {
        org.openxava.test.model.DrivingLicenceKey pk = new org.openxava.test.model.DrivingLicenceKey(this.getLevel(), this.getType());
        return pk;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public java.lang.String get_Description() {
        return this._Description;
    }

    public void set_Description(java.lang.String _Description) {
        this._Description = _Description;
    }

    public java.lang.String getType() {
        return this.type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    public String toString() {
        StringBuffer str = new StringBuffer("{");
        str.append("level=" + getLevel() + " " + "_Description=" + get_Description() + " " + "type=" + getType());
        str.append('}');
        return (str.toString());
    }

    public boolean equals(Object pOther) {
        if (pOther instanceof DrivingLicenceData) {
            DrivingLicenceData lTest = (DrivingLicenceData) pOther;
            boolean lEquals = true;
            lEquals = lEquals && this.level == lTest.level;
            if (this._Description == null) {
                lEquals = lEquals && (lTest._Description == null);
            } else {
                lEquals = lEquals && this._Description.equals(lTest._Description);
            }
            if (this.type == null) {
                lEquals = lEquals && (lTest.type == null);
            } else {
                lEquals = lEquals && this.type.equals(lTest.type);
            }
            return lEquals;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (int) level;
        result = 37 * result + ((this._Description != null) ? this._Description.hashCode() : 0);
        result = 37 * result + ((this.type != null) ? this.type.hashCode() : 0);
        return result;
    }
}
