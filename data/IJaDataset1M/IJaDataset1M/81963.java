package net.sf.jncu.fdil;

/**
 * Newton Streamed Object Format - Integer.
 * <p>
 * Integer objects are just that: objects containing integral values. The
 * integers are stored in a 30-bit field, allowing them a range of
 * <tt>-536,870,912...536,870,911</tt>.
 * 
 * @author moshew
 */
public class NSOFInteger extends NSOFImmediate {

    /**
	 * Default integer class.
	 */
    public static final NSOFSymbol CLASS_INTEGER = new NSOFSymbol("integer");

    /**
	 * A constant holding the minimum value an <code>integer</code> can have,
	 * -2<sup>29</sup>.
	 */
    public static final int MIN_VALUE = -0x20000000;

    /**
	 * A constant holding the maximum value an <code>integer</code> can have,
	 * 2<sup>29</sup>-1.
	 */
    public static final int MAX_VALUE = 0x1FFFFFFF;

    /**
	 * Creates a new integer with value {@code 0}.
	 */
    public NSOFInteger() {
        this(0);
    }

    /**
	 * Creates a new integer.
	 * 
	 * @param value
	 *            the integer value.
	 */
    public NSOFInteger(int value) {
        super(value, IMMEDIATE_INTEGER);
        setObjectClass(CLASS_INTEGER);
    }

    @Override
    protected void setValue(int value) {
        if (value < MIN_VALUE) throw new ValueOutOfRangeException();
        if (value > MAX_VALUE) throw new ValueOutOfRangeException();
        super.setValue(value);
    }

    @Override
    public NSOFObject deepClone() throws CloneNotSupportedException {
        return new NSOFInteger(this.getValue());
    }
}
