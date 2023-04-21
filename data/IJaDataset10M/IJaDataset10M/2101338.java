package org.nodal.model;

/**
 * A subclass of Seq for accessing sequences of shorts.
 * @author leei
 */
public interface ShortSeq extends IntSeq {

    /**
   * Access the unboxed short at index i in the sequence.
   * @param i the index
   * @return the value at that index
   */
    short shortAt(int i);

    /**
   * Returns a copy of the short array equivalent to this Seq
   * @return the array of shorts 
   */
    short[] asShortArray();
}

;
