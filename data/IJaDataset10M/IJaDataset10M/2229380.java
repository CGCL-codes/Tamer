package org.openxava.test.ejb;

/**
 * Data object for Subfamily2.
 */
public class Subfamily2Data extends java.lang.Object implements java.io.Serializable {

    private java.lang.String _Remarks;

    private java.lang.String _Description;

    private int number;

    private int family_number;

    public Subfamily2Data() {
    }

    public Subfamily2Data(Subfamily2Data otherData) {
        set_Remarks(otherData.get_Remarks());
        set_Description(otherData.get_Description());
        setNumber(otherData.getNumber());
        setFamily_number(otherData.getFamily_number());
    }

    public org.openxava.test.ejb.Subfamily2Key getPrimaryKey() {
        org.openxava.test.ejb.Subfamily2Key pk = new org.openxava.test.ejb.Subfamily2Key(this.getNumber());
        return pk;
    }

    public java.lang.String get_Remarks() {
        return this._Remarks;
    }

    public void set_Remarks(java.lang.String _Remarks) {
        this._Remarks = _Remarks;
    }

    public java.lang.String get_Description() {
        return this._Description;
    }

    public void set_Description(java.lang.String _Description) {
        this._Description = _Description;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getFamily_number() {
        return this.family_number;
    }

    public void setFamily_number(int family_number) {
        this.family_number = family_number;
    }

    public String toString() {
        StringBuffer str = new StringBuffer("{");
        str.append("_Remarks=" + get_Remarks() + " " + "_Description=" + get_Description() + " " + "number=" + getNumber() + " " + "family_number=" + getFamily_number());
        str.append('}');
        return (str.toString());
    }

    public boolean equals(Object pOther) {
        if (pOther instanceof Subfamily2Data) {
            Subfamily2Data lTest = (Subfamily2Data) pOther;
            boolean lEquals = true;
            if (this._Remarks == null) {
                lEquals = lEquals && (lTest._Remarks == null);
            } else {
                lEquals = lEquals && this._Remarks.equals(lTest._Remarks);
            }
            if (this._Description == null) {
                lEquals = lEquals && (lTest._Description == null);
            } else {
                lEquals = lEquals && this._Description.equals(lTest._Description);
            }
            lEquals = lEquals && this.number == lTest.number;
            lEquals = lEquals && this.family_number == lTest.family_number;
            return lEquals;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + ((this._Remarks != null) ? this._Remarks.hashCode() : 0);
        result = 37 * result + ((this._Description != null) ? this._Description.hashCode() : 0);
        result = 37 * result + (int) number;
        result = 37 * result + (int) family_number;
        return result;
    }
}
