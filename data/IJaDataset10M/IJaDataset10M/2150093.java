package net.sourceforge.nconfigurations.convert;

/**
 * A converter implementation for translation of strings to {@code short}s and
 * {@code short}s to string for a particular radix using {@link
 * Short#valueOf(String, int)} and {@link Integer#toString(int, int)}
 * implementations.  All restrictions and side-effects of these implementations
 * apply to the conversion function provided by this converter.
 *
 * <p>Instances of this class are immutable and inherently thread-safe without
 * the need for external synchronization.
 *
 * @author Petr Novotník
 * @since 1.0
 */
class ShortConverter implements Converter<Short> {

    /**
     * A convenience converter for short values with a radix of {@code 10}.
     */
    static ShortConverter RADIX10_INSTANCE = new ShortConverter(10);

    /**
     * Constructs a new converter for short values with the specified radix.
     * Note that for converters with a radix {@code 10} you should use the
     * {@link #RADIX10_INSTANCE} object to avoid unnecessary object allocation.
     *
     * @param radix the radix to be used for decoding and decoding short values
     *
     * @throws IllegalArgumentException if {@code radix} is {@code < 2}
     */
    ShortConverter(int radix) {
        RadixHelper.checkNotSmallerThan(radix, 2);
        this.radix = radix;
    }

    /**
     * {@inheritDoc}
     */
    public String valueToString(final Short x) throws ConvertException {
        return Integer.toString(x.intValue(), radix);
    }

    /**
     * {@inheritDoc}
     */
    public Short stringToValue(final String s) throws ConvertException {
        if (s == null) {
            throw new NullPointerException();
        }
        try {
            return Short.valueOf(s, radix);
        } catch (NumberFormatException e) {
            throw new ConvertException(e);
        }
    }

    private final int radix;
}
