package homura.hde.util.xml.types;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaFloat implements SchemaTypeNumber {

    protected float value;

    protected boolean isempty;

    protected boolean isnull;

    public SchemaFloat() {
        setEmpty();
    }

    public SchemaFloat(SchemaFloat newvalue) {
        value = newvalue.value;
        isempty = newvalue.isempty;
        isnull = newvalue.isnull;
    }

    public SchemaFloat(float newvalue) {
        setValue(newvalue);
    }

    public SchemaFloat(double newvalue) {
        setValue((float) newvalue);
    }

    public SchemaFloat(String newvalue) {
        parse(newvalue);
    }

    public SchemaFloat(SchemaType newvalue) {
        assign(newvalue);
    }

    public SchemaFloat(SchemaTypeNumber newvalue) {
        assign((SchemaType) newvalue);
    }

    public float getValue() {
        return value;
    }

    public void setValue(float newvalue) {
        value = newvalue;
        isempty = false;
        isnull = false;
    }

    public void parse(String s) {
        String newvalue = SchemaNormalizedString.normalize(SchemaNormalizedString.WHITESPACE_COLLAPSE, s);
        if (newvalue == null) setNull(); else if (newvalue.length() == 0) setEmpty(); else {
            try {
                value = Float.parseFloat(newvalue);
                isempty = false;
                isnull = false;
            } catch (NumberFormatException e) {
                throw new StringParseException(e);
            }
        }
    }

    public void assign(SchemaType newvalue) {
        if (newvalue == null || newvalue.isNull()) setNull(); else if (newvalue.isEmpty()) setEmpty(); else if (newvalue instanceof SchemaTypeNumber) {
            value = ((SchemaTypeNumber) newvalue).floatValue();
            isempty = false;
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
        return Float.floatToIntBits(value);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SchemaFloat)) return false;
        return value == ((SchemaFloat) obj).value;
    }

    public Object clone() {
        return new SchemaFloat(this);
    }

    public String toString() {
        if (isempty || isnull) return "";
        String result = Float.toString(value);
        if (result.length() > 2 && result.substring(result.length() - 2, result.length()).equals(".0")) return result.substring(0, result.length() - 2);
        return result;
    }

    public int length() {
        return toString().length();
    }

    public boolean booleanValue() {
        return !(value == 0 || value == Float.NaN);
    }

    public boolean isEmpty() {
        return isempty;
    }

    public boolean isNull() {
        return isnull;
    }

    public int compareTo(Object obj) {
        return compareTo((SchemaFloat) obj);
    }

    public int compareTo(SchemaFloat obj) {
        return Float.compare(value, obj.value);
    }

    public int numericType() {
        return NUMERIC_VALUE_FLOAT;
    }

    public int intValue() {
        return (int) value;
    }

    public long longValue() {
        return (long) value;
    }

    public BigInteger bigIntegerValue() {
        try {
            return new BigInteger(toString());
        } catch (NumberFormatException e) {
            throw new ValuesNotConvertableException(this, new SchemaInteger(0));
        }
    }

    public float floatValue() {
        return value;
    }

    public double doubleValue() {
        return value;
    }

    public BigDecimal bigDecimalValue() {
        return new BigDecimal(value);
    }
}
