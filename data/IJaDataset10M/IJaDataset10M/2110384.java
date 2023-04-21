package org.melanesia.sql;

/**
 * Range callback.
 *
 * @see org.melanesia.sql.Query#setRangeCallback(RangeCallback)
 *
 * @author marcin.kielar
 */
public interface RangeCallback {

    /**
     * Returns start row of the range to return.
     * Row indices start from 1, not from zero, so the first row is 1.
     *
     * @return start row of the range to return
     */
    long getStartRow();

    /**
     * Returns stop row of the range to return.
     *
     * @return stop row of the range to return
     */
    long getStopRow();

    /**
     * Method called from {@link org.melanesia.sql.Query} during execution, when
     * {@code RangeCallback} has been passed to the {@code Query} object, in
     * order to inform of the total number of rows in the "ranged" result set.
     *
     * @param totalRows total number of rows in result set
     */
    void setTotalRows(long totalRows);
}
