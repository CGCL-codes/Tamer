package netkit.graph;

/** This is the abstract parent of all Attribute classes.  Each
 * Attribute object represents one field with a string name and an
 * enumerated type.  Subclasses are used to specify the specific kinds
 * of types and any polymorphic behavior.
 * @see Attributes
 * @see netkit.graph.io.SchemaReader
 * 
 * @author Kaveh R. Ghazi
 */
public abstract class Attribute {

    private final String name;

    private final Type type;

    /** Subclasses should provide a public constructor that overrides
     * this one.
     * @param name a String indicating the name of this attribute.
     */
    protected Attribute(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    /** Parses the supplied string token for insertion into this
     * attribute and converts the token into a double value.  If the
     * attribute subclass implementation requires keeping track of
     * tokens, then subclasses may do so.  Attribute values that may
     * need to be unique must therefore be parsed only once.
     * @param token the string value to parse.
     * @return the token converted into an double as per the parsing
     * rules of the subclass implementation.
     * @throws RuntimeException or an exception subclass if the token
     * cannot be parsed by the attribute subclass or some other
     * constraint is violated.
     */
    public abstract double parseAndInsert(String token);

    /** Formats the supplied value from this attribute as a String for
     * output.  Subclasses will supply their own mechanism for
     * conversion.
     * @param value a double to be converted into an output String.
     * @return a String representing the supplied value.
     */
    public abstract String formatForOutput(double value);

    /** Get the name of this Attribute.
     * @return the name of this Attribute.
     */
    public final String getName() {
        return name;
    }

    /** Get the type of this Attribute.
     * @return the type of this Attribute.
     */
    public final Type getType() {
        return type;
    }

    /** Returns a String representation for this object.
     * @return a String representation for this object.
     */
    public final String toString() {
        return '(' + name + "->" + type + ')';
    }
}
