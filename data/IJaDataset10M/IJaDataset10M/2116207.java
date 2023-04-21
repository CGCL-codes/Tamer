package org.jbfilter.jsp.converters;

public class ShortConverter extends AbstractConverter<Short> {

    @Override
    public Class<Short> getRegisteredClass() {
        return Short.class;
    }

    @Override
    protected Short fromStringAbstractConverter(String s) {
        return Short.valueOf(s);
    }

    @Override
    protected String toStringAbstractConverter(Short t) {
        return Short.toString(t);
    }
}
