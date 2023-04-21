package org.qtitools.qti.attribute.value;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.qtitools.qti.attribute.SingleAttribute;
import org.qtitools.qti.exception.QTIParseException;
import org.qtitools.qti.node.XmlNode;

/**
 * Attribute with Date value.
 * 
 * @author Jiri Kajaba
 */
public class DateAttribute extends SingleAttribute {

    private static final long serialVersionUID = 1L;

    /** Date formatting pattern. */
    private static final DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
	 * Constructs attribute.
	 *
	 * @param parent attribute's parent
	 * @param name attribute's name
	 */
    public DateAttribute(XmlNode parent, String name) {
        super(parent, name);
    }

    /**
	 * Constructs attribute.
	 *
	 * @param parent attribute's parent
	 * @param name attribute's name
	 * @param defaultValue attribute's default value
	 */
    public DateAttribute(XmlNode parent, String name, Date defaultValue) {
        super(parent, name, defaultValue);
    }

    /**
	 * Constructs attribute.
	 *
	 * @param parent attribute's parent
	 * @param name attribute's name
	 * @param value attribute's value
	 * @param defaultValue attribute's default value
	 * @param required is this attribute required
	 */
    public DateAttribute(XmlNode parent, String name, Date value, Date defaultValue, boolean required) {
        super(parent, name, value, defaultValue, required);
    }

    @Override
    public Date getValue() {
        return (Date) super.getValue();
    }

    /**
	 * Sets new value of attribute.
	 *
	 * @param value new value of attribute
	 * @see #getValue
	 */
    public void setValue(Date value) {
        super.setValue(value);
    }

    @Override
    public Date getDefaultValue() {
        return (Date) super.getDefaultValue();
    }

    /**
	 * Sets new default value of attribute.
	 *
	 * @param defaultValue new default value of attribute
	 * @see #getDefaultValue
	 */
    public void setDefaultValue(Date defaultValue) {
        super.setDefaultValue(defaultValue);
    }

    @Override
    protected Date parseValue(String value) {
        if (value == null || value.length() == 0) throw new QTIParseException("Invalid datetime '" + value + "'. Length is not valid.");
        try {
            return format.parse(value);
        } catch (ParseException ex) {
            throw new QTIParseException("Invalid datetime '" + value + "'.", ex);
        }
    }

    @Override
    public String valueToString() {
        return (getValue() != null) ? format.format(getValue()) : "";
    }

    @Override
    public String defaultValueToString() {
        return (getDefaultValue() != null) ? format.format(getDefaultValue()) : "";
    }
}
