package org.hswgt.teachingbox.tools;

import cern.colt.list.DoubleArrayList;
import cern.colt.list.IntArrayList;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.SparseDoubleMatrix1D;
import cern.colt.matrix.linalg.SeqBlas;

public class VectorUtils {

    /**
     * Resizes a vector and copy the old values into the new vector
     * @param v The vector to resize
     * @param size The new size
     * @return return The resized Vector
     */
    public static DoubleMatrix1D resize(DoubleMatrix1D v, int size) {
        if (v.size() == size) return v.like();
        DoubleMatrix1D r = v.like(size);
        if (v.size() > size) r.assign(v.viewPart(0, size)); else r.viewPart(0, v.size()).assign(v);
        return r;
    }

    /**
     * Normalizes the sum of the vector elements to 1
     * @param vector The vector to normalize
     */
    public static void normalise(DoubleMatrix1D vector) {
        double sum = vector.zSum();
        if (sum == 0) return;
        if (vector instanceof SparseDoubleMatrix1D) {
            IntArrayList indexList = new IntArrayList();
            DoubleArrayList valueList = new DoubleArrayList();
            vector.getNonZeros(indexList, valueList);
            for (int i = 0; i < indexList.size(); i++) {
                int indexpos = indexList.getQuick(i);
                double value = valueList.getQuick(i);
                vector.set(indexpos, value / sum);
            }
            return;
        }
        SeqBlas.seqBlas.dscal(1 / sum, vector);
    }

    /**
     * Cut a value at its limits
     * @param vector The value to evaluate
     * @param min The minimal value
     * @param max The maximal value
     * @return <pre>min <= x <= max</pre>
     */
    public static void setLimits(DoubleMatrix1D vector, double min, double max) {
        if (vector instanceof SparseDoubleMatrix1D) {
            IntArrayList indexList = new IntArrayList();
            DoubleArrayList valueList = new DoubleArrayList();
            vector.getNonZeros(indexList, valueList);
            for (int i = 0; i < indexList.size(); i++) {
                int indexpos = indexList.getQuick(i);
                vector.set(indexpos, MathUtils.setLimits(vector.get(indexpos), min, max));
            }
            return;
        } else {
            for (int indexpos = 0; indexpos < vector.size(); indexpos++) vector.set(indexpos, MathUtils.setLimits(vector.get(indexpos), min, max));
        }
    }
}
