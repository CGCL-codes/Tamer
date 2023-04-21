package com.jmex.xml.types;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaInt implements SchemaTypeNumber {

    protected int value;

    protected boolean isempty;

    protected boolean isnull;

    public SchemaInt() {
        setEmpty();
    }

    public SchemaInt(SchemaInt newvalue) {
        value = newvalue.value;
        isempty = newvalue.isempty;
        isnull = newvalue.isnull;
    }

    public SchemaInt(int newvalue) {
        setValue(newvalue);
    }

    public SchemaInt(String newvalue) {
        parse(newvalue);
    }

    public SchemaInt(SchemaType newvalue) {
        assign(newvalue);
    }

    public SchemaInt(SchemaTypeNumber newvalue) {
        assign((SchemaType) newvalue);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int newvalue) {
        value = newvalue;
        isempty = false;
        isnull = false;
    }

    public void parse(String s) {
        String newvalue = SchemaNormalizedString.normalize(SchemaNormalizedString.WHITESPACE_COLLAPSE, s);
        if (newvalue == null) setNull(); else if (newvalue.length() == 0) setEmpty(); else {
            try {
                value = Integer.parseInt(newvalue);
                isempty = false;
                isnull = false;
            } catch (NumberFormatException e) {
                throw new StringParseException(e);
            }
        }
    }

    public void assign(SchemaType newvalue) {
        if (newvalue == null || newvalue.isNull()) setNull(); else if (newvalue.isEmpty()) setEmpty(); else if (newvalue instanceof SchemaTypeNumber) {
            value = ((SchemaTypeNumber) newvalue).intValue();
            isempty = false;
            isnull = false;
        } else throw new TypesIncompatibleException(newvalue, this);
    }

    public void setNull() {
        isnull = true;
        isempty = true;
        value = 0;
    }

    public void setEmpty() {
        isnull = false;
        isempty = true;
        value = 0;
    }

    public int hashCode() {
        return value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SchemaInt)) return false;
        return value == ((SchemaInt) obj).value;
    }

    public Object clone() {
        return new SchemaInt(this);
    }

    public String toString() {
        if (isempty || isnull) return "";
        return Integer.toString(value);
    }

    public int length() {
        return toString().length();
    }

    public boolean booleanValue() {
        return value != 0;
    }

    public boolean isEmpty() {
        return isempty;
    }

    public boolean isNull() {
        return isnull;
    }

    public int compareTo(Object obj) {
        return compareTo((SchemaInt) obj);
    }

    public int compareTo(SchemaInt obj) {
        return new Integer(value).compareTo(new Integer(obj.value));
    }

    public int numericType() {
        return NUMERIC_VALUE_INT;
    }

    public int intValue() {
        return value;
    }

    public long longValue() {
        return value;
    }

    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(value);
    }

    public float floatValue() {
        return value;
    }

    public double doubleValue() {
        return value;
    }

    public BigDecimal bigDecimalValue() {
        return BigDecimal.valueOf(value);
    }
}
