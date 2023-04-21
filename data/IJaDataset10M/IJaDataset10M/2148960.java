package org.virbo.cdf;

import org.virbo.dataset.AbstractDataSet;
import org.virbo.dataset.DataSetOps;
import org.virbo.dataset.DataSetUtil;
import org.virbo.dataset.QDataSet;

/**
 * QDataSet implementation that appends datasets together to make them
 * look like one long dataset.
 * Each dataset must have the same qube.  This is for efficiency.
 *
 * limitations:
 *   doesn't check DEPEND_1 when checking qube.
 * @author jbf
 */
public class SegmentedBufferDataSet extends AbstractDataSet {

    private final int rank;

    private final int[] segStart;

    private final int[] segStop;

    private int currentSegment = 0;

    private QDataSet[] segments;

    protected SegmentedBufferDataSet(int rank, QDataSet[] segments) {
        this.rank = rank;
        this.segStart = new int[segments.length];
        this.segStop = new int[segments.length];
        this.segStart[0] = 0;
        int[] tqube = null;
        for (int i = 0; i < segments.length; i++) {
            if (i == 0) {
                tqube = DataSetUtil.qubeDims(segments[i]);
            } else {
                this.segStart[i] = this.segStart[i - 1];
                if (tqube != null) {
                    int[] qube = DataSetUtil.qubeDims(segments[i]);
                    if (qube == null) {
                        tqube = null;
                    } else {
                        for (int j = 1; j < qube.length; j++) {
                            if (qube[j] != tqube[j]) {
                                tqube = null;
                                break;
                            }
                        }
                    }
                }
            }
            this.segStop[i] = this.segStart[i] + segments[i].length();
        }
        if (tqube != null) {
            putProperty(QDataSet.QUBE, Boolean.TRUE);
        }
    }

    @Override
    public int rank() {
        return rank;
    }

    /**
     * return the current segment.  We keep track of the current segment so
     * we needn't search with each access.
     * @param idx0
     * @return
     */
    protected synchronized int segmentFor(int idx0) {
        if (segStart[currentSegment] <= idx0 && segStop[currentSegment] < idx0) {
            return currentSegment;
        } else {
            while (currentSegment < segStart.length && idx0 >= segStop[currentSegment]) {
                currentSegment++;
            }
            if (currentSegment == segStart.length) {
                currentSegment = 0;
                throw new IndexOutOfBoundsException(String.format("%d is too high", idx0));
            }
            while (currentSegment > 0 && idx0 < segStart[currentSegment]) {
                currentSegment--;
            }
            if (currentSegment < 0) {
                currentSegment = 0;
                throw new IndexOutOfBoundsException(String.format("%d is too high", idx0));
            }
            return currentSegment;
        }
    }

    @Override
    public int length(int idx0) {
        final int ids = segmentFor(idx0);
        final int i = idx0 - segStart[ids];
        return segments[ids].length(i);
    }

    @Override
    public int length(int idx0, int j) {
        int ids = segmentFor(idx0);
        int i = idx0 - segStart[ids];
        return segments[ids].length(i, j);
    }

    @Override
    public int length(int idx0, int j, int k) {
        final int ids = segmentFor(idx0);
        final int i = idx0 - segStart[ids];
        return segments[ids].length(i, j, k);
    }

    @Override
    public QDataSet slice(int idx0) {
        final int ids = segmentFor(idx0);
        final int i = idx0 - segStart[ids];
        return segments[ids].slice(i);
    }

    @Override
    public QDataSet trim(int start, int stop) {
        final int ids0 = segmentFor(start);
        final int ids1 = segmentFor(stop);
        if (ids0 == ids1) {
            int off = segStart[ids0];
            return segments[ids0].trim(start - off, stop - off);
        } else {
            return DataSetOps.trim(this, start, stop);
        }
    }

    @Override
    public double value(int i0) {
        final int ids = segmentFor(i0);
        return segments[ids].value(i0 - segStart[ids]);
    }

    @Override
    public double value(int i0, int i1) {
        final int ids = segmentFor(i0);
        return segments[ids].value(i0 - segStart[ids], i1);
    }

    @Override
    public double value(int i0, int i1, int i2) {
        final int ids = segmentFor(i0);
        return segments[ids].value(i0 - segStart[ids], i1, i2);
    }

    @Override
    public double value(int i0, int i1, int i2, int i3) {
        final int ids = segmentFor(i0);
        return segments[ids].value(i0 - segStart[ids], i1, i2, i3);
    }
}
