package org.jbfilter.jsp.converters;

public class ByteConverter extends AbstractConverter<Byte> {

    @Override
    public Class<Byte> getRegisteredClass() {
        return Byte.class;
    }

    @Override
    protected Byte fromStringAbstractConverter(String s) {
        return Byte.valueOf(s);
    }

    @Override
    protected String toStringAbstractConverter(Byte t) {
        return Byte.toString(t);
    }
}
