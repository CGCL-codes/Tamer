package org.upfrost.converter;

public class NumberConverter implements Converter {

    @Override
    public Object convert(Object from, Class as) {
        Number fromNumber = (Number) from;
        if (as.equals(int.class) || as.equals(Integer.class)) {
            return fromNumber.intValue();
        } else if (as.equals(long.class) || as.equals(Long.class)) {
            return fromNumber.longValue();
        } else if (as.equals(short.class) || as.equals(Short.class)) {
            return fromNumber.shortValue();
        } else if (as.equals(byte.class) || as.equals(Byte.class)) {
            return fromNumber.byteValue();
        } else if (as.equals(float.class) || as.equals(Float.class)) {
            return fromNumber.floatValue();
        } else if (as.equals(double.class) || as.equals(Double.class)) {
            return fromNumber.doubleValue();
        }
        return from;
    }

    @Override
    public boolean handles(Class from, Class as) {
        boolean fromOK = false, asOK = false;
        if (Number.class.isAssignableFrom(from) || byte.class.equals(from) || short.class.equals(from) || int.class.equals(from) || long.class.equals(from) || float.class.equals(from) || double.class.equals(from)) fromOK = true;
        if (Number.class.isAssignableFrom(as) || byte.class.equals(as) || short.class.equals(as) || int.class.equals(as) || long.class.equals(as) || float.class.equals(as) || double.class.equals(as)) asOK = true;
        return fromOK && asOK;
    }
}
