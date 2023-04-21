package ranab.tpl;

/**
 * Template block representation class. 
 * Here block means all the data between '{' and '}' 
 * - excluding both first '{' and last '}'. 
 * 
 * @author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
 */
class Block {

    final int miStart;

    final int miLength;

    final byte[] mbyArray;

    /**
     * Constructors
     */
    Block(byte[] by) {
        miStart = 0;
        miLength = by.length;
        mbyArray = by;
    }

    Block(byte[] by, int start, int length) {
        miStart = start;
        miLength = length;
        mbyArray = by;
    }

    /**
     * Next index from where processing should start. 
     * It assumes a '}' at the end of each block.
     */
    int nextIndex() {
        return miStart + miLength + 1;
    }

    /**
     * Get subblock.
     */
    byte[] getBlock() {
        byte[] block = new byte[miLength];
        for (int i = 0; i < miLength; i++) block[i] = mbyArray[i + miStart];
        return block;
    }

    /**
     * Compare the block with a <code>byte[]</code>
     */
    boolean equals(String str) {
        if (str.length() != miLength) return false;
        boolean bEqual = true;
        for (int i = 0; i < miLength; i++) {
            if (str.charAt(i) != mbyArray[miStart + i]) {
                bEqual = false;
                break;
            }
        }
        return bEqual;
    }

    /**
     * Compare the block with a <code>byte[]</code>
     */
    boolean equals(byte[] by) {
        if (by.length != miLength) return false;
        boolean bEqual = true;
        for (int i = 0; i < miLength; i++) {
            if (by[i] != mbyArray[miStart + i]) {
                bEqual = false;
                break;
            }
        }
        return bEqual;
    }

    /**
     * String representation of this block
     */
    public String toString() {
        return new String(mbyArray, miStart, miLength);
    }
}
