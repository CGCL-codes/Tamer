package jaxlib.array;

import jaxlib.jaxlib_private.CheckArg;
import jaxlib.lang.Bytes;

/**
 * Provides routines to handle <tt>byte[]</tt> arrays as arrays of bits.
 *
 * @author  <a href="mailto:joerg.wassmer@web.de">J�rg Wa�mer</a>
 * @since   JaXLib 1.0
 * @version $Id: ByteBitArrays.java,v 1.3 2004/09/24 01:10:48 joerg_wassmer Exp $
 */
public class ByteBitArrays extends Object {

    protected ByteBitArrays() throws InstantiationException {
        throw new InstantiationException();
    }

    private static void checkRange(byte[] bits, int index) {
        CheckArg.range(bits.length << Bytes.ADDRESS_BITS, index);
    }

    private static void checkRange(byte[] bits, int fromIndex, int toIndex) {
        CheckArg.range(bits.length << Bytes.ADDRESS_BITS, fromIndex, toIndex);
    }

    private static void checkByteRange(byte[] bits, int fromIndex, int toIndex) {
        CheckArg.range(bits.length << Bytes.ADDRESS_BITS, fromIndex, toIndex);
        if (toIndex - fromIndex > Bytes.BITS) throw new IllegalArgumentException("toIndex(" + toIndex + ") - fromIndex(" + fromIndex + ") > 8.");
    }

    /**
   * Unsets the bit in specified bitvector at specified bitindex.
   *
   * @param bits  the bitvector.
   * @param index index of the bit to change.
   * 
   * @throws ArrayIndexOutOfBoundsException 
   *  if the index is out of range <code>(index &lt; 0) || (index &gt;= (bits.length * 8))</code>.
   * @throws NullPointerException      
   *   if <code>bits == null</code>.
   *
   * @since JaXLib 1.0
   */
    public static void clear(byte[] bits, int index) {
        bits[index >> Bytes.ADDRESS_BITS] = (byte) ((bits[index >> Bytes.ADDRESS_BITS] & 0xff) & ~(1 << (index & Bytes.BIT_INDEX_MASK)));
    }

    /**
   * Copies specified subsequence of specified bitvector to another position in the same bitvector.
   *
   * @param bits      the bitvector.
   * @param fromIndex index of the first bit to copy.
   * @param toIndex   index after last bit to copy.
   * @param destIndex the index where to copy bits to.
   *
   * @throws IndexOutOfBoundsException  for an illegal endpoint index value 
   *                                    (<tt>(fromIndex &lt; 0 || toIndex &gt; bits.length * 8 || fromIndex &gt; toIndex || destIndex < 0 || destIndex &gt; bits.length * 8)</tt>),
   *                                    or if the range between <tt>destIndex</tt> and <tt>data.length</tt> is to small to hold
   *                                    the source range.
   * @throws NullPointerException       if <tt>bits == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void copy(byte[] bits, int fromIndex, int toIndex, int destIndex) {
        copy(bits, fromIndex, toIndex, bits, destIndex);
    }

    /**
   * Copies all bits of source bitvector to destination bitvector.
   * Source and destination indices are zero.
   *
   * @throws IndexOutOfBoundsException  if <tt>dest.length < src.length</tt>.
   * @throws NullPointerException       if <tt>src == null || dest == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void copy(byte[] src, byte[] dest) {
        copy(src, 0, src.length << Bytes.ADDRESS_BITS, dest, 0);
    }

    /**
   * Copies the bits of the specified source sequence to the specified destination.
   * <p>
   * If the both specified arrays are identical then copying is performed as if the source sequence would
   * be copied to a temporary array and that array would than be copied to the specified destination index.
   * </p>
   *
   * @param src       the source bit vector.
   * @param fromIndex the source index of the first bit to copy.
   * @param toIndex   the source index after the last bit to copy.
   * @param dest      the destination bit vector.
   * @param destIndex the destination index of the first copied bit.
   *
   * @throws IndexOutOfBoundsException  
   *  for an illegal endpoint index value 
   *  <code>(fromIndex &lt; 0) || (toIndex &gt; src.length * 8) || (fromIndex &gt; toIndex)</code>.
   * @throws IndexOutOfBoundsException  
   *  if <code>(destIndex < 0) || (destIndex + (toIndex - fromIndex) > dest.length * 8)</code>.
   * @throws NullPointerException       
   *  if <code>(src == null) || (dest == null)</code>.
   *
   * @since JaXLib 1.0
   */
    public static void copy(byte[] src, int fromIndex, int toIndex, byte[] dest, int destIndex) {
        CheckArg.copyRangeTo(src.length << Bytes.ADDRESS_BITS, fromIndex, toIndex, dest.length << Bytes.ADDRESS_BITS, destIndex);
        int count = toIndex - fromIndex;
        if ((count == 0) || ((src == dest) && (destIndex == fromIndex))) return;
        if (count < Bytes.BITS) {
            setByte(dest, destIndex, destIndex + count, getByte(src, fromIndex, toIndex));
            return;
        }
        final boolean forward = !((src == dest) && (destIndex > fromIndex) && (destIndex < toIndex));
        int fromUnit = fromIndex >> Bytes.ADDRESS_BITS;
        int fromOffs = fromIndex & Bytes.BIT_INDEX_MASK;
        int toOffs = (fromIndex + count) & Bytes.BIT_INDEX_MASK;
        if (forward && (fromOffs != 0)) {
            final int d = Bytes.BITS - fromOffs;
            setByte(dest, destIndex, destIndex + d, getByte(src, fromIndex, fromIndex + d));
            count -= d;
            fromIndex += d;
            destIndex += d;
            if (count < Bytes.BITS) {
                setByte(dest, destIndex, destIndex + count, getByte(src, fromIndex, toIndex));
                return;
            }
            fromOffs = 0;
            fromUnit++;
        }
        if ((toOffs != 0) && (!forward || (fromIndex < destIndex) || (fromIndex >= destIndex + count))) {
            setByte(dest, destIndex + count - toOffs, destIndex + count, getByte(src, toIndex - toOffs, toIndex));
            count -= toOffs;
            toIndex -= toOffs;
            if (count < Bytes.BITS) {
                setByte(dest, destIndex, destIndex + count, getByte(src, fromIndex, toIndex));
                return;
            }
            toOffs = 0;
        }
        if ((fromOffs == 0) && (toOffs == 0) && ((destIndex & Bytes.BIT_INDEX_MASK) == 0)) {
            ByteArrays.copyFast(src, fromUnit, fromUnit + (count >> Bytes.ADDRESS_BITS), dest, destIndex >> Bytes.ADDRESS_BITS);
        } else if (forward) {
            for (int hiFromIndex = toIndex - Bytes.BITS; fromIndex < hiFromIndex; ) {
                setByte(dest, destIndex, src[fromUnit++]);
                fromIndex += Bytes.BITS;
                destIndex += Bytes.BITS;
            }
            setByte(dest, destIndex, destIndex + toIndex - fromIndex, getByte(src, fromIndex, toIndex));
        } else {
            int destToIndex = destIndex + count;
            int destToOffset = destToIndex & Bytes.BIT_INDEX_MASK;
            if (destToOffset != 0) {
                setByte(src, destToIndex - destToOffset, destToIndex, getByte(src, toIndex - destToOffset, toIndex));
                destToIndex -= destToOffset;
                toIndex -= destToOffset;
            }
            int destUnit = destToIndex >> Bytes.ADDRESS_BITS;
            for (int loToIndex = fromIndex + Bytes.BITS; toIndex >= loToIndex; ) {
                toIndex -= Bytes.BITS;
                src[--destUnit] = (byte) getByte(src, toIndex);
            }
            setByte(src, destIndex, destIndex + (toIndex - fromIndex), getByte(src, fromIndex, toIndex));
        }
    }

    /**
   * Counts the bits in specified bitvector which are set to specified state.
   *
   * @param   e         The bit to count.
   * @param   bits      The bitvector to search in.
   *
   * @throws  NullPointerException        if <tt>bits == null</tt>.
   *
   * @since JaxLib 1.0
   */
    public static int count(byte[] bits, boolean e) {
        return countUp(bits, 0, bits.length << Bytes.ADDRESS_BITS, e, -1);
    }

    /**
   * Counts the bits in specified subsequence of specified bitvector which are set to specified state.
   *
   * @param   bits      The bitvector to search in.
   * @param   fromIndex The first index to look in.
   * @param   toIndex   The lastIndex (exclusive) to look in.
   * @param   e         The bit to count.
   *
   * @throws  IndexOutOfBoundsException   for an illegal endpoint index value <tt>(fromIndex &lt; 0 || toIndex &gt; bits.length * 8 || fromIndex &gt; toIndex)</tt>.
   * @throws  NullPointerException        if <tt>bits == null</tt>.
   *
   * @since JaxLib 1.0
   */
    public static int count(byte[] bits, int fromIndex, int toIndex, boolean e) {
        return countUp(bits, fromIndex, toIndex, e, -1);
    }

    /**
   * Counts a maximum amount of bits in specified bitvector which are set to specified state.
   *
   * @param   bits      The bitvector to search in.
   * @param   e         The bit to count.
   * @param   maxCount  The maximum count of bits to search for, or <tt>-1</tt> to search all.
   *
   * @return  Count of bits in bitvector which are in state <tt>e</tt>, but not more than <tt>maxCount</tt>.
   *
   * @throws  IllegalArgumentException    if <tt>maxCount &lt; -1</tt>.
   * @throws  NullPointerException        if <tt>bits == null</tt>.
   *
   * @since JaxLib 1.0
   */
    public static int countUp(byte[] bits, boolean e, int maxCount) {
        return countUp(bits, 0, bits.length << Bytes.ADDRESS_BITS, e, maxCount);
    }

    /**
   * Counts a maximum amount of bits in specified subsequence of specified bitvector which are set to specified state.
   *
   * @param   bits      The bitvector to search in.
   * @param   fromIndex The first index to look in.
   * @param   toIndex   The lastIndex (exclusive) to look in.
   * @param   e         The bit to count.
   * @param   maxCount  The maximum count of bits to search for, or <tt>-1</tt> to search all.
   *
   * @return  Count of bits in bitvector which are in state <tt>e</tt>, but not more than <tt>maxCount</tt>.
   *
   * @throws  IllegalArgumentException    if <tt>maxCount &lt; -1</tt>.
   * @throws  IndexOutOfBoundsException   for an illegal endpoint index value <tt>(fromIndex &lt; 0 || toIndex &gt; bits.length * 8 || fromIndex &gt; toIndex)</tt>.
   * @throws  NullPointerException        if <tt>bits == null</tt>.
   *
   * @since JaxLib 1.0
   */
    public static int countUp(byte[] bits, int fromIndex, int toIndex, boolean e, int maxCount) {
        CheckArg.maxCount(maxCount);
        checkRange(bits, fromIndex, toIndex);
        if (maxCount == 0) return 0;
        if (maxCount < 0) maxCount = Integer.MAX_VALUE;
        toIndex--;
        int u = fromIndex >> Bytes.ADDRESS_BITS;
        int fromOffset = fromIndex & Bytes.BIT_INDEX_MASK;
        int toOffset = (toIndex & Bytes.BIT_INDEX_MASK) + 1;
        int toUnit = toIndex >> Bytes.ADDRESS_BITS;
        int count = 0;
        if (u == toUnit) {
            count = Bytes.bitCount((bits[u] & 0xff) & Bytes.bitMask(fromOffset, toOffset));
            if (!e) count = (toOffset - fromOffset) - count;
        } else {
            count = Bytes.bitCount((bits[u] & 0xff) & Bytes.bitMask(fromOffset, Bytes.BITS));
            if (e) {
                while ((++u < toUnit) && (count < maxCount)) count += Bytes.bitCount(bits[u]);
                count += Bytes.bitCount((bits[toUnit] & 0xff) & Bytes.bitMask(0, toOffset));
            } else {
                count = (Bytes.BITS - fromOffset) - count;
                while ((++u < toUnit) && (count < maxCount)) count += Bytes.bitCount(~(bits[u] & 0xff));
                count += toOffset - Bytes.bitCount((bits[toUnit] & 0xff) & Bytes.bitMask(0, toOffset));
            }
        }
        return Math.min(maxCount, count);
    }

    /**
   * Returns a bitvector containing same bits in same order as specified one, but with at least specified minimum capacity.
   *
   * @throws  IllegalArgumentException  if <tt>minCapacity < 0</tt>.
   * @throws  NullPointerException      if <tt>data == null</tt>.
   *
   * @see #ensureCapacity(byte[],int,float)
   *
   * @since JaXLib 1.0
   */
    public static byte[] ensureCapacity(byte[] bits, int minCapacity) {
        CheckArg.count(minCapacity);
        minCapacity = (minCapacity >> Bytes.ADDRESS_BITS) + 1;
        if (minCapacity > bits.length) {
            byte[] b = new byte[minCapacity];
            ByteArrays.copy(bits, b);
            return b;
        } else return bits;
    }

    /**
   * Returns a bitvector containing same bits in same order as specified one, but with at least specified minimum capacity.
   *
   * @throws  IllegalArgumentException  if <tt>minCapacity < 0 || growFactor < 0</tt>.
   * @throws  NullPointerException      if <tt>data == null</tt>.
   *
   * @see #ensureCapacity(byte[],int)
   *
   * @since JaXLib 1.0
   */
    public static byte[] ensureCapacity(byte[] bits, int minCapacity, float growFactor) {
        CheckArg.count(minCapacity);
        CheckArg.growFactor(growFactor);
        minCapacity = (minCapacity >> Bytes.ADDRESS_BITS) + 1;
        if (minCapacity > bits.length) {
            byte[] b = new byte[(int) (minCapacity + (minCapacity * growFactor))];
            ByteArrays.copy(bits, b);
            return b;
        } else return bits;
    }

    /**
   * Sets the bits in specified bitvector to specified state.
   *
   * @param e         the state of the bits to fill the vector with.
   * @param bits      the bitvector.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void fill(byte[] bits, boolean e) {
        fill(bits, 0, bits.length << Bytes.ADDRESS_BITS, e);
    }

    /**
   * Sets the bits in specified subsequence of specified bitvector to specified state.
   *
   * @param e         the state to set the bits to.
   * @param bits      the bitvector.
   * @param fromIndex the index of the first bit to replace.
   * @param toIndex   the index after the last bit to replace.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   * @throws IndexOutOfBoundsException if an endpoint index value is out of range <tt>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void fill(byte[] bits, int fromIndex, int toIndex, boolean e) {
        checkRange(bits, fromIndex, toIndex);
        if (fromIndex == toIndex) return;
        int fromOffset = fromIndex & Bytes.BIT_INDEX_MASK;
        if (fromOffset != 0) {
            int a = Math.min(toIndex, fromIndex + Bytes.BITS - fromOffset);
            setByte(bits, fromIndex, a, e ? 0xff : 0);
            if (a == toIndex) return;
            fromIndex = a;
        }
        int toOffset = toIndex & Bytes.BIT_INDEX_MASK;
        if (toOffset != 0) {
            setByte(bits, toIndex - toOffset, toIndex, e ? 0xff : 0);
            toIndex -= toOffset;
            if (fromIndex == toIndex) return;
        }
        ByteArrays.fillFast(bits, fromIndex >> Bytes.ADDRESS_BITS, toIndex >> Bytes.ADDRESS_BITS, e ? (byte) 0xff : 0);
    }

    /**
   * Sets in specified bitvector the bit at specified index to the complement of its current value.
   *
   * @throws ArrayIndexOutOfBoundsException 
   *  if the index is out of range <code>(index &lt; 0) || (index &gt;= (bits.length * 8))</code>.
   * @throws NullPointerException      
   *   if <code>bits == null</code>.
   *
   * @since JaXLib 1.0
   */
    public static void flip(byte[] bits, int index) {
        bits[index >> Bytes.ADDRESS_BITS] = (byte) ((bits[index >> Bytes.ADDRESS_BITS] & 0xff) ^ (1 << (index & Bytes.BIT_INDEX_MASK)));
    }

    /**
   * Sets in specified bitvector all bits to the complement of their current value.
   *
   * @param bits      the bitvector.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void flip(byte[] bits) {
        flip(bits, 0, bits.length << Bytes.ADDRESS_BITS);
    }

    /**
   * Sets in specified subsequence of specified bitvector all bits to the complement of their current value.
   *
   * @param bits      the bitvector.
   * @param fromIndex the index of the first bit to flip.
   * @param toIndex   the index after the last bit to flip.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   * @throws IndexOutOfBoundsException if an endpoint index value is out of range <tt>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void flip(byte[] bits, int fromIndex, int toIndex) {
        checkRange(bits, fromIndex, toIndex);
        while (--toIndex >= fromIndex) flip(bits, toIndex);
    }

    /**
   * Returns the bit in specified bitvector at specified bitindex.
   *
   * @return the state of the bit.
   *
   * @param bits  the bitvector.
   * @param index index of the bit to return.
   * 
   * @throws ArrayIndexOutOfBoundsException 
   *  if the index is out of range <code>(index &lt; 0) || (index &gt;= (bits.length * 8))</code>.
   * @throws NullPointerException      
   *   if <code>bits == null</code>.
   *
   * @since JaXLib 1.0
   */
    public static boolean get(byte[] bits, int index) {
        return ((bits[index >> Bytes.ADDRESS_BITS] & 0xff) & (1 << (index & Bytes.BIT_INDEX_MASK))) != 0;
    }

    /**
   * Sets the bit in specified bitvector at specified bitindex to specified state and returns the old state.
   *
   * @return the state of the bit before this call.
   *
   * @param bits  the bitvector.
   * @param index index of the bit to change.
   * @param e     the new state for the bit.
   * 
   * @throws ArrayIndexOutOfBoundsException 
   *  if the index is out of range <code>(index &lt; 0) || (index &gt;= (bits.length * 8))</code>.
   * @throws NullPointerException      
   *   if <code>bits == null</code>.
   *
   * @since JaXLib 1.0
   */
    public static boolean getAndSet(byte[] bits, int index, boolean e) {
        int unitIndex = index >> Bytes.ADDRESS_BITS;
        int bit = 1 << (index & Bytes.BIT_INDEX_MASK);
        int unit = bits[unitIndex] & 0xff;
        boolean old = (unit & bit) != 0;
        if (e != old) bits[unitIndex] = (byte) (e ? (unit | bit) : (unit ^ bit));
        return old;
    }

    /**
   * Returns <tt>8</tt> bits out of the specified bitvector beginning at specified bit index.
   *
   * @return an unsigned int value containing the requested bits.
   *
   * @param bits      the bitvector.
   * @param index     the index of the first bit to return.
   *
   * @throws ArrayIndexOutOfBoundsException 
   *  if <code>index >= bits.length * 8</code>.
   * @throws NullPointerException      
   *  if <code>bits == null</code>.
   *
   * @since JaXLib 1.0
   */
    public static int getByte(byte[] bits, int index) {
        int unit = index >> Bytes.ADDRESS_BITS;
        int offset = index & Bytes.BIT_INDEX_MASK;
        return (offset == 0) ? bits[unit] : (((bits[unit] & 0xff) >>> offset) | ((bits[unit + 1] & 0xff) << (Bytes.BITS - offset)));
    }

    /**
   * Returns <tt>8</tt> bits representing specified subrange of specified bitvector.
   * All other bits of the return value are set to 0.
   *
   * @return an unsigned int value containing the requested bits.
   *
   * @param bits      the bitvector.
   * @param fromIndex the index of the first bit to return.
   * @param toIndex   the index after the last bit to return.
   *
   * @throws ArrayIndexOutOfBoundsException 
   *  if <code>fromIndex >= bits.length * 8</code>.
   * @throws NullPointerException      
   *  if <code>bits == null</code>.
   *
   * @since JaXLib 1.0
   */
    public static int getByte(byte[] bits, int fromIndex, int toIndex) {
        if (fromIndex == toIndex) return 0;
        toIndex--;
        int fromUnit = fromIndex >> Bytes.ADDRESS_BITS;
        int toUnit = toIndex >> Bytes.ADDRESS_BITS;
        int fromOffset = fromIndex & Bytes.BIT_INDEX_MASK;
        return ((fromUnit == toUnit) ? (((bits[fromUnit] & 0xff) & Bytes.bitMask(fromOffset, (toIndex & Bytes.BIT_INDEX_MASK) + 1)) >>> fromOffset) : (((bits[fromUnit] & 0xff) >>> fromOffset) | (bits[toUnit] << (Bytes.BITS - fromOffset))));
    }

    /**
   * Returns the index of the first bit in specified bitvector which is set to specified state.
   *
   * @return the index, or <tt>-1</tt> if no bit with specified state has been found.
   *
   * @param bits      the bitvector.
   * @param e         the bit to search.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static int indexOf(byte[] bits, boolean e) {
        return indexOf(bits, 0, bits.length << Bytes.ADDRESS_BITS, e);
    }

    /**
   * Returns the index of the first bit in specified subsequence of specified bitvector which is set to specified state.
   *
   * @return the index, or <tt>-1</tt> if no bit with specified state has been found.
   *
   * @param bits      the bitvector.
   * @param fromIndex the index of the first bit.
   * @param toIndex   the index after the last bit.
   * @param e         the bit to search.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   * @throws IndexOutOfBoundsException if an endpoint index value is out of range <tt>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</tt>.
   *
   * @since JaXLib 1.0
   */
    public static int indexOf(byte[] bits, int fromIndex, int toIndex, boolean e) {
        checkRange(bits, fromIndex, toIndex);
        if (fromIndex == toIndex) return -1;
        if (e) {
            int u = fromIndex >> Bytes.ADDRESS_BITS;
            int toUnitIndex = (toIndex - 1) >> Bytes.ADDRESS_BITS;
            int testIndex = fromIndex & Bytes.BIT_INDEX_MASK;
            int unit = (bits[u] & 0xff) >> testIndex;
            if (unit == 0) {
                testIndex = 0;
                while ((unit == 0) && (u < toUnitIndex)) unit = bits[++u] & 0xff;
                if (unit == 0) return -1;
            }
            return ((u << Bytes.ADDRESS_BITS) + testIndex + Bytes.numberOfTrailingZeros(unit));
        } else {
            while (fromIndex < toIndex) {
                if (!get(bits, fromIndex)) return fromIndex;
                fromIndex++;
            }
            return -1;
        }
    }

    /**
   * Returns the index of the last bit in specified bitvector which is set to specified state.
   *
   * @return the index, or <tt>-1</tt> if no bit with specified state has been found.
   *
   * @param bits      the bitvector.
   * @param e         the bit to search.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static int lastIndexOf(byte[] bits, boolean e) {
        return lastIndexOf(bits, 0, bits.length << Bytes.ADDRESS_BITS, e);
    }

    /**
   * Returns the index of the last bit in specified subsequence of specified bitvector which is set to specified state.
   *
   * @return the index, or <tt>-1</tt> if no bit with specified state has been found.
   *
   * @param bits      the bitvector.
   * @param fromIndex the index of the first bit.
   * @param toIndex   the index after the last bit.
   * @param e         the bit to search.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   * @throws IndexOutOfBoundsException if an endpoint index value is out of range <tt>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</tt>.
   *
   * @since JaXLib 1.0
   */
    public static int lastIndexOf(byte[] bits, int fromIndex, int toIndex, boolean e) {
        checkRange(bits, fromIndex, toIndex);
        if (fromIndex == toIndex) return -1;
        while (--toIndex >= fromIndex) {
            if (get(bits, toIndex) == e) return toIndex;
        }
        return -1;
    }

    /**
   * Reverses the order of bits in specified bitvector.
   *
   * @param bits      the bitvector.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void reverse(byte[] bits) {
        reverse(bits, 0, bits.length << Bytes.ADDRESS_BITS);
    }

    /**
   * Reverses the order of bits in specified subsequence of specified bitvector.
   *
   * @param bits      the bitvector.
   * @param fromIndex the index of the first bit.
   * @param toIndex   the index after the last bit.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   * @throws IndexOutOfBoundsException if an endpoint index value is out of range <tt>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void reverse(byte[] bits, int fromIndex, int toIndex) {
        checkRange(bits, fromIndex, toIndex);
        while (fromIndex < --toIndex) ByteBitArrays.swap(bits, fromIndex++, toIndex);
    }

    /**
   * Rotates the bits in the specified range of specified bitvector by the specified distance.
   * After calling this method, the bit at index <tt>i</tt> will be the bit previously at index <tt>(i - distance)</tt> mod <tt>toIndex-fromIndex</tt>, 
   * for all values of <tt>i</tt> between <tt>fromIndex</tt> and <tt>toIndex-1</tt>, inclusive. 
   * <p>
   * For example, suppose <tt>bits</tt> comprises<tt> [1, 0, 0, 0, 0]</tt>.<br>
   * After invoking <tt>rotate(1, bits, 0, bits.length * 8)</tt> (or <tt>rotate(-4, bits, 0, bits.length * 8)</tt>), 
   * <tt>bits</tt> will comprise <tt>[0, 1, 0, 0, 0]</tt>.
   * </p><p>
   * To move more than one bit forward, increase the absolute value of the rotation distance.  
   * To move bits backward, use a positive shift distance.
   * </p>
   *
   * @param bits      the bitvector.
   * @param fromIndex index of the first bit of the subsequence to be rotated.
   * @param toIndex   index after the last bit of the subsequence to be rotated.
   * @param distance  the distance to rotate the specified subsequence. There are no constraLongs on this value; 
   *                  it may be zero, negative, or greater than <tt>data.length</tt>.
   *
   * @throws NullPointerException      if <tt>bits == null</tt>.
   * @throws IndexOutOfBoundsException if an endpoint index value is out of range <tt>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void rotate(byte[] bits, int fromIndex, int toIndex, int distance) {
        checkRange(bits, fromIndex, toIndex);
        int count = toIndex - fromIndex;
        if ((count < 2) || (distance == 0)) return;
        distance = distance % count;
        if (distance < 0) distance += count;
        if (distance == 0) return;
        if ((distance % Bytes.BITS == 0) && ((fromIndex & Bytes.BIT_INDEX_MASK) == 0) && ((toIndex & Bytes.BIT_INDEX_MASK) == 0)) {
            ByteArrays.rotate(bits, fromIndex >> Bytes.ADDRESS_BITS, toIndex >> Bytes.ADDRESS_BITS, distance >> Bytes.ADDRESS_BITS);
        } else {
            for (int cycleStart = 0, nMoved = 0; nMoved != count; cycleStart++) {
                boolean displaced = ByteBitArrays.get(bits, cycleStart + fromIndex);
                int i = cycleStart;
                do {
                    i += distance;
                    if (i >= count) i -= count;
                    displaced = ByteBitArrays.getAndSet(bits, i + fromIndex, displaced);
                    nMoved++;
                } while (i != cycleStart);
            }
        }
    }

    /**
   * Sets the bit in specified bitvector at specified bitindex.
   *
   * @param bits  the bitvector.
   * @param index index of the bit to set.
   * 
   * @throws ArrayIndexOutOfBoundsException 
   *  if the index is out of range <code>(index &lt; 0) || (index &gt;= (bits.length * 8))</code>.
   * @throws NullPointerException      
   *   if <code>bits == null</code>.
   *
   * @since JaXLib 1.0
   */
    public static void set(byte[] bits, int index) {
        bits[index >> Bytes.ADDRESS_BITS] = (byte) ((bits[index >> Bytes.ADDRESS_BITS] & 0xff) | (1 << (index & Bytes.BIT_INDEX_MASK)));
    }

    /**
   * Sets the bit in specified bitvector at specified bitindex.
   *
   * @param bits  the bitvector.
   * @param index index of the bit to change.
   * @param e     the new state for the bit.
   * 
   * @throws ArrayIndexOutOfBoundsException 
   *  if the index is out of range <code>(index &lt; 0) || (index &gt;= (bits.length * 8))</code>.
   * @throws NullPointerException      
   *   if <code>bits == null</code>.
   *
   * @since JaXLib 1.0
   */
    public static void set(byte[] bits, int index, boolean e) {
        if (e) {
            bits[index >> Bytes.ADDRESS_BITS] = (byte) ((bits[index >> Bytes.ADDRESS_BITS] & 0xff) | (1 << (index & Bytes.BIT_INDEX_MASK)));
        } else {
            bits[index >> Bytes.ADDRESS_BITS] = (byte) ((bits[index >> Bytes.ADDRESS_BITS] & 0xff) ^ (1 << (index & Bytes.BIT_INDEX_MASK)));
        }
    }

    /**
   * Copies the <tt>8</tt> bits of specified value to specified bitvector at specified index.
   * All other bits stay unaffected.
   *
   * @param bits      the bitvector.
   * @param index     the index in the bitvector where to copy the bits to.
   * @param v         the value to be copied into the bitvector.
   * 
   * @throws ArrayIndexOutOfBoundsException 
   *  if <code>index >= bits.length * 8</code>.
   * @throws NullPointerException      
   *  if <code>bits == null</code>.
   *
   * @since JaXLib 1.0
   */
    public static void setByte(byte[] bits, int index, int v) {
        int unit = index >> Bytes.ADDRESS_BITS;
        int offset = index & Bytes.BIT_INDEX_MASK;
        if (offset == 0) {
            bits[unit] = (byte) (v & 0xff);
        } else {
            bits[unit] = (byte) (((bits[unit] & 0xff) & Bytes.bitMask(0, offset)) | ((v << offset) & 0xff));
            bits[unit + 1] = (byte) (((bits[unit + 1] & 0xff) & Bytes.bitMask(offset, Bytes.BITS)) | ((v >>> (Bytes.BITS - offset)) & 0xff));
        }
    }

    /**
   * Copies the bits of specified value to specified bitvector in specified subrange.
   * All other bits stay unaffected.
   *
   * @param bits      the bitvector.
   * @param fromIndex index of the first bit to change.
   * @param toIndex   index after the last bit to change.
   * @param v         the value to be copied into the bitvector.
   * 
   * @throws ArrayIndexOutOfBoundsException 
   *  if an endpoint index value is out of range 
   *  <code>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</code>.
   * @throws NullPointerException      
   *  if <code>bits == null</code>.
   *
   * @since JaXLib 1.0
   */
    public static void setByte(byte[] bits, int fromIndex, int toIndex, int v) {
        if (fromIndex != toIndex) {
            int unit = fromIndex >> Bytes.ADDRESS_BITS;
            int offset = fromIndex & Bytes.BIT_INDEX_MASK;
            int c = toIndex - fromIndex;
            v &= Bytes.bitMask(0, c);
            if (offset == 0) {
                if (c == Bytes.BITS) bits[unit] = (byte) v; else bits[unit] = (byte) ((((bits[unit] & 0xff) >>> c) << c) | ((v << (Bytes.BITS - c)) >> (Bytes.BITS - c)));
            } else if (offset + c <= Bytes.BITS) {
                int u = bits[unit] & 0xff;
                bits[unit] = (byte) ((u & Bytes.bitMask(0, offset)) | ((v & Bytes.bitMask(0, c)) << offset) | (u & Bytes.bitMask(offset + c, Bytes.BITS)));
            } else {
                bits[unit] = (byte) (((bits[unit] & 0xff) & Bytes.bitMask(0, offset)) | (v << offset));
                bits[unit + 1] = (byte) (((bits[unit + 1] & 0xff) & Bytes.bitMask(c - (Bytes.BITS - offset), Bytes.BITS)) | ((v & Bytes.bitMask(0, c)) >>> Bytes.BITS - offset));
            }
        }
    }

    /**
   * Replaces in specified bitvector the bit at <tt>index1</tt> with the bit at <tt>index2</tt> and vice-versa.
   *
   * @param bits    the bitvector.
   * @param index1  the index of the first bit.
   * @param index2  the index of the second bit.
   *
   * @throws NullPointerException           if <tt>bits == null</tt>.
   * @throws ArrayIndexOutOfBoundsException if one of the indices is invalid.
   *
   * @since JaXLib 1.0
   */
    public static void swap(byte[] bits, int index1, int index2) {
        int i1 = index1 >> Bytes.ADDRESS_BITS;
        int i2 = index2 >> Bytes.ADDRESS_BITS;
        int b1 = 1 << (index1 & Bytes.BIT_INDEX_MASK);
        int b2 = 1 << (index2 & Bytes.BIT_INDEX_MASK);
        if (i1 == i2) {
            int a = bits[i1];
            int b = ((a & b1) != 0) ? (a | b2) : (a & ~b2);
            bits[i1] = (byte) (((a & b2) != 0) ? (b | b1) : (b & ~b1));
        } else {
            int u1 = bits[i1];
            int u2 = bits[i2];
            bits[i1] = (byte) (((u2 & b2) != 0) ? (u1 | b1) : (u1 & ~b1));
            bits[i2] = (byte) (((u1 & b1) != 0) ? (u2 | b2) : (u2 & ~b2));
        }
    }

    /**
   * Replaces element of specified first array at <tt>index1</tt> with element of specified second array at <tt>index2</tt>, and vice-versa.
   *
   * @throws  IndexOutOfBoundsException   for an illegal index value <tt>(index &lt; 0 || index &ge; array.length * 8)</tt>. 
   * @throws  NullPointerException        if <tt>(a == null) || (b == null)</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void swap(byte[] a, int index1, byte[] b, int index2) {
        int i1 = index1 >> Bytes.ADDRESS_BITS;
        int i2 = index2 >> Bytes.ADDRESS_BITS;
        int b1 = 1 << (index1 & Bytes.BIT_INDEX_MASK);
        int b2 = 1 << (index2 & Bytes.BIT_INDEX_MASK);
        if ((i1 != i2) || (a != b)) {
            int u1 = a[i1];
            int u2 = b[i2];
            a[i1] = (byte) (((u2 & b2) != 0) ? (u1 | b1) : (u1 & ~b1));
            b[i2] = (byte) (((u1 & b1) != 0) ? (u2 | b2) : (u2 & ~b2));
        } else {
            int x = a[i1];
            int y = (byte) (((x & b1) != 0) ? (x | b2) : (x & ~b2));
            a[i1] = (byte) (((x & b2) != 0) ? (y | b1) : (y & ~b1));
        }
    }

    /**
   * Swaps bits[index1 .. (index1 + count-1)] with bits[index2 .. (index2 + count-1)].
   *
   * @throws IllegalArgumentException       if <tt>count < 0</tt>
   * @throws ArrayIndexOutOfBoundsException if one of the indices is invalid.
   * @throws NullPointerException           if <tt>bits == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void swapRanges(byte[] bits, int index1, int index2, int count) {
        CheckArg.swapRanges(count, bits.length << Bytes.ADDRESS_BITS, index1, index2);
        if (index1 != index2) {
            while (count-- > 0) swap(bits, index1++, index2++);
        }
    }

    /**
   * Swaps a[index1 .. (index1 + count-1)] with b[index2 .. (index2 + count-1)].
   *
   * @throws IllegalArgumentException   if <tt>count < 0</tt>
   * @throws IndexOutOfBoundsException  for an illegal index value <tt>(index &lt; 0 || index &ge; array.length * 8)</tt>. 
   * @throws IndexOutOfBoundsException  if <tt>(index1 + count &gt; a.length * 8) || (index2 + count &gt; b.length * 8)</tt>.
   * @throws NullPointerException       if <tt>(a == null) || (b == null)</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void swapRanges(byte[] a, int index1, byte[] b, int index2, int count) {
        if (a == b) {
            swapRanges(a, index1, index2, count);
        } else {
            CheckArg.swapRanges(count, a.length << Bytes.ADDRESS_BITS, index1, b.length << Bytes.ADDRESS_BITS, index2);
            while (count-- > 0) swap(a, index1++, b, index2++);
        }
    }

    /**
   * Copies specified bitvector to a newly allocated boolean array.
   *
   * @throws NullPointerException       if <tt>bits == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static boolean[] toBooleanArray(byte[] bits) {
        return toBooleanArray(bits, 0, bits.length * Bytes.BITS);
    }

    /**
   * Copies specified subsequence of specified bitvector to a newly allocated boolean array.
   *
   * @throws IndexOutOfBoundsException  for an illegal endpoint index value <tt>(fromIndex &lt; 0 || toIndex &gt; bits.length * 8 || fromIndex &gt; toIndex)</tt>.
   * @throws NullPointerException       if <tt>bits == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static boolean[] toBooleanArray(byte[] bits, int fromIndex, int toIndex) {
        checkRange(bits, fromIndex, toIndex);
        boolean[] a = new boolean[toIndex - fromIndex];
        toBooleanArray(bits, fromIndex, toIndex, a, 0);
        return a;
    }

    /**
   * Copies all bits of source bitvector to destination boolean array.
   * Source and destination indices are zero.
   *
   * @throws IndexOutOfBoundsException  if <tt>dest.length < src.length * 8</tt>.
   * @throws NullPointerException       if <tt>src == null || dest == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void toBooleanArray(byte[] src, boolean[] dest) {
        toBooleanArray(src, 0, src.length << Bytes.ADDRESS_BITS, dest, 0);
    }

    /**
   * Copies specified subsequence of specified bitvector to specified boolean array at specified index.
   *
   * @throws IndexOutOfBoundsException  for an illegal endpoint index value <tt>(fromIndex &lt; 0 || toIndex &gt; bits.length * 8 || fromIndex &gt; toIndex)</tt>.
   * @throws IndexOutOfBoundsException  if <tt>destIndex < 0 || destIndex > dest.length</tt>.
   * @throws IndexOutOfBoundsException  if <tt>destIndex + (toIndex - fromIndex) > dest.length</tt>.
   * @throws NullPointerException       if <tt>bits == null || dest == null</tt>.
   *
   * @since JaXLib 1.0
   */
    public static void toBooleanArray(byte[] bits, int fromIndex, int toIndex, boolean[] dest, int destIndex) {
        CheckArg.copyRangeTo(bits.length << Bytes.ADDRESS_BITS, fromIndex, toIndex, dest.length, destIndex);
        if (fromIndex == toIndex) return;
        int fromUnit = fromIndex >> Bytes.ADDRESS_BITS;
        int toUnit = (toIndex - 1) >> Bytes.ADDRESS_BITS;
        int fromOffset = fromIndex & Bytes.BIT_INDEX_MASK;
        int toOffset = (toIndex - 1) & Bytes.BIT_INDEX_MASK;
        if (fromUnit == toUnit) {
            int u = ByteBitArrays.getByte(bits, fromIndex, toIndex) & 0xff;
            for (int i = 0, hi = toIndex - fromIndex; i < hi; ) dest[destIndex++] = (u & (1 << i++)) != 0;
        } else {
            int bitIndex = fromIndex;
            int a = (bitIndex + toOffset - fromOffset) + 1;
            int u = ByteBitArrays.getByte(bits, bitIndex, a) & 0xff;
            for (int i = 0, hi = a - bitIndex; i < hi; ) dest[destIndex++] = (u & (1 << i++)) != 0;
            bitIndex += toOffset - fromOffset + 1;
            fromUnit++;
            if (toOffset < Bytes.BIT_INDEX_MASK) toUnit--;
            for (int ui = fromUnit; ui <= toUnit; ) {
                u = bits[ui++] & 0xff;
                for (int i = 0; i < Bytes.BITS; ) dest[destIndex++] = (u & (1 << i++)) != 0;
            }
            if (fromUnit <= toUnit) bitIndex += (toUnit - fromUnit + 1) * Bytes.BITS;
            if (toOffset < Bytes.BIT_INDEX_MASK) {
                u = ByteBitArrays.getByte(bits, bitIndex, toIndex) & 0xff;
                for (int i = 0, hi = toIndex - bitIndex; i < hi; ) dest[destIndex++] = (u & (1 << i++)) != 0;
            }
        }
    }

    /**
   * Performs a logical <b>AND</b> operation.
   * <p>
   * The result is stored in the left hand array.
   * This method operates correctly even if the specified arrays are identical and the specified regions are
   * overlapping.
   * </p>
   *
   * @param bits       the left side operand bits.
   * @param fromIndex  the index in <i>bits</i> of the first bit.
   * @param toIndex    the index in <i>bits</i> after the last bit.
   * @param bbits      the right side operand bits.
   * @param bFromIndex the index in <i>bbits</i> of the first bit.
   *
   * @throws IndexOutOfBoundsException  
   *  for an illegal endpoint index value 
   *  <code>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</code>.
   * @throws IndexOutOfBoundsException  
   *  if <code>(bFromIndex < 0) || (bFromIndex + (toIndex - fromIndex) > bbits.length * 8)</code>.
   * @throws NullPointerException       
   *  if <code>(bits == null) || (bbits == null)</code>.
   *
   * @since JaXLib 1.0
   */
    public static void and(byte[] bits, int fromIndex, int toIndex, byte[] bbits, int bFromIndex) {
        CheckArg.copyRangeTo(bits.length << Bytes.ADDRESS_BITS, fromIndex, toIndex, bbits.length << Bytes.ADDRESS_BITS, bFromIndex);
        if ((bits != bbits) || (fromIndex != bFromIndex)) andImpl(bbits, bFromIndex, bFromIndex + toIndex - fromIndex, bits, fromIndex);
    }

    /**
   * Performs a logical <b>AND NOT</b> operation.
   * <p>
   * The result is stored in the left hand array.
   * This method operates correctly even if the specified arrays are identical and the specified regions are
   * overlapping.
   * </p>
   *
   * @param bits       the left side operand bits.
   * @param fromIndex  the index in <i>bits</i> of the first bit.
   * @param toIndex    the index in <i>bits</i> after the last bit.
   * @param bbits      the right side operand bits.
   * @param bFromIndex the index in <i>bbits</i> of the first bit.
   *
   * @throws IndexOutOfBoundsException  
   *  for an illegal endpoint index value 
   *  <code>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</code>.
   * @throws IndexOutOfBoundsException  
   *  if <code>(bFromIndex < 0) || (bFromIndex + (toIndex - fromIndex) > bbits.length * 8)</code>.
   * @throws NullPointerException       
   *  if <code>(bits == null) || (bbits == null)</code>.
   *
   * @since JaXLib 1.0
   */
    public static void andNot(byte[] bits, int fromIndex, int toIndex, byte[] bbits, int bFromIndex) {
        CheckArg.copyRangeTo(bits.length << Bytes.ADDRESS_BITS, fromIndex, toIndex, bbits.length << Bytes.ADDRESS_BITS, bFromIndex);
        if ((bits == bbits) && (fromIndex == bFromIndex)) fill(bits, fromIndex, toIndex, false); else andNotImpl(bbits, bFromIndex, bFromIndex + toIndex - fromIndex, bits, fromIndex);
    }

    /**
   * Performs a logical <b>OR</b> operation.
   * <p>
   * The result is stored in the left hand array.
   * This method operates correctly even if the specified arrays are identical and the specified regions are
   * overlapping.
   * </p>
   *
   * @param bits       the left side operand bits.
   * @param fromIndex  the index in <i>bits</i> of the first bit.
   * @param toIndex    the index in <i>bits</i> after the last bit.
   * @param bbits      the right side operand bits.
   * @param bFromIndex the index in <i>bbits</i> of the first bit.
   *
   * @throws IndexOutOfBoundsException  
   *  for an illegal endpoint index value 
   *  <code>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</code>.
   * @throws IndexOutOfBoundsException  
   *  if <code>(bFromIndex < 0) || (bFromIndex + (toIndex - fromIndex) > bbits.length * 8)</code>.
   * @throws NullPointerException       
   *  if <code>(bits == null) || (bbits == null)</code>.
   *
   * @since JaXLib 1.0
   */
    public static void or(byte[] bits, int fromIndex, int toIndex, byte[] bbits, int bFromIndex) {
        CheckArg.copyRangeTo(bits.length << Bytes.ADDRESS_BITS, fromIndex, toIndex, bbits.length << Bytes.ADDRESS_BITS, bFromIndex);
        if ((bits != bbits) || (fromIndex != bFromIndex)) orImpl(bbits, bFromIndex, bFromIndex + toIndex - fromIndex, bits, fromIndex);
    }

    /**
   * Performs a logical <b>XOR</b> operation.
   * <p>
   * The result is stored in the left hand array.
   * This method operates correctly even if the specified arrays are identical and the specified regions are
   * overlapping.
   * </p>
   *
   * @param bits       the left side operand bits.
   * @param fromIndex  the index in <i>bits</i> of the first bit.
   * @param toIndex    the index in <i>bits</i> after the last bit.
   * @param bbits      the right side operand bits.
   * @param bFromIndex the index in <i>bbits</i> of the first bit.
   *
   * @throws IndexOutOfBoundsException  
   *  for an illegal endpoint index value 
   *  <code>(fromIndex &lt; 0) || (toIndex &gt; bits.length * 8) || (fromIndex &gt; toIndex)</code>.
   * @throws IndexOutOfBoundsException  
   *  if <code>(bFromIndex < 0) || (bFromIndex + (toIndex - fromIndex) > bbits.length * 8)</code>.
   * @throws NullPointerException       
   *  if <code>(bits == null) || (bbits == null)</code>.
   *
   * @since JaXLib 1.0
   */
    public static void xor(byte[] bits, int fromIndex, int toIndex, byte[] bbits, int bFromIndex) {
        CheckArg.copyRangeTo(bits.length << Bytes.ADDRESS_BITS, fromIndex, toIndex, bbits.length << Bytes.ADDRESS_BITS, bFromIndex);
        if ((bits == bbits) && (fromIndex == bFromIndex)) fill(bits, fromIndex, toIndex, false); else xorImpl(bbits, bFromIndex, bFromIndex + toIndex - fromIndex, bits, fromIndex);
    }

    private static void andByte(byte[] dest, int destIndex, int src) {
        int unit = destIndex >> Bytes.ADDRESS_BITS;
        int offs = destIndex & Bytes.BIT_INDEX_MASK;
        if (offs == 0) {
            dest[unit] = (byte) ((dest[unit] & 0xff) & src);
        } else {
            int v = (((dest[unit] & 0xff) >>> offs) | ((dest[unit + 1] & 0xff) << (Bytes.BITS - offs))) & src;
            dest[unit] = (byte) (((dest[unit] & 0xff) & Bytes.bitMask(0, offs)) | (v << offs));
            dest[unit + 1] = (byte) (((dest[unit + 1] & 0xff) & Bytes.bitMask(offs, Bytes.BITS)) | (v >>> (Bytes.BITS - offs)));
        }
    }

    private static void orByte(byte[] dest, int destIndex, int src) {
        int unit = destIndex >> Bytes.ADDRESS_BITS;
        int offs = destIndex & Bytes.BIT_INDEX_MASK;
        if (offs == 0) {
            dest[unit] = (byte) ((dest[unit] & 0xff) | src);
        } else {
            int v = (((dest[unit] & 0xff) >>> offs) | ((dest[unit + 1] & 0xff) << (Bytes.BITS - offs))) | src;
            dest[unit] = (byte) (((dest[unit] & 0xff) & Bytes.bitMask(0, offs)) | (v << offs));
            dest[unit + 1] = (byte) (((dest[unit + 1] & 0xff) & Bytes.bitMask(offs, Bytes.BITS)) | (v >>> (Bytes.BITS - offs)));
        }
    }

    private static void xorByte(byte[] dest, int destIndex, int src) {
        int unit = destIndex >> Bytes.ADDRESS_BITS;
        int offs = destIndex & Bytes.BIT_INDEX_MASK;
        if (offs == 0) {
            dest[unit] = (byte) ((dest[unit] & 0xff) ^ src);
        } else {
            int v = (((dest[unit] & 0xff) >>> offs) | ((dest[unit + 1] & 0xff) << (Bytes.BITS - offs))) ^ src;
            dest[unit] = (byte) (((dest[unit] & 0xff) & Bytes.bitMask(0, offs)) | (v << offs));
            dest[unit + 1] = (byte) (((dest[unit + 1] & 0xff) & Bytes.bitMask(offs, Bytes.BITS)) | (v >>> (Bytes.BITS - offs)));
        }
    }

    private static void andByte(byte[] dest, int destFromIndex, int destToIndex, byte[] src, int srcFromIndex, int srcToIndex) {
        setByte(dest, destFromIndex, destToIndex, getByte(dest, destFromIndex, destToIndex) & getByte(src, srcFromIndex, srcToIndex));
    }

    private static void andNotByte(byte[] dest, int destFromIndex, int destToIndex, byte[] src, int srcFromIndex, int srcToIndex) {
        setByte(dest, destFromIndex, destToIndex, getByte(dest, destFromIndex, destToIndex) & ~getByte(src, srcFromIndex, srcToIndex));
    }

    private static void orByte(byte[] dest, int destFromIndex, int destToIndex, byte[] src, int srcFromIndex, int srcToIndex) {
        setByte(dest, destFromIndex, destToIndex, getByte(dest, destFromIndex, destToIndex) | getByte(src, srcFromIndex, srcToIndex));
    }

    private static void xorByte(byte[] dest, int destFromIndex, int destToIndex, byte[] src, int srcFromIndex, int srcToIndex) {
        setByte(dest, destFromIndex, destToIndex, getByte(dest, destFromIndex, destToIndex) ^ getByte(src, srcFromIndex, srcToIndex));
    }

    /**
   * Nearly same code as copy(...)
   */
    private static void andImpl(byte[] src, int fromIndex, int toIndex, byte[] dest, int destIndex) {
        int count = toIndex - fromIndex;
        if ((count == 0) || ((src == dest) && (destIndex == fromIndex))) return;
        if (count < Bytes.BITS) {
            andByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
            return;
        }
        final boolean forward = !((src == dest) && (destIndex > fromIndex) && (destIndex < toIndex));
        int fromUnit = fromIndex >> Bytes.ADDRESS_BITS;
        int fromOffs = fromIndex & Bytes.BIT_INDEX_MASK;
        int toOffs = (fromIndex + count) & Bytes.BIT_INDEX_MASK;
        if (forward && (fromOffs != 0)) {
            final int d = Bytes.BITS - fromOffs;
            andByte(dest, destIndex, destIndex + d, src, fromIndex, fromIndex + d);
            count -= d;
            fromIndex += d;
            destIndex += d;
            if (count < Bytes.BITS) {
                andByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
                return;
            }
            fromOffs = 0;
            fromUnit++;
        }
        if ((toOffs != 0) && (!forward || (fromIndex < destIndex) || (fromIndex >= destIndex + count))) {
            andByte(dest, destIndex + count - toOffs, destIndex + count, src, toIndex - toOffs, toIndex);
            count -= toOffs;
            toIndex -= toOffs;
            if (count < Bytes.BITS) {
                andByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
                return;
            }
            toOffs = 0;
        }
        if ((fromOffs == 0) && (toOffs == 0) && ((destIndex & Bytes.BIT_INDEX_MASK) == 0)) {
            int toUnit = toIndex >> Bytes.ADDRESS_BITS;
            if (forward) {
                int destUnit = destIndex >> Bytes.ADDRESS_BITS;
                while (fromUnit < toUnit) dest[destUnit++] &= src[fromUnit++];
            } else {
                int destUnit = (destIndex + count) >> Bytes.ADDRESS_BITS;
                while (fromUnit < toUnit) src[--destUnit] &= src[--toUnit];
            }
        } else if (forward) {
            for (int hiFromIndex = toIndex - Bytes.BITS; fromIndex < hiFromIndex; ) {
                andByte(dest, destIndex, src[fromUnit++] & 0xff);
                fromIndex += Bytes.BITS;
                destIndex += Bytes.BITS;
            }
            andByte(dest, destIndex, destIndex + toIndex - fromIndex, src, fromIndex, toIndex);
        } else {
            int destToIndex = destIndex + count;
            int destToOffset = destToIndex & Bytes.BIT_INDEX_MASK;
            if (destToOffset != 0) {
                andByte(src, destToIndex - destToOffset, destToIndex, src, toIndex - destToOffset, toIndex);
                destToIndex -= destToOffset;
                toIndex -= destToOffset;
            }
            int destUnit = destToIndex >> Bytes.ADDRESS_BITS;
            for (int loToIndex = fromIndex + Bytes.BITS; toIndex >= loToIndex; ) {
                toIndex -= Bytes.BITS;
                src[--destUnit] &= getByte(src, toIndex);
            }
            andByte(src, destIndex, destIndex + (toIndex - fromIndex), src, fromIndex, toIndex);
        }
    }

    /**
   * Nearly same code as andImpl(...)
   */
    private static void andNotImpl(byte[] src, int fromIndex, int toIndex, byte[] dest, int destIndex) {
        int count = toIndex - fromIndex;
        if ((count == 0) || ((src == dest) && (destIndex == fromIndex))) return;
        if (count < Bytes.BITS) {
            andNotByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
            return;
        }
        final boolean forward = !((src == dest) && (destIndex > fromIndex) && (destIndex < toIndex));
        int fromUnit = fromIndex >> Bytes.ADDRESS_BITS;
        int fromOffs = fromIndex & Bytes.BIT_INDEX_MASK;
        int toOffs = (fromIndex + count) & Bytes.BIT_INDEX_MASK;
        if (forward && (fromOffs != 0)) {
            final int d = Bytes.BITS - fromOffs;
            andNotByte(dest, destIndex, destIndex + d, src, fromIndex, fromIndex + d);
            count -= d;
            fromIndex += d;
            destIndex += d;
            if (count < Bytes.BITS) {
                andNotByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
                return;
            }
            fromOffs = 0;
            fromUnit++;
        }
        if ((toOffs != 0) && (!forward || (fromIndex < destIndex) || (fromIndex >= destIndex + count))) {
            andNotByte(dest, destIndex + count - toOffs, destIndex + count, src, toIndex - toOffs, toIndex);
            count -= toOffs;
            toIndex -= toOffs;
            if (count < Bytes.BITS) {
                andNotByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
                return;
            }
            toOffs = 0;
        }
        if ((fromOffs == 0) && (toOffs == 0) && ((destIndex & Bytes.BIT_INDEX_MASK) == 0)) {
            int toUnit = toIndex >> Bytes.ADDRESS_BITS;
            if (forward) {
                int destUnit = destIndex >> Bytes.ADDRESS_BITS;
                while (fromUnit < toUnit) dest[destUnit++] &= ~src[fromUnit++];
            } else {
                int destUnit = (destIndex + count) >> Bytes.ADDRESS_BITS;
                while (fromUnit < toUnit) src[--destUnit] &= ~src[--toUnit];
            }
        } else if (forward) {
            for (int hiFromIndex = toIndex - Bytes.BITS; fromIndex < hiFromIndex; ) {
                andByte(dest, destIndex, ~(src[fromUnit++] & 0xff));
                fromIndex += Bytes.BITS;
                destIndex += Bytes.BITS;
            }
            andNotByte(dest, destIndex, destIndex + toIndex - fromIndex, src, fromIndex, toIndex);
        } else {
            int destToIndex = destIndex + count;
            int destToOffset = destToIndex & Bytes.BIT_INDEX_MASK;
            if (destToOffset != 0) {
                andNotByte(src, destToIndex - destToOffset, destToIndex, src, toIndex - destToOffset, toIndex);
                destToIndex -= destToOffset;
                toIndex -= destToOffset;
            }
            int destUnit = destToIndex >> Bytes.ADDRESS_BITS;
            for (int loToIndex = fromIndex + Bytes.BITS; toIndex >= loToIndex; ) {
                toIndex -= Bytes.BITS;
                src[--destUnit] &= ~getByte(src, toIndex);
            }
            andNotByte(src, destIndex, destIndex + (toIndex - fromIndex), src, fromIndex, toIndex);
        }
    }

    /**
   * Nearly same code as andImpl(...)
   */
    private static void orImpl(byte[] src, int fromIndex, int toIndex, byte[] dest, int destIndex) {
        int count = toIndex - fromIndex;
        if ((count == 0) || ((src == dest) && (destIndex == fromIndex))) return;
        if (count < Bytes.BITS) {
            orByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
            return;
        }
        final boolean forward = !((src == dest) && (destIndex > fromIndex) && (destIndex < toIndex));
        int fromUnit = fromIndex >> Bytes.ADDRESS_BITS;
        int fromOffs = fromIndex & Bytes.BIT_INDEX_MASK;
        int toOffs = (fromIndex + count) & Bytes.BIT_INDEX_MASK;
        if (forward && (fromOffs != 0)) {
            final int d = Bytes.BITS - fromOffs;
            orByte(dest, destIndex, destIndex + d, src, fromIndex, fromIndex + d);
            count -= d;
            fromIndex += d;
            destIndex += d;
            if (count < Bytes.BITS) {
                orByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
                return;
            }
            fromOffs = 0;
            fromUnit++;
        }
        if ((toOffs != 0) && (!forward || (fromIndex < destIndex) || (fromIndex >= destIndex + count))) {
            orByte(dest, destIndex + count - toOffs, destIndex + count, src, toIndex - toOffs, toIndex);
            count -= toOffs;
            toIndex -= toOffs;
            if (count < Bytes.BITS) {
                orByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
                return;
            }
            toOffs = 0;
        }
        if ((fromOffs == 0) && (toOffs == 0) && ((destIndex & Bytes.BIT_INDEX_MASK) == 0)) {
            int toUnit = toIndex >> Bytes.ADDRESS_BITS;
            if (forward) {
                int destUnit = destIndex >> Bytes.ADDRESS_BITS;
                while (fromUnit < toUnit) dest[destUnit++] |= src[fromUnit++];
            } else {
                int destUnit = (destIndex + count) >> Bytes.ADDRESS_BITS;
                while (fromUnit < toUnit) src[--destUnit] |= src[--toUnit];
            }
        } else if (forward) {
            for (int hiFromIndex = toIndex - Bytes.BITS; fromIndex < hiFromIndex; ) {
                orByte(dest, destIndex, src[fromUnit++] & 0xff);
                fromIndex += Bytes.BITS;
                destIndex += Bytes.BITS;
            }
            orByte(dest, destIndex, destIndex + toIndex - fromIndex, src, fromIndex, toIndex);
        } else {
            int destToIndex = destIndex + count;
            int destToOffset = destToIndex & Bytes.BIT_INDEX_MASK;
            if (destToOffset != 0) {
                orByte(src, destToIndex - destToOffset, destToIndex, src, toIndex - destToOffset, toIndex);
                destToIndex -= destToOffset;
                toIndex -= destToOffset;
            }
            int destUnit = destToIndex >> Bytes.ADDRESS_BITS;
            for (int loToIndex = fromIndex + Bytes.BITS; toIndex >= loToIndex; ) {
                toIndex -= Bytes.BITS;
                src[--destUnit] |= getByte(src, toIndex);
            }
            orByte(src, destIndex, destIndex + (toIndex - fromIndex), src, fromIndex, toIndex);
        }
    }

    /**
   * Nearly same code as andImpl(...)
   */
    private static void xorImpl(byte[] src, int fromIndex, int toIndex, byte[] dest, int destIndex) {
        int count = toIndex - fromIndex;
        if ((count == 0) || ((src == dest) && (destIndex == fromIndex))) return;
        if (count < Bytes.BITS) {
            xorByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
            return;
        }
        final boolean forward = !((src == dest) && (destIndex > fromIndex) && (destIndex < toIndex));
        int fromUnit = fromIndex >> Bytes.ADDRESS_BITS;
        int fromOffs = fromIndex & Bytes.BIT_INDEX_MASK;
        int toOffs = (fromIndex + count) & Bytes.BIT_INDEX_MASK;
        if (forward && (fromOffs != 0)) {
            final int d = Bytes.BITS - fromOffs;
            xorByte(dest, destIndex, destIndex + d, src, fromIndex, fromIndex + d);
            count -= d;
            fromIndex += d;
            destIndex += d;
            if (count < Bytes.BITS) {
                xorByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
                return;
            }
            fromOffs = 0;
            fromUnit++;
        }
        if ((toOffs != 0) && (!forward || (fromIndex < destIndex) || (fromIndex >= destIndex + count))) {
            xorByte(dest, destIndex + count - toOffs, destIndex + count, src, toIndex - toOffs, toIndex);
            count -= toOffs;
            toIndex -= toOffs;
            if (count < Bytes.BITS) {
                xorByte(dest, destIndex, destIndex + count, src, fromIndex, toIndex);
                return;
            }
            toOffs = 0;
        }
        if ((fromOffs == 0) && (toOffs == 0) && ((destIndex & Bytes.BIT_INDEX_MASK) == 0)) {
            int toUnit = toIndex >> Bytes.ADDRESS_BITS;
            if (forward) {
                int destUnit = destIndex >> Bytes.ADDRESS_BITS;
                while (fromUnit < toUnit) dest[destUnit++] ^= src[fromUnit++];
            } else {
                int destUnit = (destIndex + count) >> Bytes.ADDRESS_BITS;
                while (fromUnit < toUnit) src[--destUnit] ^= src[--toUnit];
            }
        } else if (forward) {
            for (int hiFromIndex = toIndex - Bytes.BITS; fromIndex < hiFromIndex; ) {
                xorByte(dest, destIndex, src[fromUnit++] & 0xff);
                fromIndex += Bytes.BITS;
                destIndex += Bytes.BITS;
            }
            xorByte(dest, destIndex, destIndex + toIndex - fromIndex, src, fromIndex, toIndex);
        } else {
            int destToIndex = destIndex + count;
            int destToOffset = destToIndex & Bytes.BIT_INDEX_MASK;
            if (destToOffset != 0) {
                xorByte(src, destToIndex - destToOffset, destToIndex, src, toIndex - destToOffset, toIndex);
                destToIndex -= destToOffset;
                toIndex -= destToOffset;
            }
            int destUnit = destToIndex >> Bytes.ADDRESS_BITS;
            for (int loToIndex = fromIndex + Bytes.BITS; toIndex >= loToIndex; ) {
                toIndex -= Bytes.BITS;
                src[--destUnit] ^= getByte(src, toIndex);
            }
            xorByte(src, destIndex, destIndex + (toIndex - fromIndex), src, fromIndex, toIndex);
        }
    }
}
