package huf.data;

/**
 * Simple mutable object wrapper for byte values.
 */
public class WByte {

    public byte val = (byte) 0;

    /**
	 * Create new wrapper and set its value to 0.
	 */
    public WByte() {
    }

    /**
	 * Create new wrapper with specified initial value.
	 *
	 * @param val initial value
	 */
    public WByte(byte val) {
        this.val = val;
    }

    /**
	 * Create new wrapper with specified initial value.
	 *
	 * @param val initial value
	 */
    public WByte(WByte val) {
        this.val = val.val;
    }

    /**
	 * Create new wrapper with specified initial value.
	 *
	 * @param val initial value
	 */
    public WByte(Byte val) {
        this.val = val.byteValue();
    }

    /**
	 * Set value.
	 *
	 * @param val new value
	 * @return this, already updated object
	 */
    public WByte set(byte val) {
        this.val = val;
        return this;
    }

    /**
	 * Set value.
	 *
	 * @param val new value
	 * @return this, already updated object
	 */
    public WByte set(WByte val) {
        this.val = val.val;
        return this;
    }

    /**
	 * Set value.
	 *
	 * @param val new valus
	 * @return this, already updated object
	 */
    public WByte set(Byte val) {
        this.val = val.byteValue();
        return this;
    }

    /** String representation of this object. */
    @Override
    public String toString() {
        return "" + val;
    }
}
