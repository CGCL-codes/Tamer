package com.entelience.objects.mim;

/**
* Represents the data to be displayed on a snail
*/
public class SnailPortion implements java.io.Serializable {

    private String name;

    private String long_name;

    private boolean hasChildren;

    private int ddsNumber;

    private String ddsString;

    private int ddeNumber;

    private String ddeString;

    private int ddcNumber;

    private String ddcString;

    private int dpcNumber;

    private String dpcString;

    public SnailPortion() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setDdsNumber(int ddsNumber) {
        this.ddsNumber = ddsNumber;
    }

    public int getDdsNumber() {
        return ddsNumber;
    }

    public void setDdsString(String ddsString) {
        this.ddsString = ddsString;
    }

    public String getDdsString() {
        return ddsString;
    }

    public void setDdeNumber(int ddeNumber) {
        this.ddeNumber = ddeNumber;
    }

    public int getDdeNumber() {
        return ddeNumber;
    }

    public void setDdeString(String ddeString) {
        this.ddeString = ddeString;
    }

    public String getDdeString() {
        return ddeString;
    }

    public void setDdcNumber(int ddcNumber) {
        this.ddcNumber = ddcNumber;
    }

    public int getDdcNumber() {
        return ddcNumber;
    }

    public void setDdcString(String ddcString) {
        this.ddcString = ddcString;
    }

    public String getDdcString() {
        return ddcString;
    }

    public void setDpcNumber(int dpcNumber) {
        this.dpcNumber = dpcNumber;
    }

    public int getDpcNumber() {
        return dpcNumber;
    }

    public void setDpcString(String dpcString) {
        this.dpcString = dpcString;
    }

    public String getDpcString() {
        return dpcString;
    }
}
