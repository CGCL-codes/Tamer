package bpiwowar.argparser.holders;

/**
 * Wrapper class which ``holds'' a double value, enabling methods to return
 * double values through arguments.
 */
public class DoubleHolder extends Holder<Double> implements java.io.Serializable {

    private static final long serialVersionUID = 6475123456594514908L;

    /**
	 * Value of the double, set and examined by the application as needed.
	 */
    private double value;

    /**
	 * Constructs a new <code>DoubleHolder</code> with an initial value of 0.
	 */
    public DoubleHolder() {
        setValue(0);
    }

    /**
	 * Constructs a new <code>DoubleHolder</code> with a specific initial value.
	 * 
	 * @param d
	 *            Initial double value.
	 */
    public DoubleHolder(double d) {
        setValue(d);
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

    @Override
    protected void setValue(String argument) {
        this.value = Double.valueOf(argument);
    }
}
