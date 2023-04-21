package org.openliberty.igf.attributeService;

import java.util.Iterator;
import org.openliberty.igf.attributeService.schema.FilterDef;

/**
 * FilterValue is used to hold values returned from an attribute authority. An
 * FilterValue usually represents one returned attribute in a DigitalSubject.
 * All String values are UTF-8 encoded.
 * 
 * The FilterValue will also hold IGF meta response data for an attribute. TODO
 * Add IGF meta data response information TODO How to encode and handle
 * multi-byte data.
 * 
 */
public class FilterValue {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3566564049215928982L;

    private final String _name;

    private final String _value;

    /**
	 * This constructor is typically used to set filter values to be used to
	 * retrieve a subject
	 * 
	 * @param filter
	 *            The Attribute definition of the attribute being returned
	 * @param value
	 *            A single value for the attribute.
	 */
    public FilterValue(FilterDef filter, String value) {
        super();
        this._name = filter.getNameId();
        this._value = value;
    }

    public FilterValue(String filterRef, String value) {
        super();
        this._name = filterRef;
        this._value = value;
    }

    public String getNameIdRef() {
        return this._name;
    }

    public String getValue() {
        return this._value;
    }

    public Object[] getBinaryValues() {
        return null;
    }

    /**
	 * @param val An AttributeValue object to be compared containing one or more values
	 * @return true if the filter value matches at least one of the attribute values;
	 */
    public boolean compareAttributeValueExact(AttributeValue val) {
        Iterator<String> iter = val.iterator();
        while (iter.hasNext()) {
            String value = iter.next();
            if (this._value.equals(value)) return true;
        }
        return false;
    }

    /**
	 * @param val An AttributeValue object to be compared containing one or more values
	 * @return true if the filter value matches at least one of the attribute values;
	 */
    public boolean compareAttributeValueIgnoreCaseExact(AttributeValue val) {
        Iterator<String> iter = val.iterator();
        while (iter.hasNext()) {
            String value = iter.next();
            if (this._value.equalsIgnoreCase(value)) return true;
        }
        return false;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(this.getNameIdRef()).append(":\n");
        buf.append("  ").append(this._value).append('\n');
        return buf.toString();
    }
}
