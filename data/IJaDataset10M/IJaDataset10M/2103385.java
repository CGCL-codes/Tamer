package joelib.util;

import java.io.*;
import java.util.*;
import org.apache.log4j.*;

/**
 * BitSet14 extensions.
 *
 * @author     wegnerj
 * @license GPL
 * @cvsversion    $Revision: 1.21 $, $Date: 2004/08/30 12:58:20 $
 */
public class JOEBitVec extends BitSet14 implements java.io.Serializable {

    /**
     * Obtain a suitable logger.
     */
    private static Category logger = Category.getInstance("joelib.util.JOEBitVec");

    public JOEBitVec() {
        super();
    }

    /**
     *  Constructor for the JOEBitVec object
     *
     * @param  bits  Description of the Parameter
     */
    public JOEBitVec(int bits) {
        super(bits);
    }

    /**
     *  Constructor for the JOEBitVec object
     *
     * @param  bv  Description of the Parameter
     */
    public JOEBitVec(final JOEBitVec bv) {
        clear();
        super.or(bv);
    }

    /**
     *  Clears the bits from the specified fromIndex(inclusive) to the specified
     *  toIndex(inclusive) to <tt>false</tt>.
     *
     * @param  bitIndex                       The new bitOff value
     */
    public void setBitOff(int bitIndex) {
        super.clear(bitIndex);
    }

    /**
     *  Sets the bits from the specified fromIndex(inclusive) to the specified
     *  toIndex(inclusive) to <tt>false</tt>.
     *
     * @param  bitIndex                       The new bitOn value
     */
    public void setBitOn(int bitIndex) {
        super.set(bitIndex);
    }

    /**
     *  Clears the bits from the specified fromIndex(inclusive) to the specified
     *  toIndex(inclusive) to <tt>false</tt>.
     *
     * @param  from                           index of the first bit to be
     *      cleared.
     * @param  to                             index after the last bit to be
     *      cleared.
     */
    public void setRangeOff(int from, int to) {
        super.clear(from, to + 1);
    }

    /**
     *  Sets the bits from the specified fromIndex(inclusive) to the specified
     *  toIndex(inclusive) to <tt>false</tt>.
     *
     * @param  from                           index of the first bit to be
     *      cleared.
     * @param  to                             index after the last bit to be
     *      cleared.
     */
    public void setRangeOn(int from, int to) {
        super.set(from, to + 1);
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public final int endBit() {
        return (-1);
    }

    /**
     *  Description of the Method
     *
     * @param  bv  Description of the Parameter
     * @return     Description of the Return Value
     */
    public JOEBitVec and(JOEBitVec bv) {
        JOEBitVec tmp = (JOEBitVec) bv.clone();
        tmp.andSet(this);
        return tmp;
    }

    /**
     *  Description of the Method
     *
     * @param  bv1  Description of the Parameter
     * @param  bv2  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static JOEBitVec and(JOEBitVec bv1, JOEBitVec bv2) {
        JOEBitVec tmp = (JOEBitVec) bv1.clone();
        tmp.andSet(bv2);
        return tmp;
    }

    /**
     *  Description of the Method
     *
     * @param  bv  Description of the Parameter
     * @return     Description of the Return Value
     */
    public JOEBitVec andSet(JOEBitVec bv) {
        super.and(bv);
        return this;
    }

    /**
     *  Returns the index of the first bit that is set to <tt>true</tt>. If no
     *  such bit exists then -1 is returned.
     *
     * @return                             the index of the next set bit.
     * @throws  IndexOutOfBoundsException  if the specified index is negative.
     */
    public int firstBit() {
        return nextBit(-1);
    }

    /**
     *  Description of the Method
     *
     * @param  nbits  Description of the Parameter
     */
    public void fold(int nbits) {
        System.out.println("Don't know what to do ...");
    }

    /**
     *  Returns the index of the first bit that is set to <tt>true</tt> that
     *  occurs on or after the specified starting index. If no such bit exists
     *  then -1 is returned. To iterate over the <tt>true</tt> bits in a
     *  <tt>BitSet14</tt>, use the following loop: for(int i=bs.nextSetBit(0);
     *  i>=0; i=bs.nextSetBit(i+1)) { // operate on index i here }
     *
     * @param  last                        the index to start checking from
     *      (inclusive).
     * @return                             the index of the next set bit.
     * @throws  IndexOutOfBoundsException  if the specified index is negative.
     */
    public final int nextBit(int last) {
        return nextSetBit(last + 1);
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public int countBits() {
        return super.cardinality();
    }

    /**
     *  Description of the Method
     *
     * @return    Description of the Return Value
     */
    public final boolean empty() {
        return (isEmpty());
    }

    /**
     *  Description of the Method
     *
     * @param  bit  Description of the Parameter
     * @return      Description of the Return Value
     */
    public boolean bitIsOn(int bit) {
        return get(bit);
    }

    /**
     *  Description of the Method
     *
     * @param  bit  Description of the Parameter
     * @return      Description of the Return Value
     */
    public boolean bitIsSet(int bit) {
        return get(bit);
    }

    public void fromBoolArray(boolean[] boolArray) {
        for (int i = 0; i < boolArray.length; i++) {
            if (boolArray[i]) {
                super.set(i);
            } else {
                super.clear(i);
            }
        }
    }

    /**
     *  Description of the Method
     *
     * @param  intArray  Description of the Parameter
     */
    public void fromIntArray(int[] intArray) {
        for (int i = 0; i < intArray.length; i++) {
            setBitOn(intArray[i]);
        }
    }

    /**
     * Reads this bit vector from a <tt>String</tt>.
     * e.g. [0 10 15 23]. It's a list of all set bits in this
     * bit vector, which are separated by a space character.
     * The bit vector is enclosed by two brackets.
     *
     * @param  s  the string representation of set bits enclosed by []-brackets
     */
    public void fromString(String s) {
        clear();
        StringTokenizer st = new StringTokenizer(s, " \t\n");
        String stmp;
        while (st.hasMoreTokens()) {
            stmp = st.nextToken();
            if (stmp.equals("[")) {
                continue;
            } else if (stmp.equals("]")) {
                break;
            }
            setBitOn(Integer.parseInt(stmp));
        }
    }

    /**
     * @param  v     Description of the Parameter
     */
    public void fromVectorWithIntArray(List v) {
        int[] itmp;
        for (int i = 0; i < v.size(); i++) {
            itmp = (int[]) v.get(i);
            setBitOn(itmp[0]);
        }
        if (logger.isDebugEnabled()) {
            for (int i = 0; i < v.size(); i++) {
                itmp = (int[]) v.get(i);
            }
        }
    }

    /**
     *  Description of the Method
     */
    public void negate() {
        flip(0, size());
    }

    /**
     *  Description of the Method
     *
     * @param  bv  Description of the Parameter
     * @return     Description of the Return Value
     */
    public JOEBitVec or(JOEBitVec bv) {
        JOEBitVec tmp = (JOEBitVec) bv.clone();
        tmp.orSet(this);
        return tmp;
    }

    /**
     *  Description of the Method
     *
     * @param  bv1  Description of the Parameter
     * @param  bv2  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static JOEBitVec or(JOEBitVec bv1, JOEBitVec bv2) {
        JOEBitVec tmp = (JOEBitVec) bv1.clone();
        tmp.orSet(bv2);
        return tmp;
    }

    /**
     *  Description of the Method
     *
     * @param  bv  Description of the Parameter
     * @return     Description of the Return Value
     */
    public JOEBitVec orSet(JOEBitVec bv) {
        super.or(bv);
        return this;
    }

    /**
     *  Description of the Method
     *
     * @param  bv  Description of the Parameter
     * @return     Description of the Return Value
     */
    public JOEBitVec set(final JOEBitVec bv) {
        clear();
        super.or(bv);
        return this;
    }

    /**
     *  Description of the Method
     *
     * @param  bv  Description of the Parameter
     * @return     Description of the Return Value
     */
    public JOEBitVec subSet(JOEBitVec bv) {
        JOEBitVec tmp = (JOEBitVec) bv.clone();
        tmp.negate();
        super.and(tmp);
        return this;
    }

    public boolean[] toBoolArr(int to) {
        boolean[] boolArray;
        boolArray = new boolean[to];
        for (int i = 0; i < to; i++) {
            boolArray[i] = get(i);
        }
        return boolArray;
    }

    public boolean[] toBoolArray() {
        return toBoolArr(this.size());
    }

    public int[] toIntArray() {
        int[] array;
        array = new int[countBits()];
        int index = 0;
        for (int i = nextBit(-1); i != -1; i = nextSetBit(i + 1)) {
            array[index] = i;
            index++;
        }
        return array;
    }

    /**
     * @param  v     Description of the Parameter
     */
    public void toVectorWithIntArray(List v) {
        v.clear();
        if (v instanceof Vector) {
            ((Vector) v).ensureCapacity(countBits());
        }
        for (int i = nextBit(-1); i != -1; i = nextSetBit(i + 1)) {
            int[] itmp = new int[] { i };
            v.add(itmp);
        }
    }

    /**
     *  Description of the Method
     *
     * @param  bv  Description of the Parameter
     * @return     Description of the Return Value
     */
    public JOEBitVec xor(JOEBitVec bv) {
        JOEBitVec tmp = (JOEBitVec) bv.clone();
        tmp.xorSet(this);
        return tmp;
    }

    /**
     *  Description of the Method
     *
     * @param  bv1  Description of the Parameter
     * @param  bv2  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static JOEBitVec xor(JOEBitVec bv1, JOEBitVec bv2) {
        JOEBitVec tmp = (JOEBitVec) bv1.clone();
        tmp.xorSet(bv2);
        return tmp;
    }

    /**
     *  Description of the Method
     *
     * @param  bv  Description of the Parameter
     * @return     Description of the Return Value
     */
    public JOEBitVec sub(JOEBitVec bv) {
        JOEBitVec tmp = (JOEBitVec) bv.clone();
        tmp.subSet(this);
        return tmp;
    }

    /**
     *  Description of the Method
     *
     * @param  bv1  Description of the Parameter
     * @param  bv2  Description of the Parameter
     * @return      Description of the Return Value
     */
    public static JOEBitVec sub(JOEBitVec bv1, JOEBitVec bv2) {
        JOEBitVec tmp = (JOEBitVec) bv1.clone();
        tmp.subSet(bv2);
        return tmp;
    }

    /**
     *  Returns the number of bits which are set in this <tt>BitSet14</tt> AND
     *  the <tt>BitSet14</tt> b.
     *
     * @param  b  Description of the Parameter
     * @return    Description of the Return Value
     */
    public int andCount(BitSet14 b) {
        BitSet14 a = (BitSet14) this.clone();
        a.and(b);
        return a.cardinality();
    }

    /**
     *  Description of the Method
     *
     * @param  is               Description of the Parameter
     * @exception  IOException  Description of the Exception
     */
    public void in(InputStream is) throws IOException {
        LineNumberReader ln = new LineNumberReader(new InputStreamReader(is));
        String line;
        for (; ; ) {
            line = ln.readLine();
            if (line == null) {
                break;
            }
            fromString(line);
        }
    }

    /**
     *  Description of the Method
     *
     * @param  os  Description of the Parameter
     */
    public void out(OutputStream os) {
        PrintStream ps = new PrintStream(os);
        ps.println(this.toString());
    }

    /**
     *  Returns the tanimoto similarity coefficient between this <tt>BitSet14</tt>
     *  with the <tt>BitSet14</tt> b.
     *
     * @param  b  Description of the Parameter
     * @return    Description of the Return Value
     */
    public double tanimoto(BitSet14 b) {
        int ab = andCount(b);
        double tanimoto = (double) ab / (double) ((this.cardinality() + b.cardinality()) - ab);
        return tanimoto;
    }

    /**
     * Writes this bit vector to a <tt>String</tt>.
     * e.g. [0 10 15 23]. It's a list of all set bits in this
     * bit vector, which are separated by a space character.
     * The bit vector is enclosed by two brackets.
     *
     * @return    Description of the Return Value
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("[ ");
        for (int i = nextBit(-1); i != -1; i = nextSetBit(i + 1)) {
            sb.append(i);
            sb.append(' ');
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     *  Description of the Method
     *
     * @param  bv  Description of the Parameter
     * @return     Description of the Return Value
     */
    public JOEBitVec xorSet(JOEBitVec bv) {
        super.xor(bv);
        return this;
    }
}
