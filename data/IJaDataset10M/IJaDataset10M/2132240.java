package net.disy.ogc.wps.v_1_0_0.converter;

public class SuperclassingConverter<P, C extends P> implements TypeConverter<C, P> {

    private final Class<P> parentClass;

    private final Class<C> childClass;

    public SuperclassingConverter(Class<P> parentClass, Class<C> childClass) {
        this.parentClass = parentClass;
        this.childClass = childClass;
    }

    @Override
    public P convertTo(C destination) {
        return destination;
    }

    @Override
    public C convertFrom(P source) {
        return (C) source;
    }

    @Override
    public Class<C> getSourceClass() {
        return childClass;
    }

    @Override
    public Class<P> getDestinationClass() {
        return parentClass;
    }
}
