package uk.ac.shef.wit.runes.runestone.basic;

import uk.ac.shef.wit.runes.Runes;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.runestone.Stone;
import uk.ac.shef.wit.runes.runestone.StoneHelper;
import uk.ac.shef.wit.runes.runestone.StoneWithoutContent;
import uk.ac.shef.wit.runes.runestone.UtilRunestone;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

/**
 * A {@link Stone} optimized for representing binary relations whose domain is dense.
 *
 * @author <a href="mailto:j.iria@dcs.shef.ac.uk">Jos&eacute; Iria</a>
 * @version $Id: StoneBasicDenseBitMap.java 415 2008-08-06 13:55:36Z jiria $
 */
public class StoneBasicDenseBitMap extends StoneWithoutContent {

    private static final double VERSION = 0.5;

    static {
        Runes.registerStone(StoneBasicDenseBitMap.class, MessageFormat.format("[core] dense bit map v{0}", StoneBasicDenseBitMap.VERSION));
    }

    private static final Logger log = Logger.getLogger(StoneBasicDenseBitMap.class.getName());

    protected int[] _ids;

    private int[] _max;

    private int _used;

    public StoneBasicDenseBitMap(final Stone<? extends Serializable> stone, final int[] dimensions, final long bestTime, final long bestSpace, final double tradeoff) throws RunesExceptionCannotHandle {
        if (2 != dimensions.length) throw new RunesExceptionCannotHandle();
        final int newDimension = UtilRunestone.computeExpansion(dimensions[0]);
        StoneHelper.lowerBoundOnSpace((long) ((1 << 4) + (newDimension << 2)), bestTime, bestSpace, tradeoff);
        _used = 0;
        _max = new int[2];
        _ids = new int[newDimension];
        if (stone instanceof StoneBasicDenseBitMap) {
            final StoneBasicDenseBitMap other = (StoneBasicDenseBitMap) stone;
            System.arraycopy(other._ids, 0, _ids, 0, other._ids.length);
            for (int i = 0; 2 > i; ++i) _max[i] = dimensions[i];
            _used = other._used;
        } else StoneHelper.fastInitNoEncoding(this, stone, bestTime, bestSpace, tradeoff);
    }

    public long size() {
        return (long) ((1 << 4) + (_ids.length << 2));
    }

    public int[] dimensions() {
        final int[] dimensions = new int[_max.length];
        for (int i = 0, size = _max.length; i < size; ++i) dimensions[i] = _max[i];
        return dimensions;
    }

    public int cardinality(final int... mask) {
        final boolean lengthOne = 1 == mask.length;
        final boolean lengthTwo = 2 == mask.length;
        int size = 0;
        if (0 == mask.length || lengthOne && 0 == mask[0] || lengthTwo && 0 == mask[0] && 0 == mask[1]) size = _used; else if (lengthOne || lengthTwo && 0 == mask[1]) size = mask[0] <= _max[0] && 0 != _ids[mask[0]] ? 1 : 0; else if (lengthTwo && 0 == mask[0]) {
            for (int i = 0, len = _max[0]; i <= len; ++i) if (_ids[i] == mask[1]) ++size;
        } else if (lengthTwo) size = mask[0] <= _max[0] && mask[1] == _ids[mask[0]] ? 1 : 0;
        return size;
    }

    public Iterator<int[]> getStructureIterator(final int... mask) {
        if (0 == mask.length || 1 == mask.length && 0 == mask[0] || 2 == mask.length && 0 == mask[0] && 0 == mask[1]) return new Iterator<int[]>() {

            private final int[] _intArrayOfTwo = new int[2];

            private boolean _found;

            public boolean hasNext() {
                if (!_found) _found = lookahead();
                return _found;
            }

            public int[] next() {
                if (!hasNext()) throw new NoSuchElementException();
                _found = false;
                return _intArrayOfTwo;
            }

            public void remove() {
                _ids[_intArrayOfTwo[0]] = 0;
            }

            private boolean lookahead() {
                _intArrayOfTwo[1] = 0;
                while (0 == _intArrayOfTwo[1] && ++_intArrayOfTwo[0] <= _max[0]) _intArrayOfTwo[1] = _ids[_intArrayOfTwo[0]];
                return _intArrayOfTwo[0] <= _max[0];
            }
        }; else if (2 == mask.length && 0 == mask[0]) return new Iterator<int[]>() {

            private final int[] _intArrayOfTwo = new int[] { 0, mask[1] };

            private boolean _found;

            public boolean hasNext() {
                if (!_found) _found = lookahead();
                return _found;
            }

            public int[] next() {
                if (!hasNext()) throw new NoSuchElementException();
                _found = false;
                return _intArrayOfTwo;
            }

            public void remove() {
                _ids[_intArrayOfTwo[0]] = 0;
            }

            private boolean lookahead() {
                while (++_intArrayOfTwo[0] <= _max[0] && _ids[_intArrayOfTwo[0]] != _intArrayOfTwo[1]) ;
                return _intArrayOfTwo[0] <= _max[0];
            }
        }; else if (1 == mask.length && mask[0] <= _max[0] && 0 != _ids[mask[0]] || 2 == mask.length && mask[0] <= _max[0] && mask[1] == _ids[mask[0]]) return new Iterator<int[]>() {

            private final int[] _intArrayOfTwo = new int[] { mask[0], _ids[mask[0]] };

            private boolean _hasNext = true;

            public boolean hasNext() {
                return _hasNext;
            }

            public int[] next() {
                if (!hasNext()) throw new NoSuchElementException();
                _hasNext = false;
                return _intArrayOfTwo;
            }

            public void remove() {
                _ids[mask[0]] = 0;
            }
        };
        return UtilRunestone.emptyStructureIterator();
    }

    public boolean erase(final int... mask) {
        if (2 == mask.length && 0 != mask[0] && 0 != mask[1] && mask[0] <= _max[0]) {
            final int value = _ids[mask[0]];
            if (mask[1] == value) {
                _ids[mask[0]] = 0;
                --_used;
                return true;
            }
        } else if (0 == mask.length) {
            _ids = new int[_used = 0];
            _max = new int[2];
            return true;
        } else assert false : "to implement";
        return false;
    }

    public int inscribe(final boolean encode, final Serializable content, final int... id) throws RunesExceptionCannotHandle {
        if (content == null) {
            if (2 == id.length && id[0] < _ids.length) {
                if (0 != id[0] && 0 != id[1]) {
                    if (_max[0] < id[0]) _max[0] = id[0];
                    if (_max[1] < id[1]) _max[1] = id[1];
                    final int value = _ids[id[0]];
                    if (0 == value) {
                        _ids[id[0]] = id[1];
                        ++_used;
                    } else if (value != id[1]) throw new RunesExceptionCannotHandle();
                }
                return id[0];
            }
        }
        throw generateCannotHandle(content, id);
    }

    public boolean exists(final int... id) {
        return 2 == id.length && 0 != id[0] && 0 != id[1] && id[0] <= _max[0] && _ids[id[0]] == id[1];
    }

    public int encode(final int... id) throws RunesExceptionCannotHandle {
        throw new RunesExceptionCannotHandle();
    }

    public int[] decode(final int id) throws RunesExceptionCannotHandle {
        throw new RunesExceptionCannotHandle();
    }

    @Override
    public String toString() {
        return "dense bit map " + UtilRunestone.idToString(dimensions());
    }
}
