package org.apache.hadoop.io;

import java.util.Comparator;
import org.apache.hadoop.io.serializer.DeserializerComparator;

/**
 * <p>
 * A {@link Comparator} that operates directly on byte representations of
 * objects.
 * </p>
 * @param <T>
 * @see DeserializerComparator
 */
public interface RawComparator<T> extends Comparator<T> {

    public int compare(byte[] b1, int s1, int l1, byte[] b2, int s2, int l2);
}
