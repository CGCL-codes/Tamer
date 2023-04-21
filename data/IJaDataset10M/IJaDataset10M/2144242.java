package bpiwowar.argparser.holders;

/**
 * Wrapper class which ``holds'' a float value, enabling methods to return float
 * values through arguments.
 */
public class FloatHolder extends Holder<Float> implements java.io.Serializable {

    private static final long serialVersionUID = -7362196628680520477L;

    /**
	 * Value of the float, set and examined by the application as needed.
	 */
    private float value;

    /**
	 * Constructs a new <code>FloatHolder</code> with an initial value of 0.
	 */
    public FloatHolder() {
        value = 0;
    }

    /**
	 * Constructs a new <code>FloatHolder</code> with a specific initial
	 * value.
	 * 
	 * @param f
	 *            Initial float value.
	 */
    public FloatHolder(float f) {
        value = f;
    }

    public Float getValue() {
        return value;
    }

    @Override
    protected void setValue(String argument) {
        this.value = Float.valueOf(argument);
    }
}
