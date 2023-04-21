package org.jbfilter.jsp.converters;

@SuppressWarnings("rawtypes")
public class EnumConverter extends AbstractConverter<Enum> {

    private Class<Enum> enumClass;

    @SuppressWarnings("unchecked")
    public EnumConverter(String classForName) throws ClassNotFoundException {
        enumClass = (Class<Enum>) Class.forName(classForName);
        if (!enumClass.isEnum()) {
            throw new IllegalArgumentException(String.format("'%s' is not an enum.", classForName));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Enum fromStringAbstractConverter(String s) {
        return Enum.valueOf(enumClass, s);
    }

    @Override
    protected String toStringAbstractConverter(Enum t) {
        return t.name();
    }

    @Override
    public Class<Enum> getRegisteredClass() {
        throw new UnsupportedOperationException("Stateful object : cannot be reused.");
    }
}
