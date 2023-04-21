package org.javaseis.array;

import java.util.Iterator;

public class MultiArrayFrameIterator implements Iterable, Iterator {

    private IMultiArray _a;

    private int _ndim;

    private int _nframes;

    private int _iframe;

    private int _incr;

    private int[] _position, _shape, _frameMultiplier;

    private boolean _forward = true;

    public MultiArrayFrameIterator(IMultiArray a) {
        constructIterator(a, true);
    }

    public MultiArrayFrameIterator(IMultiArray a, boolean forward) {
        constructIterator(a, forward);
    }

    private void constructIterator(IMultiArray a, boolean forward) {
        _ndim = a.getDimensions();
        if (_ndim < 2) throw new IllegalArgumentException("Number of dimensions must be 2 or larger");
        _a = a;
        _shape = _a.getShape();
        _position = new int[_ndim];
        _frameMultiplier = new int[_ndim];
        _nframes = 1;
        for (int i = 2; i < _ndim; i++) {
            _nframes *= _shape[i];
            _frameMultiplier[i] = _nframes;
        }
        _forward = forward;
        if (_forward) {
            _iframe = 0;
            _incr = 1;
        } else {
            _iframe = _nframes;
            _incr = -1;
        }
    }

    public Iterator iterator() {
        return new MultiArrayFrameIterator(_a);
    }

    public boolean hasNext() {
        if (_forward) {
            if (_iframe < _nframes) return true; else return false;
        } else {
            if (_iframe > 0) return true; else return false;
        }
    }

    public IMultiArray next() {
        if (!hasNext()) throw new IndexOutOfBoundsException("Call to MultiArrayFrameIterator.next() when hasNext() is false");
        frameToPosition();
        _iframe += _incr;
        return _a.view(2, _position);
    }

    private void frameToPosition() {
        _position[0] = _position[1] = 0;
        int iframe = _iframe;
        for (int i = _ndim - 1; i >= 2; i--) {
            _position[i] = iframe % _frameMultiplier[i];
            iframe = iframe - _frameMultiplier[i] * _position[i];
        }
    }

    public void remove() {
        throw new UnsupportedOperationException("MultiArrayFrameIterator.remove() is not supported");
    }
}
