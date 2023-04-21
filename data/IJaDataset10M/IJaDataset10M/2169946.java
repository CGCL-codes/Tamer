package com.michaelzanussi.genalyze.genesys.attributes;

/**
 * AttributeAddressInfoStatus
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (28 September 2006) 
 */
public class AttributeAddressInfoStatus extends AbstractAttribute {

    private Integer value;

    /**
	 * Single-arg constructor.
	 * 
	 * @param value attribute value.
	 */
    public AttributeAddressInfoStatus(String value) {
        this.value = stringToInteger(value);
    }

    public String toString() {
        return value.toString();
    }

    public Object value() {
        return value;
    }
}
