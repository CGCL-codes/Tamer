package org.neodatis.odb.test.vo.attribute;

import java.math.BigDecimal;
import java.util.Date;

public class TestClass {

    private int int1;

    private boolean boolean1;

    private Boolean boolean2;

    private String string1;

    private char char1;

    private BigDecimal bigDecimal1;

    private Double double1;

    private Date date1;

    public Date getDate1() {
        return date1;
    }

    public void setDate1(Date date1) {
        this.date1 = date1;
    }

    public TestClass() {
    }

    public BigDecimal getBigDecimal1() {
        return bigDecimal1;
    }

    public void setBigDecimal1(BigDecimal bigDecimal1) {
        this.bigDecimal1 = bigDecimal1;
    }

    public boolean isBoolean1() {
        return boolean1;
    }

    public void setBoolean1(boolean boolean1) {
        this.boolean1 = boolean1;
    }

    public char getChar1() {
        return char1;
    }

    public void setChar1(char char1) {
        this.char1 = char1;
    }

    public Double getDouble1() {
        return double1;
    }

    public void setDouble1(Double double1) {
        this.double1 = double1;
    }

    public int getInt1() {
        return int1;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    ;

    public void change() {
        string1 = "ola";
    }

    public String toString() {
        return double1 + " | " + string1 + " | " + int1 + "\n";
    }

    public Boolean getBoolean2() {
        return boolean2;
    }

    public void setBoolean2(Boolean boolean2) {
        this.boolean2 = boolean2;
    }
}
