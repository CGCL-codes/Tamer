package jaxlib.jaxlib_private.col;

import java.lang.reflect.Array;
import jaxlib.array.ObjectArrays;
import jaxlib.jaxlib_private.CheckArg;
import jaxlib.lang.JavaTypeID;
import jaxlib.lang.UnexpectedError;
import jaxlib.util.BiDi;

/**
 * Used by jaxlib.col.GapList and the GapList implementations of jaxlib.tcol.
 * <p>
 * Everything in this class is public for performance purposes (inlining).
 * </p>
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: Gap.java,v 1.2 2004/08/24 22:10:05 joerg_wassmer Exp $
 */
public final class Gap<E> extends Object implements Cloneable {

    /**
   * The default initial capacity of a GapList.
   */
    public static final int DEFAULT_CAPACITY = 16;

    /**
   * The default growfactor of a GapList.
   */
    public static final float DEFAULT_GROWFACTOR = 0.5F;

    public static final int toArrayIndex(int index, int offset, int gapStart, int gapLength) {
        index += offset;
        if (index >= gapStart) index += gapLength;
        return index;
    }

    public final JavaTypeID type;

    public final Class componentType;

    public Object array;

    /**
   * The object array representation of array, or null if the component type is not primitive.
   */
    public E[] objects;

    public boolean[] booleans;

    public byte[] bytes;

    public char[] chars;

    public double[] doubles;

    public float[] floats;

    public int[] ints;

    public long[] longs;

    public short[] shorts;

    public int capacity;

    public int size = 0;

    public int offset;

    public int gapLength;

    public int gapStart;

    public float growFactor;

    public int lowWaterMark;

    public boolean shrink = false;

    public Gap(Object initialArray, float growFactor) {
        super();
        if (initialArray == null) throw new AssertionError();
        if (!(growFactor >= 0)) throw new IllegalArgumentException("Growfactor is not >= 0: " + growFactor);
        this.array = initialArray;
        this.componentType = initialArray.getClass().getComponentType();
        this.capacity = Array.getLength(initialArray);
        this.offset = 0;
        this.gapStart = 0;
        this.gapLength = this.capacity >> 1;
        this.growFactor = growFactor;
        this.type = JavaTypeID.get(this.componentType);
        switch(this.type) {
            case OBJECT:
                this.objects = (E[]) initialArray;
                break;
            case BOOLEAN:
                this.booleans = (boolean[]) initialArray;
                break;
            case BYTE:
                this.bytes = (byte[]) initialArray;
                break;
            case CHAR:
                this.chars = (char[]) initialArray;
                break;
            case DOUBLE:
                this.doubles = (double[]) initialArray;
                break;
            case FLOAT:
                this.floats = (float[]) initialArray;
                break;
            case INT:
                this.ints = (int[]) initialArray;
                break;
            case LONG:
                this.longs = (long[]) initialArray;
                break;
            case SHORT:
                this.shorts = (short[]) initialArray;
                break;
            default:
                throw new AssertionError(this.type);
        }
    }

    /**
   * @deprecated Private.
   */
    @Deprecated
    public final void arraycopy(int fromIndex, int destIndex, int length) {
        if (fromIndex != destIndex) {
            if (length < 16) {
                int toIndex = fromIndex + length;
                if (destIndex < fromIndex) {
                    switch(this.type) {
                        case OBJECT:
                            for (Object[] a = this.objects; fromIndex < toIndex; ) a[destIndex++] = a[fromIndex++];
                            break;
                        case BOOLEAN:
                            for (boolean[] a = this.booleans; fromIndex < toIndex; ) a[destIndex++] = a[fromIndex++];
                            break;
                        case BYTE:
                            for (byte[] a = this.bytes; fromIndex < toIndex; ) a[destIndex++] = a[fromIndex++];
                            break;
                        case CHAR:
                            for (char[] a = this.chars; fromIndex < toIndex; ) a[destIndex++] = a[fromIndex++];
                            break;
                        case DOUBLE:
                            for (double[] a = this.doubles; fromIndex < toIndex; ) a[destIndex++] = a[fromIndex++];
                            break;
                        case FLOAT:
                            for (float[] a = this.floats; fromIndex < toIndex; ) a[destIndex++] = a[fromIndex++];
                            break;
                        case INT:
                            for (int[] a = this.ints; fromIndex < toIndex; ) a[destIndex++] = a[fromIndex++];
                            break;
                        case LONG:
                            for (long[] a = this.longs; fromIndex < toIndex; ) a[destIndex++] = a[fromIndex++];
                            break;
                        case SHORT:
                            for (short[] a = this.shorts; fromIndex < toIndex; ) a[destIndex++] = a[fromIndex++];
                            break;
                        default:
                            throw new AssertionError(this.type);
                    }
                } else {
                    destIndex += (toIndex - fromIndex);
                    switch(this.type) {
                        case OBJECT:
                            for (Object[] a = this.objects; fromIndex < toIndex; ) a[--destIndex] = a[--toIndex];
                            break;
                        case BOOLEAN:
                            for (boolean[] a = this.booleans; fromIndex < toIndex; ) a[--destIndex] = a[--toIndex];
                            break;
                        case BYTE:
                            for (byte[] a = this.bytes; fromIndex < toIndex; ) a[--destIndex] = a[--toIndex];
                            break;
                        case CHAR:
                            for (char[] a = this.chars; fromIndex < toIndex; ) a[--destIndex] = a[--toIndex];
                            break;
                        case DOUBLE:
                            for (double[] a = this.doubles; fromIndex < toIndex; ) a[--destIndex] = a[--toIndex];
                            break;
                        case FLOAT:
                            for (float[] a = this.floats; fromIndex < toIndex; ) a[--destIndex] = a[--toIndex];
                            break;
                        case INT:
                            for (int[] a = this.ints; fromIndex < toIndex; ) a[--destIndex] = a[--toIndex];
                            break;
                        case LONG:
                            for (long[] a = this.longs; fromIndex < toIndex; ) a[--destIndex] = a[--toIndex];
                            break;
                        case SHORT:
                            for (short[] a = this.shorts; fromIndex < toIndex; ) a[--destIndex] = a[--toIndex];
                            break;
                        default:
                            throw new AssertionError(this.type);
                    }
                }
            } else {
                System.arraycopy(this.array, fromIndex, this.array, destIndex, length);
            }
        }
    }

    /**
   * @deprecated Private.
   */
    @Deprecated
    public final void clearObjects(int arrayFromIndex, int arrayToIndex) {
        if (this.objects != null) ObjectArrays.clearFast(this.objects, arrayFromIndex, arrayToIndex);
    }

    public final Object dispose() {
        removeGap(true);
        Object array = this.array;
        setArray(Array.newInstance(this.componentType, 0), 0, 0, 0, 0, 0);
        return array;
    }

    /**
   * @deprecated Private.
   */
    @Deprecated
    public final void setArray(Object newArray, int newCapacity, int newSize, int newOffset, int newGapStart, int newGapLength) {
        if (newSize == 0) {
            newOffset = 0;
            newGapStart = 0;
            newGapLength = newCapacity >> 1;
        } else if ((newGapLength > 0) && (newGapStart <= newOffset)) {
            newOffset = newGapStart + newGapLength;
            newGapLength = 0;
        }
        this.array = newArray;
        this.capacity = newCapacity;
        this.size = newSize;
        this.offset = newOffset;
        this.gapStart = (newGapLength == 0) ? 0 : newGapStart;
        this.gapLength = newGapLength;
        this.lowWaterMark = this.shrink ? ((int) Math.max(1, newSize * (1 - this.growFactor))) : 0;
        switch(this.type) {
            case OBJECT:
                this.objects = (E[]) newArray;
                break;
            case BOOLEAN:
                this.booleans = (boolean[]) newArray;
                break;
            case BYTE:
                this.bytes = (byte[]) newArray;
                break;
            case CHAR:
                this.chars = (char[]) newArray;
                break;
            case DOUBLE:
                this.doubles = (double[]) newArray;
                break;
            case FLOAT:
                this.floats = (float[]) newArray;
                break;
            case INT:
                this.ints = (int[]) newArray;
                break;
            case LONG:
                this.longs = (long[]) newArray;
                break;
            case SHORT:
                this.shorts = (short[]) newArray;
                break;
            default:
                throw new AssertionError(this.type);
        }
    }

    public final void setAutoReducingCapacity(boolean enable) {
        this.shrink = enable;
        this.lowWaterMark = enable ? ((int) Math.max(1, this.size * (1 - this.growFactor))) : 0;
    }

    public final void setGrowFactor(float growFactor) {
        if (!(growFactor >= 0)) throw new IllegalArgumentException("Growfactor is not >= 0: " + growFactor);
        this.growFactor = growFactor;
    }

    public Gap<E> clone() {
        try {
            Gap<E> clone = (Gap<E>) super.clone();
            Object a = Array.newInstance(this.componentType, this.capacity);
            System.arraycopy(this.array, 0, a, 0, this.capacity);
            clone.setArray(a, clone.capacity, clone.size, clone.offset, clone.gapStart, clone.gapLength);
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new UnexpectedError(ex);
        }
    }

    public final int toArrayIndex(int index) {
        if (index < 0) throw new IndexOutOfBoundsException("index(" + index + ") < 0.");
        if (index >= this.size) throw new IndexOutOfBoundsException("index(" + index + ") >= size(" + this.size + ").");
        index += this.offset;
        if (index >= this.gapStart) index += this.gapLength;
        return index;
    }

    public final int toArrayIndexUnsafe(int index) {
        index += this.offset;
        if (index >= this.gapStart) index += this.gapLength;
        return index;
    }

    /**
   * @return the translated index, or the original one if <code>index < 0</code>.
   */
    public final int toListIndex(int index) {
        if (index < 0) return index;
        return ((index < this.gapStart) ? index : (index - this.gapLength)) - this.offset;
    }

    /**
   * Index of first element before gap.
   */
    public final int from1(int fromIndex, int toIndex) {
        CheckArg.range(this.size, fromIndex, toIndex);
        fromIndex += this.offset;
        return ((fromIndex < this.gapStart) || (this.gapLength == 0)) ? fromIndex : 0;
    }

    /**
   * Index after last element before gap.
   */
    public final int to1(int fromIndex, int toIndex) {
        if (fromIndex == toIndex) toIndex = from1(fromIndex, toIndex); else if (this.gapLength == 0) toIndex = toIndex + this.offset; else if (fromIndex + this.offset >= this.gapStart) toIndex = 0; else {
            toIndex += this.offset;
            toIndex = (toIndex <= this.gapStart) ? toIndex : this.gapStart;
        }
        return toIndex;
    }

    /**
   * Index of first element after gap.
   */
    public final int from2(int fromIndex, int toIndex) {
        CheckArg.range(this.size, fromIndex, toIndex);
        return ((toIndex + this.offset > this.gapStart) && (this.gapLength != 0)) ? (this.gapStart + this.gapLength) : 0;
    }

    /**
   * Index after last element after gap.
   */
    public final int to2(int fromIndex, int toIndex) {
        if (fromIndex == toIndex) toIndex = from2(fromIndex, toIndex); else if (this.gapLength > 0) {
            toIndex += this.offset;
            toIndex = (toIndex > this.gapStart) ? (toIndex + this.gapLength) : 0;
        } else toIndex = 0;
        return toIndex;
    }

    public final int from1(int fromIndex, int toIndex, BiDi dir) {
        return (dir == BiDi.FORWARD) ? from1(fromIndex, toIndex) : from2(fromIndex, toIndex);
    }

    public final int to1(int fromIndex, int toIndex, BiDi dir) {
        return (dir == BiDi.FORWARD) ? to1(fromIndex, toIndex) : to2(fromIndex, toIndex);
    }

    public final int from2(int fromIndex, int toIndex, BiDi dir) {
        return (dir == BiDi.FORWARD) ? from2(fromIndex, toIndex) : from1(fromIndex, toIndex);
    }

    public final int to2(int fromIndex, int toIndex, BiDi dir) {
        return (dir == BiDi.FORWARD) ? to2(fromIndex, toIndex) : to1(fromIndex, toIndex);
    }

    public final void clear(int fromIndex, int toIndex) {
        final int size = this.size;
        CheckArg.range(size, fromIndex, toIndex);
        final int count = toIndex - fromIndex;
        if (count == 0) return;
        final int newSize = size - count;
        if (this.shrink && (newSize < this.lowWaterMark)) {
            Object newArray = Array.newInstance(this.componentType, newSize);
            toArray(0, fromIndex, newArray, 0);
            toArray(toIndex, size, newArray, fromIndex);
            setArray(newArray, newSize, newSize, 0, 0, 0);
        } else {
            int offset = this.offset;
            int gapLength = this.gapLength;
            int gapStart = (gapLength == 0) ? toIndex : this.gapStart;
            fromIndex += offset;
            toIndex += offset;
            if (gapLength == 0) {
                clearObjects(fromIndex, toIndex);
                this.gapStart = gapStart = fromIndex;
                this.gapLength = gapLength = count;
            } else if (toIndex <= gapStart) {
                final int moveToGap = gapStart - toIndex;
                final int moveToOffset = fromIndex - offset;
                if (moveToOffset <= moveToGap) {
                    final int newOffset = offset + count;
                    arraycopy(offset, newOffset, moveToOffset);
                    clearObjects(offset, newOffset);
                    this.offset = offset = newOffset;
                } else {
                    arraycopy(toIndex, fromIndex, moveToGap);
                    clearObjects(fromIndex + moveToGap, gapStart);
                    this.gapStart = gapStart = fromIndex + moveToGap;
                    this.gapLength = gapLength = gapLength + count;
                }
            } else if (fromIndex >= gapStart) {
                fromIndex += gapLength;
                toIndex += gapLength;
                final int end = offset + gapLength + size;
                final int gapEnd = gapStart + gapLength;
                final int moveToGap = fromIndex - gapEnd;
                final int moveToEnd = end - toIndex;
                if (moveToEnd <= moveToGap) {
                    arraycopy(toIndex, fromIndex, moveToEnd);
                    clearObjects(end - count, end);
                } else {
                    arraycopy(gapEnd, toIndex - moveToGap, moveToGap);
                    clearObjects(gapEnd, toIndex - moveToGap);
                    this.gapLength = gapLength += count;
                }
            } else {
                toIndex += gapLength;
                clearObjects(fromIndex, gapStart);
                clearObjects(gapStart + gapLength, toIndex);
                this.gapStart = gapStart = fromIndex;
                this.gapLength = gapLength += count;
            }
            if (newSize == 0) {
                this.offset = 0;
                this.gapStart = 0;
                this.gapLength = this.capacity >> 1;
            } else if ((gapLength > 0) && (gapStart <= offset)) {
                this.offset = gapStart + gapLength;
                this.gapLength = 0;
            }
            this.size = newSize;
        }
    }

    public final int prepareAdd(int index, int count) {
        final int size = this.size;
        CheckArg.rangeForAdding(size, index);
        if (count == 0) return this.gapStart;
        if (count < 0) CheckArg.count(count);
        final int newSize = size + count;
        if (newSize > this.capacity) trimCapacity((int) (newSize + (newSize * this.growFactor)));
        int offset = this.offset;
        int gapLength = this.gapLength;
        int gapStart = this.gapStart;
        index += offset;
        if (index < gapStart) {
            final int moveToOffset = index - offset;
            final int moveToGap = gapStart - index;
            if (((moveToOffset <= moveToGap) || (gapLength < count)) && (offset >= count)) {
                final int newOffset = offset - count;
                arraycopy(offset, newOffset, moveToOffset);
                this.offset = offset = newOffset;
                index -= count;
            } else if (gapLength >= count) {
                arraycopy(index, index + count, moveToGap);
                this.gapStart = gapStart += count;
                this.gapLength = gapLength -= count;
            } else {
                int minGrowGap = count - (offset + gapLength);
                if (minGrowGap > 0) {
                    int endSpaceToUse = this.capacity - (offset + gapLength + size);
                    if (endSpaceToUse > minGrowGap) endSpaceToUse -= (endSpaceToUse - minGrowGap) >> 1;
                    if (gapLength == 0) {
                        gapStart = index;
                        gapLength = endSpaceToUse;
                        int gapEnd = gapStart + gapLength;
                        arraycopy(gapStart, gapEnd, size - (gapStart - offset));
                        clearObjects(gapStart + minGrowGap, gapEnd);
                    } else {
                        int oldGapEnd = gapStart + gapLength;
                        int newGapEnd = oldGapEnd + endSpaceToUse;
                        arraycopy(oldGapEnd, newGapEnd, size - (gapStart - offset));
                        gapLength += endSpaceToUse;
                        clearObjects(oldGapEnd + minGrowGap, newGapEnd);
                    }
                }
                final int newOffset = 0;
                arraycopy(offset, newOffset, moveToOffset);
                count -= offset - newOffset;
                arraycopy(index, index + count, gapStart - index);
                index -= offset - newOffset;
                this.gapStart = gapStart += count;
                this.gapLength = gapLength -= count;
                this.offset = offset = newOffset;
            }
        } else {
            index += gapLength;
            final int end = offset + gapLength + size;
            final int endLength = this.capacity - end;
            int gapEnd = gapStart + gapLength;
            final int moveToGap = index - gapEnd;
            final int moveToEnd = end - index;
            if (((moveToEnd <= moveToGap) || (gapLength < count)) && (endLength >= count)) {
                arraycopy(index, index + count, end - index);
            } else if (gapLength >= count) {
                arraycopy(gapEnd, gapEnd - count, index - gapEnd);
                index -= count;
                this.gapLength = gapLength -= count;
            } else {
                int minGrowGap = count - (gapLength + endLength);
                if (minGrowGap > 0) {
                    int frontSpaceToUse = offset;
                    if (frontSpaceToUse > minGrowGap) frontSpaceToUse -= (frontSpaceToUse - minGrowGap) >> 1;
                    int newOffset = offset - frontSpaceToUse;
                    if (gapLength == 0) {
                        arraycopy(offset, newOffset, index - offset);
                        gapStart = newOffset + (index - offset);
                        gapLength = frontSpaceToUse;
                    } else {
                        arraycopy(offset, newOffset, gapStart - offset);
                        gapStart -= frontSpaceToUse;
                        gapLength += frontSpaceToUse;
                    }
                    this.gapStart = gapStart;
                    this.offset = offset = newOffset;
                    gapEnd = gapStart + gapLength;
                    clearObjects(gapStart, gapStart + gapLength - minGrowGap);
                }
                arraycopy(index, index + endLength, end - index);
                count -= endLength;
                arraycopy(gapEnd, gapEnd - count, index - gapEnd);
                index -= count;
                this.gapLength = gapLength -= count;
            }
        }
        if ((gapLength > 0) && (gapStart <= offset)) {
            this.offset = gapStart + gapLength;
            this.gapLength = 0;
        }
        this.size = newSize;
        return index;
    }

    public final void removeGap(boolean offsetAlso) {
        int offset = this.offset;
        int gapLength = this.gapLength;
        int size = this.size;
        if (gapLength > 0) {
            int hi = offset + gapLength + size;
            int gapStart = this.gapStart;
            int gapEnd = gapStart + gapLength;
            arraycopy(gapEnd, gapStart, hi - gapEnd);
            this.gapLength = 0;
        }
        if (offsetAlso && (offset > 0)) {
            arraycopy(offset, 0, size);
            clearObjects(size, offset + size);
            this.offset = 0;
        } else clearObjects(offset + size, offset + size + gapLength);
    }

    public final void removeGap(int fromIndex, int toIndex) {
        CheckArg.range(this.size, fromIndex, toIndex);
        fromIndex += this.offset;
        toIndex += this.offset;
        if ((fromIndex <= this.gapStart) && (toIndex > this.gapStart)) removeGap(false);
    }

    public final void swapContent(Gap<E> b) {
        if (b == this) return;
        assert (this.componentType == b.componentType);
        Object tarray = this.array;
        this.array = b.array;
        b.array = tarray;
        E[] tobjects = this.objects;
        this.objects = b.objects;
        b.objects = tobjects;
        boolean[] tbooleans = this.booleans;
        this.booleans = b.booleans;
        b.booleans = tbooleans;
        byte[] tbytes = this.bytes;
        this.bytes = b.bytes;
        b.bytes = tbytes;
        char[] tchars = this.chars;
        this.chars = b.chars;
        b.chars = tchars;
        double[] tdoubles = this.doubles;
        this.doubles = b.doubles;
        b.doubles = tdoubles;
        float[] tfloats = this.floats;
        this.floats = b.floats;
        b.floats = tfloats;
        int[] tints = this.ints;
        this.ints = b.ints;
        b.ints = tints;
        long[] tlongs = this.longs;
        this.longs = b.longs;
        b.longs = tlongs;
        short[] tshorts = this.shorts;
        this.shorts = b.shorts;
        b.shorts = tshorts;
        int t = this.capacity;
        this.capacity = b.capacity;
        b.capacity = t;
        t = this.size;
        this.size = b.size;
        b.size = t;
        t = this.offset;
        this.offset = b.offset;
        b.offset = t;
        t = this.gapLength;
        this.gapLength = b.gapLength;
        b.gapLength = t;
        t = this.gapStart;
        this.gapStart = b.gapStart;
        b.gapStart = t;
        this.setAutoReducingCapacity(this.shrink);
        b.setAutoReducingCapacity(b.shrink);
    }

    public final void toArray(int fromIndex, int toIndex, Object target, int targetIndex) {
        CheckArg.copyRangeTo(this.size, fromIndex, toIndex, Array.getLength(target), targetIndex);
        int offset = this.offset;
        int gapLength = this.gapLength;
        int gapStart = this.gapStart;
        fromIndex += offset;
        toIndex += offset;
        if ((gapLength == 0) || (toIndex <= gapStart)) System.arraycopy(this.array, fromIndex, target, targetIndex, toIndex - fromIndex); else if (fromIndex >= gapStart) System.arraycopy(this.array, fromIndex + gapLength, target, targetIndex, toIndex - fromIndex); else {
            int a = gapStart - fromIndex;
            System.arraycopy(this.array, fromIndex, target, targetIndex, a);
            System.arraycopy(this.array, gapStart + gapLength, target, targetIndex + a, toIndex - gapStart);
        }
    }

    public final int trimCapacity(int newCapacity) {
        CheckArg.newCapacity(newCapacity);
        int size = this.size;
        newCapacity = Math.max(size, newCapacity);
        if (newCapacity != this.capacity) {
            Object newArray = Array.newInstance(this.componentType, newCapacity);
            int newFree = newCapacity - size;
            int newGapLength = newFree / 3;
            int newOffset = newGapLength;
            int beforeGap = size >> 1;
            int newGapStart = newOffset + beforeGap;
            toArray(0, beforeGap, newArray, newOffset);
            toArray(beforeGap, size, newArray, newGapStart + newGapLength);
            setArray(newArray, newCapacity, size, newOffset, newGapStart, newGapLength);
        }
        return newCapacity;
    }
}
