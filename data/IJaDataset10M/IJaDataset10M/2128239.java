package com.prowidesoftware.swift.model.field;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import com.prowidesoftware.swift.model.*;
import com.prowidesoftware.swift.utils.SwiftFormatUtils;

/**
 * Field 15G<br /><br />
 *
 * validation pattern: $<br />
 * parser pattern: S<br />
 * components pattern: S<br />
 *
 * <h1>Components Data types</h1>
 * <ul> 
 * 		<li>component1: <code>String</code></li> 
 * </ul>
 *		 
 * <em>NOTE: this source code has been generated.</em>
 * <p>Template id: <br>
 * $Id: Field15G.java,v 1.7 2012/05/11 17:44:53 zubri Exp $ </p>
 *
 * @author www.prowidesoftware.com
 * @since 6.0
 */
@SuppressWarnings("unused")
public class Field15G extends Field implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public Field15G() {
        super(1);
    }

    /**
	 * Creates the field parsing the parameter value into fields' components
	 * @param value
	 */
    public Field15G(String value) {
        this();
        setComponent1(value);
    }

    /**
	 * Serializes the fields' components into the single string value (SWIFT format)
	 */
    @Override
    public String getValue() {
        final StringBuilder result = new StringBuilder();
        result.append(StringUtils.trimToEmpty(getComponent1()));
        return result.toString();
    }

    /**
	 * Get the component1
	 * @return the component1
	 */
    public String getComponent1() {
        return getComponent(1);
    }

    /**
	 * Get the component1 as String
	 * @return the component1 converted to String or <code>null</code> if cannot be converted
	 */
    public java.lang.String getComponent1AsString() {
        return (java.lang.String) getComponentAs(1, java.lang.String.class);
    }

    /**
	 * Set the component1.
	 * @param component1 the component1 to set
	 */
    public void setComponent1(String component1) {
        setComponent(1, component1);
    }
}
