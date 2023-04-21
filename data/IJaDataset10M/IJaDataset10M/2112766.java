package org.aiotrade.math.vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Sparse implement of Vec. It do not store 0 valued elements.
 *
 * This class should keep elements index sorted.
 *
 * @author Caoyuan Deng
 */
public class SparseVec implements Vec {

    private int dimension;

    public static final String ITEM_SEPARATOR = " ";

    private VecItem[] items;

    /**
     * Create a zero items <code>SparseVec</code>.
     */
    public SparseVec() {
        this.items = new VecItem[0];
    }

    /**
     * Create a <code>SparseVec</code> whose items are copied from
     * <code>source</code>.
     * 
     * @param source   the array from which items are copied
     */
    public SparseVec(VecItem[] src) {
        this.items = src;
    }

    /**
     * Create a <code>SparseVec</code> of the desired dimension initialized to zero.
     *
     * @param dimension   the dimension of the new <code>SparseVec</code>
     */
    public SparseVec(int dimension) {
        this.dimension = dimension;
        this.items = new VecItem[0];
    }

    /**
     * Create a <code>SparseVec</code> whose items are copied from
     * <code>src</code>.
     * 
     * @param src   the <code>Vec</code> to be used as src
     */
    public SparseVec(Vec src) {
        copy(src);
    }

    public void setTo(VecItem[] src) {
        this.items = src;
    }

    public void add(double value) {
        assert false : "SparseVec do not support this method, because we should make sure the elements is index sorted";
    }

    public double[] toDoubleArray() {
        double[] values = new double[dimension];
        for (VecItem item : items) {
            values[item.index] = item.value;
        }
        return values;
    }

    public void checkDimensionEquality(Vec comp) {
        if (comp.dimension() != this.dimension()) {
            throw new ArrayIndexOutOfBoundsException("Doing operations with SparseVec instances of different sizes.");
        }
    }

    public SparseVec clone() {
        return new SparseVec(this);
    }

    public double metric(Vec other) {
        return this.minus(other).normTwo();
    }

    public boolean equals(Vec another) {
        if (dimension() != another.dimension()) {
            return false;
        }
        if (another instanceof SparseVec) {
            VecItem[] itemsA = this.items;
            VecItem[] itemsB = ((SparseVec) another).items;
            for (int idxA = 0, idxB = 0, lenA = itemsA.length, lenB = itemsB.length; idxA < lenA && idxB < lenB; ) {
                VecItem itemA = itemsA[idxA];
                VecItem itemB = itemsB[idxB];
                if (itemA.index == itemB.index) {
                    if (itemA.value != itemB.value) {
                        return false;
                    }
                    idxA++;
                    idxB++;
                } else if (itemA.index > itemB.index) {
                    idxB++;
                } else {
                    idxA++;
                }
            }
        } else {
            for (int i = 0, n = dimension(); i < n; i++) {
                if (get(i) != another.get(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    public VecItem getItemByPosition(int position) {
        return items[position];
    }

    public double get(int dimensionIdx) {
        for (VecItem item : items) {
            if (item.index == dimensionIdx) {
                return item.value;
            }
        }
        return 0;
    }

    public void set(int dimensionIdx, double value) {
        VecItem item = getItem(dimensionIdx);
        if (item != null) {
            item.value = value;
        } else {
            VecItem[] newItems = new VecItem[items.length + 1];
            boolean added = false;
            for (int i = 0, n = newItems.length; i < n; i++) {
                if (items[i].index < dimensionIdx) {
                    newItems[i] = items[i];
                } else {
                    if (!added) {
                        newItems[i] = new VecItem(dimensionIdx, value);
                        added = true;
                    } else {
                        newItems[i] = items[i - 1];
                    }
                }
            }
            this.items = newItems;
        }
    }

    public VecItem getItem(int dimensionIdx) {
        for (int i = 0, n = items.length; i < n; i++) {
            if (items[i].index == dimensionIdx) {
                return items[i];
            }
        }
        return null;
    }

    public void setAll(double value) {
        if (value == 0) {
            items = null;
        } else {
            items = new VecItem[dimension];
            for (int i = 0; i < dimension; i++) {
                items[i].index = i;
                items[i].value = value;
            }
        }
    }

    public void copy(Vec src) {
        checkDimensionEquality(src);
        if (src instanceof SparseVec) {
            SparseVec sparseSrc = ((SparseVec) src);
            this.dimension = sparseSrc.dimension;
            VecItem[] srcItems = sparseSrc.items;
            VecItem[] newItems = new VecItem[srcItems.length];
            System.arraycopy(srcItems, 0, newItems, 0, srcItems.length);
        } else {
            List<VecItem> itemList = new ArrayList<VecItem>();
            for (int i = 0, n = src.dimension(); i < n; i++) {
                double value = src.get(i);
                if (value != 0) {
                    itemList.add(new VecItem(i, value));
                }
            }
            items = (VecItem[]) itemList.toArray();
        }
    }

    public void copy(Vec src, int srcPos, int destPos, int length) {
    }

    public void setValues(double[] values) {
        if (dimension() != values.length) {
            throw new ArrayIndexOutOfBoundsException("Doing operations with source of different sizes.");
        }
        VecItem[] newItems = new VecItem[dimension];
        for (int i = 0, n = dimension(); i < n; i++) {
            double value = values[i];
            if (value != 0) {
                newItems[i].index = i;
                newItems[i].value = value;
            }
        }
        items = newItems;
    }

    public int dimension() {
        return dimension;
    }

    public Vec plus(Vec operand) {
        checkDimensionEquality(operand);
        Vec result = new SparseVec(dimension());
        for (int i = 0, n = dimension(); i < n; i++) {
            double value = get(i) + operand.get(i);
            if (value != 0) {
                result.set(i, value);
            }
        }
        return result;
    }

    public Vec minus(Vec operand) {
        checkDimensionEquality(operand);
        Vec result = new SparseVec(dimension());
        for (int i = 0, n = operand.dimension(); i < n; i++) {
            double value = get(i) - operand.get(i);
            if (value != 0) {
                result.set(i, value);
            }
        }
        return result;
    }

    public double innerProduct(Vec operand) {
        checkDimensionEquality(operand);
        double result = 0;
        if (operand instanceof SparseVec) {
            VecItem[] itemsA = this.items;
            VecItem[] itemsB = ((SparseVec) operand).items;
            for (int idxA = 0, idxB = 0, lenA = itemsA.length, lenB = itemsB.length; idxA < lenA && idxB < lenB; ) {
                VecItem itemA = itemsA[idxA];
                VecItem itemB = itemsB[idxB];
                if (itemA.index == itemB.index) {
                    result += itemA.value * itemB.value;
                    idxA++;
                    idxB++;
                } else if (itemA.index > itemB.index) {
                    idxB++;
                } else {
                    idxA++;
                }
            }
        } else {
            for (int i = 0, n = items.length; i < n; i++) {
                VecItem item = items[i];
                result += item.value * operand.get(item.index);
            }
        }
        return result;
    }

    public double square() {
        double result = 0;
        for (int i = 0, n = items.length; i < n; i++) {
            double value = items[i].value;
            result += value * value;
        }
        return result;
    }

    public Vec plus(double operand) {
        SparseVec result = new SparseVec(this);
        for (int i = 0, n = items.length; i < n; i++) {
            result.items[i].value = items[i].value + operand;
        }
        return result;
    }

    public Vec times(double operand) {
        SparseVec result = new SparseVec(this);
        for (int i = 0, n = items.length; i < n; i++) {
            result.items[i].value = items[i].value * operand;
        }
        return result;
    }

    public int getCompactSize() {
        return items.length;
    }

    public VecItem[] getCompactData() {
        return items;
    }

    public double normOne() {
        double result = 0.0;
        for (int i = 0, n = items.length; i < n; i++) {
            result += Math.abs(items[i].value);
        }
        return result;
    }

    public double normTwo() {
        double result = 0.0;
        for (int i = 0, n = items.length; i < n; i++) {
            result += Math.pow(items[i].value, 2);
        }
        result = Math.sqrt(result);
        return result;
    }

    public boolean checkValidation() {
        boolean b = true;
        for (int i = 0, n = items.length; i < n; i++) {
            if (Double.isNaN(items[i].value)) {
                b = false;
                break;
            }
        }
        return b;
    }

    public String toString() {
        String result = "[";
        for (int i = 0, n = dimension(); i < n; i++) {
            result = result + String.valueOf(get(i)) + ITEM_SEPARATOR;
        }
        return result.trim() + "]";
    }

    /**
     * Parses a String into a <code>DefaultVec</code>.
     * Elements are separated by <code>DefaultVec.ITEM_SEPARATOR</code>
     *
     * @param str   the String to parse
     * @return the resulting <code>DefaultVec</code>
     * @see DefaultVec#ITEM_SEPARATOR
     */
    public static Vec parseVec(String str) {
        StringTokenizer st = new StringTokenizer(str, ITEM_SEPARATOR);
        int dimension = st.countTokens();
        Vec result = new DefaultVec(dimension);
        for (int i = 0; i < dimension; i++) {
            result.set(i, Double.parseDouble(st.nextToken()));
        }
        return result;
    }

    public void randomize(double min, double max) {
        Random source = new Random(System.currentTimeMillis() + Runtime.getRuntime().freeMemory());
        for (int i = 0, n = dimension(); i < n; i++) {
            set(i, source.nextDouble() * (max - min) + min);
        }
    }
}
