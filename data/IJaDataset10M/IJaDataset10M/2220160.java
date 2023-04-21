package org.openxava.test.ejb;

/**
 * Data object for Family.
 */
public class FamilyData extends java.lang.Object implements java.io.Serializable {

    private java.lang.String oid;

    private int number;

    private java.lang.String description;

    public FamilyData() {
    }

    public FamilyData(FamilyData otherData) {
        setOid(otherData.getOid());
        setNumber(otherData.getNumber());
        setDescription(otherData.getDescription());
    }

    public org.openxava.test.ejb.FamilyKey getPrimaryKey() {
        org.openxava.test.ejb.FamilyKey pk = new org.openxava.test.ejb.FamilyKey(this.getOid());
        return pk;
    }

    public java.lang.String getOid() {
        return this.oid;
    }

    public void setOid(java.lang.String oid) {
        this.oid = oid;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public java.lang.String getDescription() {
        return this.description;
    }

    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    public String toString() {
        StringBuffer str = new StringBuffer("{");
        str.append("oid=" + getOid() + " " + "number=" + getNumber() + " " + "description=" + getDescription());
        str.append('}');
        return (str.toString());
    }

    public boolean equals(Object pOther) {
        if (pOther instanceof FamilyData) {
            FamilyData lTest = (FamilyData) pOther;
            boolean lEquals = true;
            if (this.oid == null) {
                lEquals = lEquals && (lTest.oid == null);
            } else {
                lEquals = lEquals && this.oid.equals(lTest.oid);
            }
            lEquals = lEquals && this.number == lTest.number;
            if (this.description == null) {
                lEquals = lEquals && (lTest.description == null);
            } else {
                lEquals = lEquals && this.description.equals(lTest.description);
            }
            return lEquals;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + ((this.oid != null) ? this.oid.hashCode() : 0);
        result = 37 * result + (int) number;
        result = 37 * result + ((this.description != null) ? this.description.hashCode() : 0);
        return result;
    }
}
