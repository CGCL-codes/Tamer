package net.jwde.object;

import java.util.Date;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TestPersistenceCapable extends GenericModel {

    private static final long serialVersionUID = 1L;

    private long testPersistenceCapableID;

    private String stringField;

    private int intField;

    private long longField;

    private float floatField;

    private boolean booleanField;

    private Date dateField;

    private BigDecimal bigDecimalField;

    private BigInteger bigIntegerField;

    private byte byteField;

    private short shortField;

    private double doubleField;

    public TestPersistenceCapable(String stringField, int intField, long longField, float floatField, boolean booleanField, Date dateField, BigDecimal bigDecimalField, BigInteger bigIntegerField, byte byteField, short shortField, double doubleField) {
        this.stringField = stringField;
        this.intField = intField;
        this.longField = longField;
        this.floatField = floatField;
        this.booleanField = booleanField;
        this.dateField = dateField;
        this.bigDecimalField = bigDecimalField;
        this.bigIntegerField = bigIntegerField;
        this.byteField = byteField;
        this.shortField = shortField;
        this.doubleField = doubleField;
    }

    public boolean isBooleanField() {
        return booleanField;
    }

    public void setBooleanField(boolean booleanField) {
        this.booleanField = booleanField;
    }

    public Date getDateField() {
        return dateField;
    }

    public void setDateField(Date dateField) {
        this.dateField = dateField;
    }

    public BigDecimal getBigDecimalField() {
        return bigDecimalField;
    }

    public void setBigDecimalField(BigDecimal bigDecimalField) {
        this.bigDecimalField = bigDecimalField;
    }

    public byte getByteField() {
        return byteField;
    }

    public void setByteField(byte byteField) {
        this.byteField = byteField;
    }

    public short getShortField() {
        return shortField;
    }

    public void setShortField(short shortField) {
        this.shortField = shortField;
    }

    public double getDoubleField() {
        return doubleField;
    }

    public void setDoubleField(double doubleField) {
        this.doubleField = doubleField;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getTestPersistenceCapableID() {
        return testPersistenceCapableID;
    }

    public void setTestPersistenceCapableID(long testPersistenceCapableID) {
        this.testPersistenceCapableID = testPersistenceCapableID;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }

    public long getLongField() {
        return longField;
    }

    public void setLongField(long longField) {
        this.longField = longField;
    }

    public float getFloatField() {
        return floatField;
    }

    public void setFloatField(float floatField) {
        this.floatField = floatField;
    }

    public BigInteger getBigIntegerField() {
        return bigIntegerField;
    }

    public void setBigIntegerField(BigInteger bigIntegerField) {
        this.bigIntegerField = bigIntegerField;
    }
}
