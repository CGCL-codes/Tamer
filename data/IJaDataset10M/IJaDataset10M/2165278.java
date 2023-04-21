package desmoj.extensions.space2D.xml;

import desmoj.extensions.space2D.space.Attribute;
import desmoj.extensions.space2D.space.ComparableAttribute;

/**
 * A description of an attribute. Consists of a name, a type (optionally), and a
 * value object.
 */
public class AttributeDescription {

    /** the attribute's name */
    private String name;

    /** the attribute's value type */
    private String type;

    /** the attribute's value */
    private Object value;

    /** default constructor */
    public AttributeDescription() {
    }

    /** returns the attribute's name */
    public String getName() {
        return this.name;
    }

    /** sets the attribute's name */
    public void setName(String name) {
        this.name = name;
    }

    /** returns the attribute's value type */
    public String getType() {
        return this.type;
    }

    /** sets the attribute's value type */
    public void setType(String type) {
        this.type = type;
    }

    /** returns the attribute's value */
    public Object getValue() {
        return this.value;
    }

    /** sets the attribute's value */
    public void setValue(Object value) {
        this.value = value;
        if (value instanceof PrimitiveWrapper) {
            this.value = ((PrimitiveWrapper) value).getObject();
        }
        if (this.type == null || this.type.equals("org.exolab.castor.types.AnyNode")) {
            this.type = value.getClass().getName();
        }
    }

    /**
	 * returns the attribute described by this attribute description. If the
	 * value object is comparable, a comparable attribute is returned.
	 */
    public Attribute getAttribute() {
        Attribute attr;
        Object attrValue = this.value;
        if (this.value instanceof PrimitiveWrapper) {
            attrValue = ((PrimitiveWrapper) this.value).getObject();
        }
        if (Comparable.class.isAssignableFrom(attrValue.getClass())) {
            attr = new ComparableAttribute(this.name, (Comparable) attrValue);
        } else {
            attr = new Attribute(this.name, attrValue);
        }
        return attr;
    }

    /**
	 * returns a String representation of this attribute description. Lists the
	 * name and value object.
	 */
    public String toString() {
        StringBuffer s = new StringBuffer("Attribute ");
        s.append(this.name);
        s.append(": ");
        s.append(this.value.toString());
        return s.toString();
    }

    /**
	 * sets all fields in this attribute description to null / zero values.
	 */
    public void destroy() {
        this.name = null;
        this.type = null;
        this.value = null;
    }

    /**
	 * Constructs a new attribute description for the given attribute to be used
	 * in marshalling (= exporting to xml). Sets name, value and type.
	 */
    public AttributeDescription(Attribute a) {
        this.name = a.getName();
        this.value = a.getValue();
        this.type = this.value.getClass().getName();
    }
}
