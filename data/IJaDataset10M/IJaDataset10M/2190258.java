package org.javaseis.parallel;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements an iteration over all of the frames in a distributed array,
 * including the 3D, 4D, and 5D case.  This class uses generics, but currently only
 * float[][] and int[][] types are supported, although others can easily be added.
 */
public class DistributedArrayFrameIterator<E> implements Iterator<E> {

    private static final boolean c_tolerateFrameOutOfBounds = false;

    private static final Logger LOG = Logger.getLogger("org.javaseis.parallel");

    private DistributedArray _distributedArray;

    private E _traces;

    private int[] _framePosition;

    private int _ndim;

    private int _nHypercubes;

    private int _nVolumes;

    private int _nFrames;

    private int _hypercubeIndex = 0;

    private int _volumeIndex = 0;

    private int _frameIndex = 0;

    /**
   * Creates a new distributed array frame iterator.
   *
   * @param  distributedArray  the distributed array to iterate over.
   * @param  traces  the buffer to use to return the values from the next() method.
   */
    public DistributedArrayFrameIterator(DistributedArray distributedArray, E traces) {
        this(distributedArray, distributedArray.getShape()[2], traces);
    }

    /**
   * Creates a new distributed array frame iterator.
   *
   * @param  distributedArray  the distributed array to iterate over.
   * @param  activeFrameCount  the number of global active frames (== getShape()[2] unless discarding padding)
   * @param  traces  the buffer to use to return the values from the next() method.
   */
    public DistributedArrayFrameIterator(DistributedArray distributedArray, int activeFrameCount, E traces) {
        if (distributedArray == null) throw new IllegalArgumentException("distributedArray is null");
        if (traces == null) throw new IllegalArgumentException("traces is null");
        _distributedArray = distributedArray;
        _traces = traces;
        _ndim = distributedArray.getDimensions();
        _nHypercubes = 1;
        if (_ndim > 4) _nHypercubes = distributedArray.getLocalLength(4);
        _nVolumes = 1;
        if (_ndim > 3) _nVolumes = distributedArray.getLocalLength(3);
        int rank = distributedArray.getParallelContext().rank();
        int size = distributedArray.getParallelContext().size();
        int localLength = distributedArray.getLocalLength(2);
        _nFrames = localLength;
        if (_nFrames > 0) {
            int nFramesGlobalPadding = distributedArray.getShape()[2] - activeFrameCount;
            int nTasksInvolved = nFramesGlobalPadding / localLength;
            int testRank = size - nTasksInvolved;
            if (rank >= testRank) {
                _nFrames = 0;
            } else if (rank >= testRank - 1) {
                _nFrames = (nTasksInvolved + 1) * localLength - nFramesGlobalPadding;
            }
        }
        int nDimensions = _distributedArray.getDimensions();
        _framePosition = new int[nDimensions];
    }

    public boolean hasNext() {
        if (_nFrames <= 0) return false;
        if (_hypercubeIndex >= _nHypercubes) {
            return false;
        } else {
            return true;
        }
    }

    /**
   * Returns the next frame of values, always in the array space that was provided to the
   * constructor (not a new float[][] or int[][] array each time).
   *
   * @return  the next frame of values.
   */
    public E next() {
        if (!this.hasNext()) throw new IllegalStateException("Attempt to use next() after hasNext() returns false");
        RuntimeException re = null;
        try {
            this.getData();
        } catch (RuntimeException e) {
            re = e;
        }
        _frameIndex++;
        if (_frameIndex >= _nFrames) {
            _frameIndex = 0;
            _volumeIndex++;
        }
        if (_volumeIndex >= _nVolumes) {
            _frameIndex = 0;
            _volumeIndex = 0;
            _hypercubeIndex++;
        }
        if (re != null) throw re;
        return _traces;
    }

    private void getData() throws RuntimeException {
        if (_ndim > 4) _framePosition[4] = _distributedArray.localToGlobal(4, _hypercubeIndex);
        if (_ndim > 3) _framePosition[3] = _distributedArray.localToGlobal(3, _volumeIndex);
        _framePosition[2] = _distributedArray.localToGlobal(2, _frameIndex);
        _framePosition[0] = 0;
        try {
            if (_traces instanceof float[][]) {
                _distributedArray.getFrame((float[][]) _traces, _framePosition);
            } else if (_traces instanceof int[][]) {
                _distributedArray.getFrame((int[][]) _traces, _framePosition);
            } else {
                throw new RuntimeException("Data type not recognized");
            }
        } catch (Throwable t) {
            t.printStackTrace();
            this.handleFrameOutOfBounds(_framePosition, _distributedArray);
        }
    }

    /**
   * Returns the global position of the iterator without advancing to the next frame.
   * This is useful to determine which frame (and perhaps which volume) was just returned
   * by the next() method.  The returned array is a shallow copy,
   * so changes to its values would affect the behavior of the interator.
   *
   * @return  the global position of the iterator.
   */
    public int[] getPosition() {
        return _framePosition;
    }

    /**
   * Puts data back into the distributed array(s) exactly where the previous next() fetched
   * it from, without advancing to the next frame.
   *
   * @param  traces  the data to put back into the distributed array(s).
   */
    public void putData(E traces) throws RuntimeException {
        try {
            if (traces instanceof float[][]) {
                _distributedArray.putFrame((float[][]) traces, _framePosition);
            } else if (traces instanceof int[][]) {
                _distributedArray.putFrame((int[][]) traces, _framePosition);
            } else {
                throw new RuntimeException("Data type not recognized");
            }
        } catch (Throwable t) {
            t.printStackTrace();
            this.handleFrameOutOfBounds(_framePosition, _distributedArray);
        }
    }

    public void remove() {
        throw new RuntimeException("Optional method Iterator.remove() is not implemented");
    }

    private void handleFrameOutOfBounds(int[] framePosition, DistributedArray distributedArray) throws RuntimeException {
        if (LOG.isLoggable(Level.FINE)) {
            distributedArray.printContents();
        }
        if (c_tolerateFrameOutOfBounds) return;
        String s = "";
        for (int i = 0; i < framePosition.length; i++) s += " " + framePosition[i];
        throw new RuntimeException("Out-of-bounds frame encountered at frame position " + s);
    }
}
