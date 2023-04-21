package net.entelijan.cobean.bind.impl;

public class SimpleBBean implements ISimpleBBean {

    private String strVal;

    public SimpleBBean() {
        super();
    }

    public SimpleBBean(String strVal) {
        super();
        this.strVal = strVal;
    }

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }

    @Override
    public String toString() {
        return "SimpleBBean [strVal=" + strVal + "]";
    }
}
