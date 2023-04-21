package polimi.ln.nodeAttributes;

import java.util.HashSet;

/**
 * Defines a static set attribute.
 * 
 * @author Luca Mottola <a
 *         href="mailto:mottola@elet.polimi.it">mottola@elet.polimi.it</a>
 * 
 */
public class SetAttribute extends NodeAttribute {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Creates a new instance of this attribute.
	 * 
	 * @param key the name of the attribute.
	 * @param values the values associated.
	 */
    public SetAttribute(String key, Object[] values) {
        this.key = key;
        HashSet container = new HashSet();
        for (int i = 0; i < values.length; i++) {
            container.add(values[i]);
        }
        this.value = container;
    }

    public String toString() {
        return "SetAttribute key = " + key + ", value = " + value;
    }
}
