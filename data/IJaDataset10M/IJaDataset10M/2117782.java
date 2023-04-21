package org.javaseis.array;

import java.util.Iterator;

public class MultiArrayVolumeIterator implements Iterable, Iterator {

    private IMultiArray _a;

    private int _ndim;

    private int _nvolumes;

    private int _ivolume;

    private int _incr;

    private int[] _position, _shape, _volumeMultiplier;

    private boolean _forward = true;

    public MultiArrayVolumeIterator(IMultiArray a) {
        constructIterator(a, true);
    }

    public MultiArrayVolumeIterator(IMultiArray a, boolean forward) {
        constructIterator(a, forward);
    }

    private void constructIterator(IMultiArray a, boolean forward) {
        _ndim = a.getDimensions();
        if (_ndim < 3) throw new IllegalArgumentException("MultiArrayVolumeIterator: Number of dimensions must be 3 or larger");
        _a = a;
        _shape = _a.getShape();
        _position = new int[_ndim];
        _volumeMultiplier = new int[_ndim];
        _nvolumes = 1;
        for (int i = 2; i < _ndim; i++) {
            _nvolumes *= _shape[i];
            _volumeMultiplier[i] = _nvolumes;
        }
        _forward = forward;
        if (_forward) {
            _ivolume = 0;
            _incr = 1;
        } else {
            _ivolume = _nvolumes - 1;
            _incr = -1;
        }
    }

    public Iterator iterator() {
        return new MultiArrayVolumeIterator(_a);
    }

    public boolean hasNext() {
        if (_forward) {
            if (_ivolume < _nvolumes) return true; else return false;
        } else {
            if (_ivolume >= 0) return true; else return false;
        }
    }

    public IMultiArray next() {
        if (!hasNext()) throw new IndexOutOfBoundsException("Call to MultiArrayVolumeIterator.next() when hasNext() is false");
        volumeToPosition();
        _ivolume += _incr;
        return _a.view(3, _position);
    }

    private void volumeToPosition() {
        _position[0] = _position[1] = _position[2] = 0;
        int ivolume = _ivolume;
        for (int i = _ndim - 1; i >= 3; i--) {
            _position[i] = ivolume % _volumeMultiplier[i];
            ivolume = ivolume - _volumeMultiplier[i] * _position[i];
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("MultiArrayFrameIterator.remove() is not supported");
    }
}
