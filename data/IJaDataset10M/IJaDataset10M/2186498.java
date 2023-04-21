package com.rapidminer.example.table;

/**
 * Implementation of DataRow that is backed by primitive arrays. Should always
 * be used if more than 50% of the data is sparse. As fast (or even faster than
 * map implementation) but needs considerably less memory. This implementation uses
 * short arrays instead of double arrays which will reduce the used memory even more.
 * 
 * @author Ingo Mierswa, Shevek
 */
public class ShortSparseArrayDataRow extends AbstractSparseArrayDataRow {

    private static final long serialVersionUID = 1688504268820756726L;

    /** Stores the used attribute values. */
    private short[] values;

    /** Creates an empty sparse array data row with size 0. */
    public ShortSparseArrayDataRow() {
        this(0);
    }

    /** Creates a sparse array data row of the given size. */
    public ShortSparseArrayDataRow(int size) {
        super(size);
        values = new short[size];
    }

    /**
     * Swaps x[a] with x[b].
     */
    @Override
    protected void swapValues(int a, int b) {
        short tt = values[a];
        values[a] = values[b];
        values[b] = tt;
    }

    @Override
    public void resizeValues(int length) {
        short[] d = new short[length];
        System.arraycopy(values, 0, d, 0, Math.min(values.length, length));
        values = d;
    }

    @Override
    public void removeValue(int index) {
        System.arraycopy(values, index + 1, values, index, (values.length - (index + 1)));
    }

    /** Returns the desired data for the given attribute. */
    @Override
    public double getValue(int index) {
        return values[index];
    }

    /** Sets the given data for the given attribute. */
    @Override
    public void setValue(int index, double v) {
        values[index] = (short) v;
    }

    @Override
    protected double[] getAllValues() {
        double[] result = new double[this.values.length];
        for (int i = 0; i < result.length; i++) result[i] = this.values[i];
        return result;
    }

    @Override
    public int getType() {
        return DataRowFactory.TYPE_SHORT_SPARSE_ARRAY;
    }
}
