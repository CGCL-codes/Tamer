package ma.glasnost.orika.converter;

/**
 * 
 * 
 * @author 
 *
 * @param <S>
 * @param <D>
 * 
 * @deprecated use {@link ma.glasnost.orika.converter.BidirectionalConverter} instead
 */
@Deprecated
public abstract class BidirectionConverter<S, D> extends TypeConverter<Object, Object> implements Converter<Object, Object> {

    public abstract D convertTo(S source, Class<D> destinationClass);

    public abstract S convertFrom(D source, Class<S> destinationClass);

    @SuppressWarnings("unchecked")
    public Object convert(Object source, Class<? extends Object> destinationClass) {
        if (destinationClass.equals(this.destinationClass)) {
            return convertTo((S) source, (Class<D>) destinationClass);
        } else {
            return convertFrom((D) source, (Class<S>) destinationClass);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean canConvert(Class<Object> sourceClass, Class<? extends Object> destinationClass) {
        return super.canConvert(sourceClass, destinationClass) || super.canConvert((Class<Object>) destinationClass, sourceClass);
    }
}
