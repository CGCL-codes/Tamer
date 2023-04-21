package org.apache.hadoop.util;

/**
 * Interface for sort algorithms accepting {@link IndexedSortable} items.
 *
 * A sort algorithm implementing this interface may only
 * {@link IndexedSortable#compare} and {@link IndexedSortable#swap} items
 * for a range of indices to effect a sort across that range.
 */
public interface IndexedSorter {

    /**
   * Sort the items accessed through the given IndexedSortable over the given
   * range of logical indices. From the perspective of the sort algorithm,
   * each index between l (inclusive) and r (exclusive) is an addressable
   * entry.
   * @see IndexedSortable#compare
   * @see IndexedSortable#swap
   */
    void sort(IndexedSortable s, int l, int r);

    /**
   * Same as {@link #sort(IndexedSortable,int,int)}, but indicate progress
   * periodically.
   * @see #sort(IndexedSortable,int,int)
   */
    void sort(IndexedSortable s, int l, int r, Progressable rep);
}
