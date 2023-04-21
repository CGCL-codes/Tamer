package org.hsqldb.lib;

import java.util.NoSuchElementException;

/**
 * A deque of long value. Implementation based on HsqlDeque class.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class LongDeque {

    private long[] list;

    private int firstindex = 0;

    private int endindex = 0;

    protected int elementCount;

    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    public LongDeque() {
        list = new long[DEFAULT_INITIAL_CAPACITY];
    }

    public int size() {
        return elementCount;
    }

    public boolean isEmpty() {
        return elementCount == 0;
    }

    public long getFirst() throws NoSuchElementException {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        return list[firstindex];
    }

    public long getLast() throws NoSuchElementException {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        return list[endindex - 1];
    }

    public long get(int i) throws IndexOutOfBoundsException {
        int index = getInternalIndex(i);
        return list[index];
    }

    public long removeFirst() throws NoSuchElementException {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        long value = list[firstindex];
        list[firstindex] = 0;
        firstindex++;
        elementCount--;
        if (elementCount == 0) {
            firstindex = endindex = 0;
        } else if (firstindex == list.length) {
            firstindex = 0;
        }
        return value;
    }

    public long removeLast() throws NoSuchElementException {
        if (elementCount == 0) {
            throw new NoSuchElementException();
        }
        endindex--;
        long value = list[endindex];
        list[endindex] = 0;
        elementCount--;
        if (elementCount == 0) {
            firstindex = endindex = 0;
        } else if (endindex == 0) {
            endindex = list.length;
        }
        return value;
    }

    public boolean add(long value) {
        resetCapacity();
        if (endindex == list.length) {
            endindex = 0;
        }
        list[endindex] = value;
        elementCount++;
        endindex++;
        return true;
    }

    public boolean addLast(long value) {
        return add(value);
    }

    public boolean addFirst(long value) {
        resetCapacity();
        firstindex--;
        if (firstindex < 0) {
            firstindex = list.length - 1;
            if (endindex == 0) {
                endindex = list.length;
            }
        }
        list[firstindex] = value;
        elementCount++;
        return true;
    }

    public void clear() {
        if (elementCount == 0) {
            return;
        }
        firstindex = endindex = elementCount = 0;
        for (int i = 0; i < list.length; i++) {
            list[i] = 0;
        }
    }

    public int indexOf(long value) {
        for (int i = 0; i < elementCount; i++) {
            int index = firstindex + i;
            if (index >= list.length) {
                index -= list.length;
            }
            if (list[index] == value) {
                return i;
            }
        }
        return -1;
    }

    public long remove(final int index) {
        int target = getInternalIndex(index);
        long value = list[target];
        if (target == firstindex) {
            list[firstindex] = 0;
            firstindex++;
            if (firstindex == list.length) {
                firstindex = 0;
            }
        } else if (target > firstindex) {
            System.arraycopy(list, firstindex, list, firstindex + 1, target - firstindex);
            list[firstindex] = 0;
            firstindex++;
            if (firstindex == list.length) {
                firstindex = 0;
            }
        } else {
            System.arraycopy(list, target + 1, list, target, endindex - target - 1);
            endindex--;
            list[endindex] = 0;
            if (endindex == 0) {
                endindex = list.length;
            }
        }
        elementCount--;
        if (elementCount == 0) {
            firstindex = endindex = 0;
        }
        return value;
    }

    public boolean contains(long value) {
        for (int i = 0; i < elementCount; i++) {
            int index = firstindex + i;
            if (index >= list.length) {
                index -= list.length;
            }
            if (list[index] == value) {
                return true;
            }
        }
        return false;
    }

    public void toArray(int[] array) {
        for (int i = 0; i < elementCount; i++) {
            array[i] = (int) get(i);
        }
    }

    private int getInternalIndex(int i) throws IndexOutOfBoundsException {
        if (i < 0 || i >= elementCount) {
            throw new IndexOutOfBoundsException();
        }
        int index = firstindex + i;
        if (index >= list.length) {
            index -= list.length;
        }
        return index;
    }

    private void resetCapacity() {
        if (elementCount < list.length) {
            return;
        }
        long[] newList = new long[list.length * 2];
        System.arraycopy(list, firstindex, newList, firstindex, list.length - firstindex);
        if (endindex <= firstindex) {
            System.arraycopy(list, 0, newList, list.length, endindex);
            endindex = list.length + endindex;
        }
        list = newList;
    }
}
