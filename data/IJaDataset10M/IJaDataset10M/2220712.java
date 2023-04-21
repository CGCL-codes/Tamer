package svc.core;

/**
 * A bit is either 1 or 0. Multiple bits form a <code>BitSequence
 * </code>.
 * 
 * @author Allen Charlton
 */
public class Bit {

    private boolean value;

    /**
	 * Constructs a <code>Bit</code> according to the number specified. Only
	 * if the number is 1 is this bit set.
	 * 
	 * @param b
	 *            a number of value 1 or 0. All values not of these two values
	 *            are regareded as 0.
	 */
    public Bit(int b) {
        value = (b == 1 ? true : false);
    }

    /**
	 * Constructs a <code>Bit</code> with the same value as the flag
	 * <tt>b</tt>.
	 * 
	 * @param b
	 *            a flag
	 */
    public Bit(boolean b) {
        value = b;
    }

    /**
	 * Constructs a <code>Bit</code> of value 0;
	 * 
	 */
    public Bit() {
        this(0);
    }

    /**
	 * True if the <code>Bit</code> is "1".
	 * 
	 * @return true if the bit is 1, false otherwise.
	 */
    public boolean isSet() {
        return value;
    }
}
