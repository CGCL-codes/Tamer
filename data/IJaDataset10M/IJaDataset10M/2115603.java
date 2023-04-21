package org.jenetics.util;

/**
 * Adapts an accumulator from type {@code A} to type {@code B}.
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: AccumulatorAdapter.java 727 2010-12-26 17:56:04Z fwilhelm $
 */
public final class AccumulatorAdapter<A, B> extends AdaptableAccumulator<B> {

    private final Accumulator<? super A> _adoptee;

    private final Converter<? super B, ? extends A> _converter;

    /**
	 * Create an new AccumulatorAdapter. 
	 * 
	 * @param adoptee the original, adapted, Accumulator.
	 * @param converter the converter needed to convert from type {@code A} to 
	 *        type {@code B}.
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 */
    public AccumulatorAdapter(final Accumulator<? super A> adoptee, final Converter<? super B, ? extends A> converter) {
        _adoptee = Validator.nonNull(adoptee);
        _converter = Validator.nonNull(converter);
    }

    /**
	 * Return the adapted Accumulator.
	 * 
	 * @return the adapted Accumulator.
	 */
    public Accumulator<? super A> getAccumulator() {
        return _adoptee;
    }

    /**
	 * Return the needed converter from type {@code A} to  type {@code B}.
	 * 
	 * @return the needed converter from type {@code A} to  type {@code B}.
	 */
    public Converter<? super B, ? extends A> getConverter() {
        return _converter;
    }

    @Override
    public void accumulate(final B value) {
        _adoptee.accumulate(_converter.convert(value));
        ++_samples;
    }

    /**
	 * Create an new AccumulatorAdapter. 
	 * 
	 * @param adoptee the original, adapted, Accumulator.
	 * @param converter the converter needed to convert from type {@code A} to 
	 *        type {@code B}.
	 * @throws NullPointerException if one of the arguments is {@code null}.
	 */
    public static <A, B> AccumulatorAdapter<A, B> valueOf(final Accumulator<? super A> adoptee, final Converter<? super B, ? extends A> converter) {
        return new AccumulatorAdapter<A, B>(adoptee, converter);
    }

    @Override
    public String toString() {
        return String.format("%s[a=%s, c=%s]", getClass().getSimpleName(), _adoptee, _converter);
    }
}
