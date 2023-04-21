package net.sourceforge.freejava.collection.array;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import net.sourceforge.freejava.util.exception.NotImplementedException;

public class LongArrayWrapper extends AbstractArrayWrapper<long[], Long> {

    public final long[] array;

    LongArrayWrapper(long[] array) {
        this(array, 0, array.length);
    }

    LongArrayWrapper(long[] array, int start, int end) {
        super(start, end);
        if (array == null) throw new NullPointerException("array");
        if (end > array.length) throw new IndexOutOfBoundsException("Bad end index: " + end);
        this.array = array;
    }

    @Override
    public long[] allocate(int size) {
        return new long[size];
    }

    @Override
    public long[] getArray() {
        return array;
    }

    @Override
    protected IArrayWrapper<long[], Long> _select(int actualFrom, int actualTo) {
        return wrap(array, actualFrom, actualTo);
    }

    @Override
    public Long _get(int actualIndex) {
        return array[actualIndex];
    }

    @Override
    public void _set(int actualIndex, Long value) {
        array[actualIndex] = value;
    }

    @Override
    protected int _binarySearch(int actualFrom, int actualTo, Long key) {
        return Arrays.binarySearch(array, actualFrom, actualTo, key);
    }

    @Override
    protected long[] _copyArray(int actualFrom, int actualTo) {
        return Arrays.copyOfRange(array, actualFrom, actualTo);
    }

    @Override
    protected IArrayWrapper<long[], Long> _copy(int actualFrom, int actualTo) {
        long[] copyArray = _copyArray(actualFrom, actualTo);
        return wrap(copyArray);
    }

    @Override
    public void _fill(int actualFrom, int actualTo, Long val) {
        Arrays.fill(array, actualFrom, actualTo, (Long) val);
    }

    @Override
    protected void _reverse(int actualFrom, int actualTo) {
        int swaps = (actualTo - actualFrom) / 2;
        assert swaps >= 0;
        int left = actualFrom;
        int right = actualTo - 1;
        while (swaps-- != 0) {
            long temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            ++left;
            --right;
        }
    }

    @Override
    protected void _shuffle(Random random, int actualFrom, int actualTo, int strength) {
        int length = actualTo - actualFrom;
        while (strength-- > 0) {
            int n = start + random.nextInt(length);
            int m = start + random.nextInt(length);
            if (n == m) continue;
            long temp = array[n];
            array[n] = array[m];
            array[m] = temp;
        }
    }

    @Override
    protected void _sort(int actualFrom, int actualTo) {
        Arrays.sort(array, actualFrom, actualTo);
    }

    @Override
    protected void _sort(int actualFrom, int actualTo, Comparator<? super Long> comparator) {
        throw new NotImplementedException();
    }

    @Override
    public int indexOf(Long val, int start) {
        long _val = val;
        int actualStart = checkIndex(start);
        for (int i = actualStart; i < end; i++) if (array[i] == _val) return i;
        return -1;
    }

    @Override
    public int lastIndexOf(Long val, int start) {
        long _val = val;
        int actualStart = checkIndex(start);
        for (int i = actualStart; i >= start; i--) if (array[i] == _val) return i;
        return -1;
    }
}
