package org.openxava.test.model;

/**
 * Data object for Office2.
 */
public class Office2Data extends java.lang.Object implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private java.lang.Integer _Receptionist;

    private java.lang.String _Name;

    private int number;

    private int zoneNumber;

    private int _OfficeManager_number;

    private java.lang.Integer _DefaultCarrier_number;

    private java.lang.Integer _MainWarehouse_number;

    public Office2Data() {
    }

    public Office2Data(Office2Data otherData) {
        set_Receptionist(otherData.get_Receptionist());
        set_Name(otherData.get_Name());
        setNumber(otherData.getNumber());
        setZoneNumber(otherData.getZoneNumber());
        set_OfficeManager_number(otherData.get_OfficeManager_number());
        set_DefaultCarrier_number(otherData.get_DefaultCarrier_number());
        set_MainWarehouse_number(otherData.get_MainWarehouse_number());
    }

    public org.openxava.test.model.Office2Key getPrimaryKey() {
        org.openxava.test.model.Office2Key pk = new org.openxava.test.model.Office2Key(this.getNumber(), this.getZoneNumber(), this.get_MainWarehouse_number());
        return pk;
    }

    public java.lang.Integer get_Receptionist() {
        return this._Receptionist;
    }

    public void set_Receptionist(java.lang.Integer _Receptionist) {
        this._Receptionist = _Receptionist;
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

    public int getZoneNumber() {
        return this.zoneNumber;
    }

    public void setZoneNumber(int zoneNumber) {
        this.zoneNumber = zoneNumber;
    }

    public int get_OfficeManager_number() {
        return this._OfficeManager_number;
    }

    public void set_OfficeManager_number(int _OfficeManager_number) {
        this._OfficeManager_number = _OfficeManager_number;
    }

    public java.lang.Integer get_DefaultCarrier_number() {
        return this._DefaultCarrier_number;
    }

    public void set_DefaultCarrier_number(java.lang.Integer _DefaultCarrier_number) {
        this._DefaultCarrier_number = _DefaultCarrier_number;
    }

    public java.lang.Integer get_MainWarehouse_number() {
        return this._MainWarehouse_number;
    }

    public void set_MainWarehouse_number(java.lang.Integer _MainWarehouse_number) {
        this._MainWarehouse_number = _MainWarehouse_number;
    }

    public String toString() {
        StringBuffer str = new StringBuffer("{");
        str.append("_Receptionist=" + get_Receptionist() + " " + "_Name=" + get_Name() + " " + "number=" + getNumber() + " " + "zoneNumber=" + getZoneNumber() + " " + "_OfficeManager_number=" + get_OfficeManager_number() + " " + "_DefaultCarrier_number=" + get_DefaultCarrier_number() + " " + "_MainWarehouse_number=" + get_MainWarehouse_number());
        str.append('}');
        return (str.toString());
    }

    public boolean equals(Object pOther) {
        if (pOther instanceof Office2Data) {
            Office2Data lTest = (Office2Data) pOther;
            boolean lEquals = true;
            if (this._Receptionist == null) {
                lEquals = lEquals && (lTest._Receptionist == null);
            } else {
                lEquals = lEquals && this._Receptionist.equals(lTest._Receptionist);
            }
            if (this._Name == null) {
                lEquals = lEquals && (lTest._Name == null);
            } else {
                lEquals = lEquals && this._Name.equals(lTest._Name);
            }
            lEquals = lEquals && this.number == lTest.number;
            lEquals = lEquals && this.zoneNumber == lTest.zoneNumber;
            lEquals = lEquals && this._OfficeManager_number == lTest._OfficeManager_number;
            if (this._DefaultCarrier_number == null) {
                lEquals = lEquals && (lTest._DefaultCarrier_number == null);
            } else {
                lEquals = lEquals && this._DefaultCarrier_number.equals(lTest._DefaultCarrier_number);
            }
            if (this._MainWarehouse_number == null) {
                lEquals = lEquals && (lTest._MainWarehouse_number == null);
            } else {
                lEquals = lEquals && this._MainWarehouse_number.equals(lTest._MainWarehouse_number);
            }
            return lEquals;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + ((this._Receptionist != null) ? this._Receptionist.hashCode() : 0);
        result = 37 * result + ((this._Name != null) ? this._Name.hashCode() : 0);
        result = 37 * result + (int) number;
        result = 37 * result + (int) zoneNumber;
        result = 37 * result + (int) _OfficeManager_number;
        result = 37 * result + ((this._DefaultCarrier_number != null) ? this._DefaultCarrier_number.hashCode() : 0);
        result = 37 * result + ((this._MainWarehouse_number != null) ? this._MainWarehouse_number.hashCode() : 0);
        return result;
    }
}
