package polimi.ln.nodeAttributes;

/**
 * Defines an integer attribute whose value is dynamically derived.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class DynamicIntegerAttribute extends IntegerAttribute {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Creates a new instance of this attribute.
	 * 
	 * @param key the name of the attribute
	 * @param provider an object used to derive the value associated dynamically.
	 */
    public DynamicIntegerAttribute(String key, IntegerProvider provider) {
        this.key = key;
        this.value = provider;
    }

    /**
	 * @see polimi.ln.nodeAttributes.NodeAttribute#getValue()
	 */
    public Object getValue() {
        return new Integer(((IntegerProvider) value).getValue());
    }

    public String toString() {
        return "IntegerAttribute key = " + key + ", value = " + value;
    }
}
