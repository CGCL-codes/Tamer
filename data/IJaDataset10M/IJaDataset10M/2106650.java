package org.stimpack.testclasses.field;

import org.stimpack.core.aop.asm.FieldWriteDispatcher;

/**
 * This class is used to test the interception of primitive
 * type field assignments.
 * @author Endre S�ndor Varga 2008
 *
 */
public class CopyOfPrimitiveAccess {

    private byte byteVariable;

    private short shortVariable;

    private int intVariable;

    private long longVariable;

    private float floatVariable;

    private double doubleVariable;

    private boolean booleanVariable;

    private char charVariable;

    public byte getByteVariable() {
        return byteVariable;
    }

    public void setByteVariable(byte byteVariable) {
        this.byteVariable = byteVariable;
    }

    public short getShortVariable() {
        return shortVariable;
    }

    public void setShortVariable(short shortVariable) {
        this.shortVariable = shortVariable;
    }

    public int getIntVariable() {
        return intVariable;
    }

    public void setIntVariable(int intVariable) {
        FieldWriteDispatcher.dispatchIntWrite(0, intVariable, this.intVariable);
        this.intVariable = intVariable;
    }

    public long getLongVariable() {
        return longVariable;
    }

    public void setLongVariable(long longVariable) {
        this.longVariable = longVariable;
    }

    public float getFloatVariable() {
        return floatVariable;
    }

    public void setFloatVariable(float floatVariable) {
        this.floatVariable = floatVariable;
    }

    public double getDoubleVariable() {
        return doubleVariable;
    }

    public void setDoubleVariable(double doubleVariable) {
        this.doubleVariable = doubleVariable;
    }

    public boolean isBooleanVariable() {
        return booleanVariable;
    }

    public void setBooleanVariable(boolean booleanVariable) {
        this.booleanVariable = booleanVariable;
    }

    public char getCharVariable() {
        return charVariable;
    }

    public void setCharVariable(char charVariable) {
        this.charVariable = charVariable;
    }
}
