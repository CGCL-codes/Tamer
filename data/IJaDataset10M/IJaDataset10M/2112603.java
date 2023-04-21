package net.sf.jradius.packet.attribute;

/**
 * A simple attribute description bean (stored as triplet of Strings: 
 * name, operator, and value). 
 *
 * @author David Bird
 */
public final class AttributeDescription {

    private String name;

    private String op;

    private String value;

    /**
     * Default constructor
     */
    public AttributeDescription() {
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the op.
     */
    public String getOp() {
        return op;
    }

    /**
     * @param op The op to set.
     */
    public void setOp(String op) {
        this.op = op;
    }

    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
