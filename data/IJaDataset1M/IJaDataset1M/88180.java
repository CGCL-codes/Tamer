package org.jenetics.util;

/**
 * This class allows to build transitive converters.
 * 
 * @author <a href="mailto:franz.wilhelmstoetter@gmx.at">Franz Wilhelmstötter</a>
 * @version $Id: CompositeConverter.java 719 2010-12-22 08:10:40Z fwilhelm $
 */
public class CompositeConverter<A, B, C> implements Converter<A, C> {

    private final Converter<A, B> _first;

    private final Converter<B, C> _second;

    /**
	 * Create a new transitive converter with the given converters.
	 * 
	 * @param first first converter
	 * @param second second converter
	 * @throws NullPointerException if one of the converters is {@code null}.
	 */
    public CompositeConverter(final Converter<A, B> first, final Converter<B, C> second) {
        _first = Validator.nonNull(first);
        _second = Validator.nonNull(second);
    }

    @Override
    public C convert(final A value) {
        return _second.convert(_first.convert(value));
    }

    @Override
    public String toString() {
        return String.format("%s[%s, %s]", getClass().getSimpleName(), _first, _second);
    }

    public static <A, B, C> Converter<A, C> valueOf(final Converter<A, B> c1, final Converter<B, C> c2) {
        return new CompositeConverter<A, B, C>(c1, c2);
    }

    public static <A, B, C, D> Converter<A, D> valueOf(final Converter<A, B> c1, final Converter<B, C> c2, final Converter<C, D> c3) {
        return valueOf(valueOf(c1, c2), c3);
    }

    public static <A, B, C, D, E> Converter<A, E> valueOf(final Converter<A, B> c1, final Converter<B, C> c2, final Converter<C, D> c3, final Converter<D, E> c4) {
        return valueOf(valueOf(valueOf(c1, c2), c3), c4);
    }

    public static <A, B, C, D, E, F> Converter<A, F> valueOf(final Converter<A, B> c1, final Converter<B, C> c2, final Converter<C, D> c3, final Converter<D, E> c4, final Converter<E, F> c5) {
        return valueOf(valueOf(valueOf(valueOf(c1, c2), c3), c4), c5);
    }
}
