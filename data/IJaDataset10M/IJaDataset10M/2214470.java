package net.disy.ogc.wps.v_1_0_0.converter;

public class SubclassingConverter<P, C extends P> implements TypeConverter<P, C> {

    private final Class<P> parentClass;

    private final Class<C> childClass;

    public SubclassingConverter(Class<P> parentClass, Class<C> childClass) {
        this.parentClass = parentClass;
        this.childClass = childClass;
    }

    @Override
    public P convertFrom(C destination) {
        return destination;
    }

    @Override
    public C convertTo(P source) {
        return (C) source;
    }

    @Override
    public Class<C> getDestinationClass() {
        return childClass;
    }

    @Override
    public Class<P> getSourceClass() {
        return parentClass;
    }
}
