package org.zeroexchange.data;

/**
 * @author black
 *
 */
public interface SlicingDataSet<T> extends DataSet<T> {

    /**
     * Sets the start index.
     */
    void setStart(int start);

    /**
     * Sets the max size of fetching fragment.
     */
    void setCount(int count);
}
