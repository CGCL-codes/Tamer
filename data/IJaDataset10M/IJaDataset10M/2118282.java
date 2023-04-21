package org.keyintegrity.webbeans;

import java.math.BigDecimal;
import java.util.Date;

public class Bean {

    private int i;

    private boolean b;

    private double d;

    private BigDecimal decimal;

    private Date date;

    private SimpleEnum simpleEnum;

    private String string;

    private JaxbEnum jaxbEnum;

    public JaxbEnum getJaxbEnum() {
        return jaxbEnum;
    }

    public void setJaxbEnum(JaxbEnum jaxbEnum) {
        this.jaxbEnum = jaxbEnum;
    }

    public SimpleEnum getSimpleEnum() {
        return simpleEnum;
    }

    public void setSimpleEnum(SimpleEnum simpleEnum) {
        this.simpleEnum = simpleEnum;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }
}
