package org.exist.xquery.value;

import java.math.BigDecimal;
import java.text.Collator;
import org.exist.util.FastStringBuffer;
import org.exist.util.FloatingPointConverter;
import org.exist.xquery.Constants;
import org.exist.xquery.XPathException;

public class DoubleValue extends NumericValue {

    public static final DoubleValue ZERO = new DoubleValue(0.0E0);

    public static final DoubleValue POSITIVE_INFINITY = new DoubleValue(Double.POSITIVE_INFINITY);

    public static final DoubleValue NEGATIVE_INFINITY = new DoubleValue(Double.NEGATIVE_INFINITY);

    public static final DoubleValue NaN = new DoubleValue(Double.NaN);

    private double value;

    public DoubleValue(double value) {
        this.value = value;
    }

    public DoubleValue(AtomicValue otherValue) throws XPathException {
        this(otherValue.getStringValue());
    }

    public DoubleValue(String stringValue) throws XPathException {
        try {
            if (stringValue.equals("INF")) value = Double.POSITIVE_INFINITY; else if (stringValue.equals("-INF")) value = Double.NEGATIVE_INFINITY; else if (stringValue.equals("NaN")) value = Double.NaN; else value = Double.parseDouble(stringValue);
        } catch (NumberFormatException e) {
            throw new XPathException("FORG0001: cannot construct " + Type.getTypeName(this.getItemType()) + " from '" + stringValue + "'");
        }
    }

    public int getType() {
        return Type.DOUBLE;
    }

    public String getStringValue() {
        FastStringBuffer sb = new FastStringBuffer(20);
        FloatingPointConverter.appendDouble(sb, value).getNormalizedString(0);
        return sb.toString();
    }

    public double getValue() {
        return value;
    }

    public boolean hasFractionalPart() {
        if (isNaN()) return false;
        if (isInfinite()) return false;
        return new DecimalValue(new BigDecimal(value)).hasFractionalPart();
    }

    ;

    public Item itemAt(int pos) {
        return pos == 0 ? this : null;
    }

    public boolean isNaN() {
        return Double.isNaN(value);
    }

    public boolean isInfinite() {
        return Double.isInfinite(value);
    }

    public boolean isZero() {
        return Double.compare(Math.abs(value), 0.0) == Constants.EQUAL;
    }

    public boolean isNegative() {
        return (Double.compare(value, 0.0) < Constants.EQUAL);
    }

    public boolean isPositive() {
        return (Double.compare(value, 0.0) > Constants.EQUAL);
    }

    public AtomicValue convertTo(int requiredType) throws XPathException {
        switch(requiredType) {
            case Type.ATOMIC:
            case Type.ITEM:
            case Type.NUMBER:
            case Type.DOUBLE:
                return this;
            case Type.FLOAT:
                return new FloatValue(new Float(value).floatValue());
            case Type.UNTYPED_ATOMIC:
                return new UntypedAtomicValue(getStringValue());
            case Type.STRING:
                return new StringValue(getStringValue());
            case Type.DECIMAL:
                if (isNaN()) throw new XPathException("FORG0001: can not convert " + Type.getTypeName(getType()) + "('" + getStringValue() + "') to " + Type.getTypeName(requiredType));
                if (isInfinite()) throw new XPathException("FORG0001: can not convert " + Type.getTypeName(getType()) + "('" + getStringValue() + "') to " + Type.getTypeName(requiredType));
                return new DecimalValue(new BigDecimal(value));
            case Type.INTEGER:
            case Type.NON_POSITIVE_INTEGER:
            case Type.NEGATIVE_INTEGER:
            case Type.LONG:
            case Type.INT:
            case Type.SHORT:
            case Type.BYTE:
            case Type.NON_NEGATIVE_INTEGER:
            case Type.UNSIGNED_LONG:
            case Type.UNSIGNED_INT:
            case Type.UNSIGNED_SHORT:
            case Type.UNSIGNED_BYTE:
            case Type.POSITIVE_INTEGER:
                if (isNaN()) throw new XPathException("FORG0001: can not convert " + Type.getTypeName(getType()) + "('" + getStringValue() + "') to " + Type.getTypeName(requiredType));
                if (Double.isInfinite(value)) throw new XPathException("FORG0001: can not convert " + Type.getTypeName(getType()) + "('" + getStringValue() + "') to " + Type.getTypeName(requiredType));
                if (value > Integer.MAX_VALUE) throw new XPathException("err:FOCA0003: Value is out of range for type xs:integer");
                return new IntegerValue((long) value, requiredType);
            case Type.BOOLEAN:
                return new BooleanValue(this.effectiveBooleanValue());
            default:
                throw new XPathException("FORG0001: cannot cast '" + Type.getTypeName(this.getItemType()) + "(\"" + getStringValue() + "\")' to " + Type.getTypeName(requiredType));
        }
    }

    public double getDouble() throws XPathException {
        return value;
    }

    public int getInt() throws XPathException {
        return (int) Math.round(value);
    }

    public long getLong() throws XPathException {
        return (long) Math.round(value);
    }

    public void setValue(double val) {
        value = val;
    }

    public NumericValue ceiling() throws XPathException {
        return new DoubleValue(Math.ceil(value));
    }

    public NumericValue floor() throws XPathException {
        return new DoubleValue(Math.floor(value));
    }

    public NumericValue round() throws XPathException {
        if (Double.isNaN(value)) {
            return this;
        }
        if (Double.isInfinite(value)) {
            return this;
        }
        if (value == 0.0) {
            return this;
        }
        if (value > -0.5 && value < 0.0) {
            return new DoubleValue(-0.0);
        }
        if (value > Long.MIN_VALUE && value < Long.MAX_VALUE) {
            return new DoubleValue(Math.round(value));
        }
        return this;
    }

    public NumericValue round(IntegerValue precision) throws XPathException {
        return (DoubleValue) ((DecimalValue) convertTo(Type.DECIMAL)).round(precision).convertTo(Type.DOUBLE);
    }

    public ComputableValue minus(ComputableValue other) throws XPathException {
        if (Type.subTypeOf(other.getType(), Type.DOUBLE)) return new DoubleValue(value - ((DoubleValue) other).value); else return minus((ComputableValue) other.convertTo(getType()));
    }

    public ComputableValue plus(ComputableValue other) throws XPathException {
        if (Type.subTypeOf(other.getType(), Type.DOUBLE)) return new DoubleValue(value + ((DoubleValue) other).value); else return plus((ComputableValue) other.convertTo(getType()));
    }

    public ComputableValue mult(ComputableValue other) throws XPathException {
        switch(other.getType()) {
            case Type.DOUBLE:
                return new DoubleValue(value * ((DoubleValue) other).value);
            case Type.DAY_TIME_DURATION:
            case Type.YEAR_MONTH_DURATION:
                return other.mult(this);
            default:
                return mult((ComputableValue) other.convertTo(getType()));
        }
    }

    public ComputableValue div(ComputableValue other) throws XPathException {
        if (Type.subTypeOf(other.getType(), Type.NUMBER)) {
            if (this.isZero() && ((NumericValue) other).isZero()) return NaN;
            if (this.isNegative() && ((NumericValue) other).isZero() && ((NumericValue) other).isPositive()) return NEGATIVE_INFINITY;
            if (this.isNegative() && ((NumericValue) other).isZero() && ((NumericValue) other).isNegative()) return POSITIVE_INFINITY;
            if (this.isPositive() && ((NumericValue) other).isZero() && ((NumericValue) other).isNegative()) return NEGATIVE_INFINITY;
            if (this.isPositive() && ((NumericValue) other).isZero() && ((NumericValue) other).isPositive()) return POSITIVE_INFINITY;
            if (this.isInfinite() && ((NumericValue) other).isInfinite()) return NaN;
        }
        if (Type.subTypeOf(other.getType(), Type.DOUBLE)) return new DoubleValue(value / ((DoubleValue) other).value); else return div((ComputableValue) other.convertTo(getType()));
    }

    public IntegerValue idiv(NumericValue other) throws XPathException {
        ComputableValue result = div(other);
        return new IntegerValue(((IntegerValue) result.convertTo(Type.INTEGER)).getLong());
    }

    public NumericValue mod(NumericValue other) throws XPathException {
        if (Type.subTypeOf(other.getType(), Type.DOUBLE)) return new DoubleValue(value % ((DoubleValue) other).value); else return mod((NumericValue) other.convertTo(getType()));
    }

    public NumericValue negate() throws XPathException {
        return new DoubleValue(-value);
    }

    public NumericValue abs() throws XPathException {
        return new DoubleValue(Math.abs(value));
    }

    public AtomicValue max(Collator collator, AtomicValue other) throws XPathException {
        if (Type.subTypeOf(other.getType(), Type.DOUBLE)) return new DoubleValue(Math.max(value, ((DoubleValue) other).value)); else return new DoubleValue(Math.max(value, ((DoubleValue) other.convertTo(Type.DOUBLE)).value));
    }

    public AtomicValue min(Collator collator, AtomicValue other) throws XPathException {
        if (Type.subTypeOf(other.getType(), Type.DOUBLE)) return new DoubleValue(Math.min(value, ((DoubleValue) other).value)); else return new DoubleValue(Math.min(value, ((DoubleValue) other.convertTo(Type.DOUBLE)).value));
    }

    public int conversionPreference(Class<?> javaClass) {
        if (javaClass.isAssignableFrom(DoubleValue.class)) return 0;
        if (javaClass == Long.class || javaClass == long.class) return 3;
        if (javaClass == Integer.class || javaClass == int.class) return 4;
        if (javaClass == Short.class || javaClass == short.class) return 5;
        if (javaClass == Byte.class || javaClass == byte.class) return 6;
        if (javaClass == Double.class || javaClass == double.class) return 1;
        if (javaClass == Float.class || javaClass == float.class) return 2;
        if (javaClass == String.class) return 7;
        if (javaClass == Boolean.class || javaClass == boolean.class) return 8;
        if (javaClass == Object.class) return 20;
        return Integer.MAX_VALUE;
    }

    public Object toJavaObject(Class<?> target) throws XPathException {
        if (target.isAssignableFrom(DoubleValue.class)) return this; else if (target == Double.class || target == double.class) return new Double(value); else if (target == Float.class || target == float.class) return new Float(value); else if (target == Long.class || target == long.class) {
            return Long.valueOf(((IntegerValue) convertTo(Type.LONG)).getValue());
        } else if (target == Integer.class || target == int.class) {
            IntegerValue v = (IntegerValue) convertTo(Type.INT);
            return Integer.valueOf((int) v.getValue());
        } else if (target == Short.class || target == short.class) {
            IntegerValue v = (IntegerValue) convertTo(Type.SHORT);
            return Short.valueOf((short) v.getValue());
        } else if (target == Byte.class || target == byte.class) {
            IntegerValue v = (IntegerValue) convertTo(Type.BYTE);
            return Byte.valueOf((byte) v.getValue());
        } else if (target == String.class) return getStringValue(); else if (target == Boolean.class) return Boolean.valueOf(effectiveBooleanValue());
        throw new XPathException("cannot convert value of type " + Type.getTypeName(getType()) + " to Java object of type " + target.getName());
    }

    /** size writen by {link #serialize(short, boolean)} */
    public int getSerializedSize() {
        return 1 + 8;
    }

    public int compareTo(Object o) {
        final AtomicValue other = (AtomicValue) o;
        if (Type.subTypeOf(other.getType(), Type.DOUBLE)) return Double.compare(value, ((DoubleValue) other).value); else return getType() < other.getType() ? Constants.INFERIOR : Constants.SUPERIOR;
    }
}
