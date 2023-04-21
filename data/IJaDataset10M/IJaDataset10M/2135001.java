package bpiwowar.argparser.holders;

/**
 * Wrapper class which ``holds'' a String reference, enabling methods to return
 * String references through arguments.
 */
public class StringHolder extends Holder<String> implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * Value of the String reference, set and examined by the application as
	 * needed.
	 */
    private String value;

    /**
	 * Constructs a new <code>StringHolder</code> with an initial value of
	 * <code>null</code>.
	 */
    public StringHolder() {
        setValue(null);
    }

    /**
	 * Constructs a new <code>StringHolder</code> with a specific initial
	 * value.
	 * 
	 * @param s
	 *            Initial String reference.
	 */
    public StringHolder(String s) {
        setValue(s);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
