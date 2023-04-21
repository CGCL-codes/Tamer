package net.disy.ogc.wps.v_1_0_0.converter;

public class ReducedConverter<S, D> implements TypeConverter<S, D> {

    private final Class<D> destinationClass;

    private final TypeConverter<S, ? super D> converter;

    public ReducedConverter(Class<D> destinationClass, TypeConverter<S, ? super D> converter) {
        this.destinationClass = destinationClass;
        this.converter = converter;
    }

    @Override
    public Class<S> getSourceClass() {
        return converter.getSourceClass();
    }

    @Override
    public Class<D> getDestinationClass() {
        return destinationClass;
    }

    @Override
    public D convertTo(S source) {
        Object result = converter.convertTo(source);
        if (result == null) {
            return null;
        }
        if (!destinationClass.isAssignableFrom(result.getClass())) {
            throw new IllegalArgumentException("Object '" + result + "' is no instance of '" + destinationClass + "'.");
        }
        return (D) result;
    }

    @Override
    public S convertFrom(D destination) {
        S result = converter.convertFrom(destination);
        if (result == null) {
            return null;
        }
        return result;
    }
}
