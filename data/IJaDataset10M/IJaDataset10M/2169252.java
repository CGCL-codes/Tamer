package ru.susu.algebra.wrapper.number;

import java.math.BigInteger;
import org.apache.commons.lang.ObjectUtils;

/**
 * @author akargapolov
 * @since: 24.03.2009
 */
public class BigIntegerWrapper implements INumberWrapper<BigInteger> {

    private BigInteger _value;

    public BigIntegerWrapper() {
    }

    public BigIntegerWrapper(BigInteger value) {
        _value = value;
    }

    @Override
    public INumberWrapper<BigInteger> add(INumberWrapper<BigInteger> other) {
        return new BigIntegerWrapper(_value.add(other.getValue()));
    }

    @Override
    public INumberWrapper<BigInteger> divide(INumberWrapper<BigInteger> other) {
        return new BigIntegerWrapper(_value.divide(other.getValue()));
    }

    @Override
    public BigInteger getValue() {
        return _value;
    }

    @Override
    public INumberWrapper<BigInteger> multiply(INumberWrapper<BigInteger> other) {
        return new BigIntegerWrapper(_value.multiply(other.getValue()));
    }

    @Override
    public INumberWrapper<BigInteger> subtract(INumberWrapper<BigInteger> other) {
        return new BigIntegerWrapper(_value.subtract(other.getValue()));
    }

    @Override
    public void setValue(Number value) {
        if (value instanceof BigInteger) _value = (BigInteger) value; else _value = BigInteger.valueOf(value.longValue());
    }

    @Override
    public long remainder(long val) {
        return _value.remainder(BigInteger.valueOf(val)).longValue();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_value == null) ? 0 : _value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        BigIntegerWrapper other = (BigIntegerWrapper) obj;
        return ObjectUtils.equals(_value, other._value);
    }

    @Override
    public INumberWrapper<BigInteger> mod(BigInteger mod) throws Exception {
        return new BigIntegerWrapper(_value.mod(mod));
    }

    @Override
    public String toString() {
        return "BigIntegerWrapper [_value=" + _value + "]";
    }
}
