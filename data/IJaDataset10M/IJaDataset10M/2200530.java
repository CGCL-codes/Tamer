package org.jbfilter.test.junit;

import java.util.Date;

/**
 * @author Marcus Adrian
 *
 */
public class DummyBean {

    private String elektra;

    private String salome;

    private DummyBean nestedOne;

    private int anInt;

    private Date oneDate;

    private boolean boolPrimitif;

    private Boolean boolObject;

    private DummyEnumeration dummyEnumeration;

    public DummyEnumeration getDummyEnumeration() {
        return dummyEnumeration;
    }

    public void setDummyEnumeration(DummyEnumeration dummyEnumeration) {
        this.dummyEnumeration = dummyEnumeration;
    }

    public void setBoolPrimitif(boolean boolPrimitif) {
        this.boolPrimitif = boolPrimitif;
    }

    public void setBoolObject(Boolean boolObject) {
        this.boolObject = boolObject;
    }

    public Date getOneDate() {
        return oneDate;
    }

    public void setOneDate(Date oneDate) {
        this.oneDate = oneDate;
    }

    public int getAnInt() {
        return anInt;
    }

    public void setAnInt(int anInt) {
        this.anInt = anInt;
    }

    public DummyBean() {
        super();
    }

    public DummyBean(String elektra, String salome) {
        super();
        this.elektra = elektra;
        this.salome = salome;
    }

    public String getElektra() {
        return elektra;
    }

    public String getSalome() {
        return salome;
    }

    public DummyBean getNestedOne() {
        return nestedOne;
    }

    public void setNestedOne(DummyBean nestedOne) {
        this.nestedOne = nestedOne;
    }

    public boolean isBoolPrimitif() {
        return boolPrimitif;
    }

    public Boolean getBoolObject() {
        return boolObject;
    }
}
