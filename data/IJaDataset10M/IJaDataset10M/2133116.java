package jaxlib.tcol.tshort;

import java.io.DataOutput;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ShortBuffer;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import jaxlib.util.BiDi;
import jaxlib.util.sorting.SortAlgorithm;
import jaxlib.closure.IntTransformer;
import jaxlib.closure.ShortClosure;
import jaxlib.closure.IntFilter;
import jaxlib.closure.DoShort;
import jaxlib.closure.ForEachShort;
import jaxlib.array.ShortArrays;
import jaxlib.util.AccessType;
import jaxlib.util.AccessTypeSet;
import jaxlib.io.stream.DataIO;
import jaxlib.jaxlib_private.CheckArg;

/**
 * Provides an optionally read-only wrapper over a <tt>short[]</tt> array.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: ShortArray.java 1069 2004-04-09 15:48:50Z joerg_wassmer $
 */
public class ShortArray extends AbstractShortList implements ShortList, RandomAccess, Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    public static ShortArray readOnly(short[] a) {
        return new ShortArray(a, true);
    }

    /**
   * @serial
   * @since JaXLib 1.0
   */
    protected final short[] array;

    /**
   * @serial
   * @since JaXLib 1.0
   */
    public final boolean readOnly;

    /**
   * @since JaXLib 1.0
   */
    private transient ShortBuffer privateBuffer;

    public ShortArray(short[] array) {
        super();
        if (array == null) throw new NullPointerException("array");
        this.array = array;
        this.readOnly = false;
    }

    public ShortArray(short[] array, boolean readOnly) {
        super();
        if (array == null) throw new NullPointerException("array");
        this.array = array;
        this.readOnly = readOnly;
    }

    public ShortArray(int size) {
        super();
        this.array = new short[size];
        this.readOnly = false;
    }

    public ShortArray(ShortList source) {
        super();
        this.array = source.toArray();
        this.readOnly = false;
    }

    public ShortArray(ShortList source, int fromIndex, int toIndex) {
        super();
        this.array = source.toArray(fromIndex, toIndex);
        this.readOnly = false;
    }

    @Overrides
    final short[] getArrayIfArrayList() {
        return this.array;
    }

    @Overrides
    final int getArrayOffsetIfArrayList() {
        return 0;
    }

    @Overrides
    public final AccessTypeSet accessTypes() {
        return this.readOnly ? AccessTypeSet.READ_ONLY : AccessTypeSet.READ_SET;
    }

    @Overrides
    public final void at(int index, short e) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        this.array[index] = e;
    }

    private ShortBuffer privateBuffer() {
        ShortBuffer b = this.privateBuffer;
        if (b == null) {
            b = ShortBuffer.wrap(this.array);
            if (this.readOnly) b = b.asReadOnlyBuffer();
            this.privateBuffer = b;
        }
        return b;
    }

    /**
   * Returns a buffer wrapping the array of the receiving <tt>ShortArray</tt>.
   * The returned buffer is <tt>readOnly</tt> if the <tt>ShortArray</tt> is.
   *
   * @since JaXLib 1.0
   */
    public final ShortBuffer asBuffer() {
        return ShortBuffer.wrap(this.array);
    }

    public final ShortBuffer asReadOnlyBuffer() {
        return privateBuffer().asReadOnlyBuffer();
    }

    @Overrides
    public final int binarySearch(int fromIndex, int toIndex, short e) {
        return ShortArrays.binarySearch(this.array, fromIndex, toIndex, e);
    }

    @Overrides
    public final int binarySearchFirst(int fromIndex, int toIndex, short e) {
        return ShortArrays.binarySearchFirst(this.array, fromIndex, toIndex, e);
    }

    @Overrides
    public final int binarySearchLast(int fromIndex, int toIndex, short e) {
        return ShortArrays.binarySearchLast(this.array, fromIndex, toIndex, e);
    }

    @Overrides
    public final int capacity() {
        return this.array.length;
    }

    @Overrides
    public final int copy(int fromIndex, int toIndex, int destIndex) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        CheckArg.copyRangeToUngrowableList(this.array.length, fromIndex, toIndex, this.array.length, destIndex);
        ShortArrays.copyFast(this.array, fromIndex, toIndex, destIndex);
        return 0;
    }

    @Overrides
    public int copy(int index, ShortList source, int fromIndex, int toIndex) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        CheckArg.copyRangeToUngrowableList(source.size(), fromIndex, toIndex, this.array.length, index);
        if (fromIndex != toIndex) source.toArray(fromIndex, toIndex, this.array, index);
        return 0;
    }

    @Overrides
    public final int copy(int index, short[] source, int fromIndex, int toIndex) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        CheckArg.copyRangeToUngrowableList(source.length, fromIndex, toIndex, this.array.length, index);
        ShortArrays.copyFast(source, fromIndex, toIndex, this.array, index);
        return 0;
    }

    @Overrides
    public int countMatches(int fromIndex, int toIndex, BiDi dir, IntFilter condition, boolean iF, int maxCount, boolean stopOnDismatch) {
        return ForEachShort.countUp(this.array, fromIndex, toIndex, dir, condition, iF, maxCount, stopOnDismatch);
    }

    @Overrides
    public final int countUp(int fromIndex, int toIndex, short e, int maxCount) {
        return ShortArrays.countUp(this.array, fromIndex, toIndex, e, maxCount);
    }

    @Overrides
    public boolean equals(Object o) {
        if (o == this) return true; else if (o instanceof ShortArray) return ShortArrays.equals(this.array, ((ShortArray) o).array); else if (o instanceof AbstractShortList) {
            AbstractShortList b = (AbstractShortList) o;
            int bo = b.getArrayOffsetIfArrayList();
            if (bo >= 0) return ShortArrays.equals(this.array, 0, this.array.length, b.getArrayIfArrayList(), bo, bo + b.size());
        }
        return super.equals(o);
    }

    @Overrides
    public final int fill(int fromIndex, int toIndex, short e) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        ShortArrays.fill(this.array, fromIndex, toIndex, e);
        return 0;
    }

    @Overrides
    public final short first() {
        if (this.array.length == 0) throw new NoSuchElementException(); else return this.array[0];
    }

    @Overrides
    public int forEach(int fromIndex, int toIndex, BiDi dir, ShortClosure procedure) {
        return ForEachShort.proceed(this.array, fromIndex, toIndex, dir, procedure);
    }

    @Overrides
    public void forEachApply(int fromIndex, int toIndex, BiDi dir, IntTransformer function) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        ForEachShort.apply(this.array, fromIndex, toIndex, dir, function);
    }

    @Overrides
    public final int freeCapacity() {
        return 0;
    }

    @Overrides
    public final short get(int index) {
        return this.array[index];
    }

    @Overrides
    public int hashCode() {
        return ShortArrays.hashCode(this.array);
    }

    @Overrides
    public final int indexOf(int fromIndex, int toIndex, short e) {
        return ShortArrays.indexOf(this.array, fromIndex, toIndex, e);
    }

    @Overrides
    public int indexOfSequence(int fromIndex, int toIndex, ShortList seq) {
        if (seq instanceof AbstractShortList) {
            AbstractShortList al = (AbstractShortList) seq;
            short[] a = al.getArrayIfArrayList();
            if (a != null) {
                int ao = al.getArrayOffsetIfArrayList();
                return ShortArrays.indexOfSequence(this.array, fromIndex, toIndex, a, ao, ao + seq.size());
            }
        }
        return super.indexOfSequence(fromIndex, toIndex, seq);
    }

    @Overrides
    public final short last() {
        int length = this.array.length;
        if (length == 0) throw new NoSuchElementException(); else return this.array[length - 1];
    }

    @Overrides
    public final int lastIndexOf(int fromIndex, int toIndex, short e) {
        return ShortArrays.lastIndexOf(this.array, fromIndex, toIndex, e);
    }

    public int lastIndexOfSequence(int fromIndex, int toIndex, ShortList seq) {
        if (seq instanceof AbstractShortList) {
            AbstractShortList al = (AbstractShortList) seq;
            short[] a = al.getArrayIfArrayList();
            if (a != null) {
                int ao = al.getArrayOffsetIfArrayList();
                return ShortArrays.lastIndexOfSequence(this.array, fromIndex, toIndex, a, ao, ao + seq.size());
            }
        }
        return super.lastIndexOfSequence(fromIndex, toIndex, seq);
    }

    @Overrides
    public final boolean isEmpty() {
        return this.array.length == 0;
    }

    @Overrides
    public ShortListIterator listIterator(int index) {
        if (this.readOnly) return super.listIterator(index); else {
            ShortListIterator it = ShortIterators.iterator(this.array);
            it.skip(index);
            return it;
        }
    }

    @Overrides
    public final short max(int fromIndex, int toIndex) {
        return ShortArrays.max(this.array, fromIndex, toIndex);
    }

    @Overrides
    public final short min(int fromIndex, int toIndex) {
        return ShortArrays.min(this.array, fromIndex, toIndex);
    }

    @Overrides
    public final int replaceEach(int fromIndex, int toIndex, short oldValue, short newValue) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        return ShortArrays.replaceEach(this.array, fromIndex, toIndex, oldValue, newValue);
    }

    @Overrides
    public final short set(int index, short e) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        short old = this.array[index];
        this.array[index] = e;
        return old;
    }

    @Overrides
    public final void reverse(int fromIndex, int toIndex) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        ShortArrays.reverse(this.array, fromIndex, toIndex);
    }

    @Overrides
    public final void rotate(int fromIndex, int toIndex, int distance) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        ShortArrays.rotate(this.array, fromIndex, toIndex, distance);
    }

    @Overrides
    public final int size() {
        return this.array.length;
    }

    @Overrides
    public final void sort(int fromIndex, int toIndex, SortAlgorithm algo) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        if (algo == null) algo = SortAlgorithm.getDefault();
        algo.apply(this.array, fromIndex, toIndex);
    }

    @Overrides
    public ShortList subList(int fromIndex, int toIndex) {
        if ((fromIndex == 0) && (toIndex == size())) return this; else return super.subList(fromIndex, toIndex);
    }

    @Overrides
    public final void swap(int index1, int index2) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        short t = this.array[index1];
        this.array[index1] = this.array[index2];
        this.array[index2] = t;
    }

    @Overrides
    public final void swapRanges(int count, int index1, int index2) {
        if (this.readOnly) throw new UnsupportedOperationException("This ShortArray is read-only.");
        ShortArrays.swapRanges(this.array, index1, index2, count);
    }

    @Overrides
    public final short[] toArray() {
        return ShortArrays.clone(this.array);
    }

    @Overrides
    public final short[] toArray(short[] dest) {
        if ((dest == null) || (dest.length < this.array.length)) return ShortArrays.clone(this.array); else {
            ShortArrays.copy(this.array, dest);
            return dest;
        }
    }

    @Overrides
    public final int toArray(short[] dest, int destIndex) {
        ShortArrays.copy(this.array, 0, this.array.length, dest, destIndex);
        return this.array.length;
    }

    @Overrides
    public final void toArray(int fromIndex, int toIndex, short[] dest, int destIndex) {
        ShortArrays.copy(this.array, fromIndex, toIndex, dest, destIndex);
    }

    @Overrides
    public final int toBuffer(ShortBuffer dest) {
        dest.put(this.array, 0, this.array.length);
        return this.array.length;
    }

    @Overrides
    public final void toBuffer(int fromIndex, int toIndex, ShortBuffer dest) {
        CheckArg.range(this.array.length, fromIndex, toIndex);
        dest.put(this.array, fromIndex, toIndex - fromIndex);
    }

    @Overrides
    public void toStream(int fromIndex, int toIndex, DataOutput dest) throws IOException {
        if (this.readOnly) super.toStream(fromIndex, toIndex, dest); else {
            CheckArg.range(this.array.length, fromIndex, toIndex);
            DataIO.write(dest, this.array, fromIndex, toIndex - fromIndex);
        }
    }
}
