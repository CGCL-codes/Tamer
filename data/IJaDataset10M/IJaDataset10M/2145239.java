package com.tomgibara.crinch.hashing;

/**
 * Utility methods for working with objects in this package.
 * 
 * @author tomgibara
 *
 */
public class Hashes {

    private Hashes() {
    }

    /**
	 * Returns a {@link MultiHash} implementation that returns hashes in a
	 * specified range, based on the hash values produced by another
	 * {@link MultiHash} implementation. If the range is being widened, there is
	 * no guarantee that every value in the new range will be used. If the new
	 * range is equal to that of the supplied {@link MultiHash}, the original
	 * hash will be returned, unmodified. To use this method with a plain
	 * {@link Hash}, first pass it to {@link #asMultiHash(Hash)}.
	 * 
	 * @param <T>
	 *            the type of objects for which hashes may be generated
	 * @param newRange
	 *            the range to which generated hash values should be constrained
	 * @param multiHash
	 *            supplies the hash values
	 * @return a {@link MultiHash} that returns values within the specified
	 *         range
	 * @throws IllegalArgumentException
	 *             if any of the arguments to the method is null
	 */
    public static <T> MultiHash<T> rangeAdjust(HashRange newRange, MultiHash<T> multiHash) throws IllegalArgumentException {
        if (newRange == null) throw new IllegalArgumentException("null newRange");
        if (multiHash == null) throw new IllegalArgumentException("null multiHash");
        final HashRange oldRange = multiHash.getRange();
        if (oldRange.equals(newRange)) return multiHash;
        if (newRange.isIntBounded() && newRange.isIntSized() && oldRange.isIntBounded() && oldRange.isIntSized()) return new IntRerangedHash<T>(multiHash, newRange);
        if (newRange.isLongBounded() && newRange.isLongSized() && oldRange.isLongBounded() && oldRange.isLongSized()) return new LongRerangedHash<T>(multiHash, newRange);
        return new BigIntRerangedHash<T>(multiHash, newRange);
    }

    /**
	 * Adapts a {@link Hash} implementation into a {@link MultiHash}
	 * implementation. If the supplied object already implements the
	 * {@link MultiHash} interface, the original object is returned, otherwise,
	 * a new object will be created that returns the same hash values through
	 * the {@link MultiHash} interface.
	 * 
	 * @param <T>
	 *            the type of objects for which hashes may be generated
	 * @param hash
	 *            the {@link Hash} implementation for which a {@link MultiHash}
	 *            is needed
	 * @return a {@link MultiHash} implementation that returns the hash values
	 *         from the supplied {@link Hash}
	 * @throws IllegalArgumentException
	 *             if the supplied hash is null
	 */
    public static <T> MultiHash<T> asMultiHash(Hash<T> hash) {
        if (hash == null) throw new IllegalArgumentException("null hash");
        if (hash instanceof MultiHash<?>) return (MultiHash<T>) hash;
        return new SingletonMultiHash<T>(hash);
    }

    public static int hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

    public static int hashCode(byte value) {
        return value;
    }

    public static int hashCode(short value) {
        return value;
    }

    public static int hashCode(char value) {
        return value;
    }

    public static int hashCode(int value) {
        return value;
    }

    public static int hashCode(long value) {
        return (int) (value ^ (value >>> 32));
    }

    public static int hashCode(float value) {
        return Float.floatToIntBits(value);
    }

    public static int hashCode(double value) {
        long bits = Double.doubleToLongBits(value);
        return (int) (bits ^ (bits >>> 32));
    }
}
