package org.hip.kernel.bitmap;

import org.hip.kernel.sys.VObject;

/**
 * Implementation of a bit row.
 * 
 * @author Benno Luthiger
 */
public class BitRowImpl extends VObject implements BitRow, Cloneable {

    private boolean[] bits;

    private int size = 0;

    /**
	 * Constructor for BitRowImpl.
	 */
    public BitRowImpl(int inSize) {
        super();
        size = inSize;
        bits = new boolean[size];
    }

    /**
	 * @see BitRow#getBitValue()
	 */
    public int getBitValue() {
        int outValue = 0;
        for (int i = 0; i < size; i++) {
            if (bits[i]) outValue += (int) Math.pow(2, i);
        }
        return outValue;
    }

    /**
	 * @see BitRow#setBitValue(int)
	 */
    public void setBitValue(int inBitPattern) {
        for (int i = 0; i < size; i++) {
            int lPattern = (int) Math.pow(2, i);
            setBit(i, (lPattern & inBitPattern) > 0);
        }
    }

    /**
	 * @see BitRow#getBit(int)
	 */
    public boolean getBit(int inPosition) {
        return bits[inPosition];
    }

    /**
	 * @see BitRow#setBit(int, boolean)
	 */
    public void setBit(int inPosition, boolean inValue) {
        bits[inPosition] = inValue;
    }

    /**
	 * @see BitRow#invert()
	 */
    public BitRow invert() {
        BitRow outBitRow = new BitRowImpl(size);
        for (int i = 0; i < size; i++) outBitRow.setBit(i, !getBit(i));
        return outBitRow;
    }

    /**
	 * @see BitRow#and(BitRow)
	 */
    public BitRow and(BitRow inAnd) {
        BitRow outBitRow = new BitRowImpl(size);
        outBitRow.setBitValue(getBitValue() & inAnd.getBitValue());
        return outBitRow;
    }

    /**
	 * @see BitRow#or(BitRow)
	 */
    public BitRow or(BitRow inOr) {
        BitRow outBitRow = new BitRowImpl(size);
        outBitRow.setBitValue(getBitValue() | inOr.getBitValue());
        return outBitRow;
    }

    /**
	 * @see BitRow#xor(BitRow)
	 */
    public BitRow xor(BitRow inXOr) {
        BitRow outBitRow = new BitRowImpl(size);
        outBitRow.setBitValue(getBitValue() ^ inXOr.getBitValue());
        return outBitRow;
    }

    /**
	 * Returns the size of the row.
	 * 
	 * @return int
	 */
    public int getSize() {
        return size;
    }

    /**
	 * Returns a hash code value for this bit pattern.
	 * 
	 * @return int
	 */
    public int hashCode() {
        return getBitValue();
    }

    /**
	 * Compares this object against the specified object.
	 * 
	 * @param inObject java.lang.Object
	 * @return boolean
	 */
    public boolean equals(Object inObject) {
        if (inObject == null) return false;
        if (getClass() != inObject.getClass()) return false;
        return getBitValue() == ((BitRow) inObject).getBitValue();
    }

    /**
	 * Returns a string representation of this bit pattern.
	 * 
	 * @param java.lang.String
	 */
    public String toString() {
        String outPattern = "";
        for (int i = size - 1; i >= 0; i--) outPattern += (getBit(i) ? "1" : "0");
        return "<BitRow>" + outPattern + "</BitRow>";
    }

    /**
	 * Creates and returns a copy of this object.
	 * 
	 * @return java.lang.Object
	 */
    public Object clone() {
        BitRow outClone = new BitRowImpl(getSize());
        outClone.setBitValue(getBitValue());
        return outClone;
    }
}
