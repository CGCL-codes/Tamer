package org.tamacat.groovy.test;

public class Param {

    private String stringValue;

    private String stringValue2;

    private int intValue;

    private long longValue;

    private float floatValue;

    private double doubleValue;

    private boolean booleanValue;

    private char charValue;

    private String initValue;

    private Param param;

    private Param p2;

    public Param() {
    }

    public Param(Param param) {
        this.param = param;
    }

    public Param(Param param, Param p2) {
        this.param = param;
        this.p2 = p2;
    }

    public Param(String name) {
        this.stringValue = name;
    }

    public Param(String name, String name2) {
        this.stringValue = name;
        this.stringValue2 = name2;
    }

    public void init() {
        initValue = "Initialized";
    }

    public String getInitValue() {
        return initValue;
    }

    public Param getParamTest() {
        return param;
    }

    public Param getParam2() {
        return p2;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public String getStringValue2() {
        return stringValue2;
    }

    public void setVoid() {
        System.out.println("execute");
    }

    public char getCharValue() {
        return charValue;
    }

    public void setCharValue(char charValue) {
        this.charValue = charValue;
    }
}
