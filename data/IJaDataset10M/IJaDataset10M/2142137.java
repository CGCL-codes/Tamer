package net.disy.ogc.wps.v_1_0_0.converter;

public interface TypeConverterRegistry {

    public <S, D> D convertTo(Class<S> sourceClass, Class<D> destinationClass, S object);

    public <S, D> S convertFrom(Class<S> sourceClass, Class<D> destinationClass, D object);

    public boolean isConversionSupported(Class<?> sourceClass, Class<?> destinationClass);
}
