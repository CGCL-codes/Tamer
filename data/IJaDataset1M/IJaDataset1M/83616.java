package com.michaelzanussi.genalyze.genesys.attributes;

/**
 * AttributeErrorMessage
 * 
 * @author <a href="mailto:admin@michaelzanussi.com">Michael Zanussi</a>
 * @version 1.0 (28 September 2006) 
 */
public class AttributeErrorMessage extends AbstractAttribute {

    private String value;

    /**
	 * Single-arg constructor.
	 * 
	 * @param value attribute value.
	 */
    public AttributeErrorMessage(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }

    public Object value() {
        return value;
    }
}
