package opennlp.tools.coref.sim;

/**
 * Enumeration of number types. 
 */
public class NumberEnum {

    private String name;

    /** Singular number type. */
    public static final NumberEnum SINGULAR = new NumberEnum("singular");

    /** Plural number type. */
    public static final NumberEnum PLURAL = new NumberEnum("plural");

    /** Unknown number type. */
    public static final NumberEnum UNKNOWN = new NumberEnum("unknown");

    private NumberEnum(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
