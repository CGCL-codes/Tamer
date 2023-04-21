package org.openxava.test.model;

/**
 * Data object for Seller.
 */
public class SellerData extends java.lang.Object implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private java.lang.String _Name;

    private int number;

    private java.lang.String _Regions;

    private java.lang.String _Level_id;

    private int _Boss_number;

    public SellerData() {
    }

    public SellerData(SellerData otherData) {
        set_Name(otherData.get_Name());
        setNumber(otherData.getNumber());
        set_Regions(otherData.get_Regions());
        set_Level_id(otherData.get_Level_id());
        set_Boss_number(otherData.get_Boss_number());
    }

    public org.openxava.test.model.SellerKey getPrimaryKey() {
        org.openxava.test.model.SellerKey pk = new org.openxava.test.model.SellerKey(this.getNumber());
        return pk;
    }

    public java.lang.String get_Name() {
        return this._Name;
    }

    public void set_Name(java.lang.String _Name) {
        this._Name = _Name;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public java.lang.String get_Regions() {
        return this._Regions;
    }

    public void set_Regions(java.lang.String _Regions) {
        this._Regions = _Regions;
    }

    public java.lang.String get_Level_id() {
        return this._Level_id;
    }

    public void set_Level_id(java.lang.String _Level_id) {
        this._Level_id = _Level_id;
    }

    public int get_Boss_number() {
        return this._Boss_number;
    }

    public void set_Boss_number(int _Boss_number) {
        this._Boss_number = _Boss_number;
    }

    public String toString() {
        StringBuffer str = new StringBuffer("{");
        str.append("_Name=" + get_Name() + " " + "number=" + getNumber() + " " + "_Regions=" + get_Regions() + " " + "_Level_id=" + get_Level_id() + " " + "_Boss_number=" + get_Boss_number());
        str.append('}');
        return (str.toString());
    }

    public boolean equals(Object pOther) {
        if (pOther instanceof SellerData) {
            SellerData lTest = (SellerData) pOther;
            boolean lEquals = true;
            if (this._Name == null) {
                lEquals = lEquals && (lTest._Name == null);
            } else {
                lEquals = lEquals && this._Name.equals(lTest._Name);
            }
            lEquals = lEquals && this.number == lTest.number;
            if (this._Regions == null) {
                lEquals = lEquals && (lTest._Regions == null);
            } else {
                lEquals = lEquals && this._Regions.equals(lTest._Regions);
            }
            if (this._Level_id == null) {
                lEquals = lEquals && (lTest._Level_id == null);
            } else {
                lEquals = lEquals && this._Level_id.equals(lTest._Level_id);
            }
            lEquals = lEquals && this._Boss_number == lTest._Boss_number;
            return lEquals;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + ((this._Name != null) ? this._Name.hashCode() : 0);
        result = 37 * result + (int) number;
        result = 37 * result + ((this._Regions != null) ? this._Regions.hashCode() : 0);
        result = 37 * result + ((this._Level_id != null) ? this._Level_id.hashCode() : 0);
        result = 37 * result + (int) _Boss_number;
        return result;
    }
}
