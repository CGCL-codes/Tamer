package org.virbo.binarydatasource;

import java.nio.DoubleBuffer;
import org.virbo.dataset.AbstractDataSet;
import org.virbo.dataset.QDataSet;

/**
 * rank 1,2,or 3 dataset backed by double array. 
 * Mutable datasets warning: No dataset should be mutable once it is accessible to the
 * rest of the system.  This would require clients make defensive copies which would 
 * seriously degrade performance.  
 *
 * @author jbf
 */
public final class DoubleBufferDataSet extends AbstractDataSet {

    DoubleBuffer back;

    int rank;

    int len0;

    int len1;

    int len2;

    int reclen0;

    int reclen1;

    int reclen2;

    int recoffs0;

    int recoffs1;

    int recoffs2;

    private static final boolean RANGE_CHECK = true;

    public DoubleBufferDataSet(int rank, int len0, int reclen0, int recoffs0, int len1, int reclen1, int recoffs1, DoubleBuffer back) {
        this.back = back;
        this.rank = rank;
        this.len0 = len0;
        this.len1 = len1;
        this.len2 = len2;
        this.reclen0 = reclen0;
        this.reclen1 = reclen1;
        this.reclen2 = 1;
        this.recoffs0 = 0;
    }

    public int rank() {
        return rank;
    }

    public int length() {
        return len0;
    }

    public int length(int i) {
        return len1;
    }

    public int length(int i0, int i1) {
        return len2;
    }

    public double value(int i0) {
        if (RANGE_CHECK) {
            if (i0 < 0 || i0 >= len0) {
                throw new IndexOutOfBoundsException("i0=" + i0 + " " + this.toString());
            }
        }
        return back.get(offset(i0, 0, 0));
    }

    public double value(int i0, int i1) {
        if (RANGE_CHECK) {
            if (i0 < 0 || i0 >= len0) {
                throw new IndexOutOfBoundsException("i0=" + i0 + " " + this.toString());
            }
            if (i1 < 0 || i1 >= len1) {
                throw new IndexOutOfBoundsException("i1=" + i1 + " " + this.toString());
            }
        }
        return back.get(offset(i0, i1, 0));
    }

    public double value(int i0, int i1, int i2) {
        if (RANGE_CHECK) {
            if (i0 < 0 || i0 >= len0) {
                throw new IndexOutOfBoundsException("i0=" + i0 + " " + this.toString());
            }
            if (i1 < 0 || i1 >= len1) {
                throw new IndexOutOfBoundsException("i1=" + i1 + " " + this.toString());
            }
            if (i2 < 0 || i2 >= len2) {
                throw new IndexOutOfBoundsException("i2=" + i2 + " " + this.toString());
            }
        }
        return back.get(offset(i0, i1, i2));
    }

    private int offset(int i0, int i1, int i2) {
        return i0 * reclen0 * reclen1 * reclen2 + i1 * len2 + i2;
    }
}
