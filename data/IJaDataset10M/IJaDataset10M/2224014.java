package com.ixora.common.typedproperties;

import java.text.NumberFormat;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import com.ixora.common.typedproperties.exception.InvalidPropertyValue;
import com.ixora.common.typedproperties.exception.PropertyValueOutOfRange;
import com.ixora.common.xml.XMLUtils;
import com.ixora.common.xml.exception.XMLException;

/**
 * @author Daniel Moraru
 */
public abstract class PropertyEntryNumber<T extends Comparable<T>> extends PropertyEntry<T> {

    private static final long serialVersionUID = -4484396938757447806L;

    /** Max value */
    protected T max;

    /** Min value */
    protected T min;

    /** Format */
    protected NumberFormat format;

    /**
     * Constructor.
     */
    protected PropertyEntryNumber() {
        super();
    }

    /**
     * Constructor.
     * @param prop
     * @param v
     * @param set
     * @param type
     * @param required
     */
    protected PropertyEntryNumber(String prop, boolean v, T[] set, int type, boolean required) {
        super(prop, v, set, type, required);
    }

    /**
     * @return the max.
     */
    public T getMax() {
        return max;
    }

    /**
     * @return the min.
     */
    public T getMin() {
        return min;
    }

    /**
     * Sets the min.
     * @param c
     */
    public void setMin(T c) {
        min = c;
    }

    /**
     * Sets the max.
     * @param c
     */
    public void setMax(T c) {
        max = c;
    }

    /**
     * @return the format.
     */
    public NumberFormat getFormat() {
        return format;
    }

    /**
	 * @see com.ixora.common.typedproperties.PropertyEntry#validateValue(java.lang.Object)
	 */
    public void validateValue(T value) {
        super.validateValue(value);
        checkRange(value);
    }

    /**
     * Checks the min max range.
     * @param c
     * @throws PropertyValueOutOfRange
     */
    protected void checkRange(T c) {
        if (max != null && max.compareTo(c) < 0) {
            throw new PropertyValueOutOfRange(c.toString());
        }
        if (min != null && min.compareTo(c) > 0) {
            throw new PropertyValueOutOfRange(c.toString());
        }
    }

    /**
     * @see com.ixora.common.xml.XMLExternalizable#fromXML(org.w3c.dom.Node)
     */
    public void fromXML(Node node) throws XMLException {
        try {
            super.fromXML(node);
            Attr a = XMLUtils.findAttribute(node, "min");
            if (a != null) {
                min = makeObject(a.getValue());
            }
            a = XMLUtils.findAttribute(node, "max");
            if (a != null) {
                max = makeObject(a.getValue());
            }
            if (value != null) {
                validateValue(value);
            }
            if (defaultValue != null) {
                validateValue(defaultValue);
            }
        } catch (InvalidPropertyValue e) {
            throw new XMLException(e);
        }
    }

    /**
     * @see com.ixora.common.xml.XMLExternalizable#toXML(org.w3c.dom.Node)
     */
    public void toXML(Node parent) throws XMLException {
        super.toXML(parent);
        Element el = (Element) XMLUtils.findChild(parent, property);
        if (el == null) {
            return;
        }
        if (min != null) {
            el.setAttribute("min", min.toString());
        }
        if (max != null) {
            el.setAttribute("max", max.toString());
        }
    }
}
