package com.prowidesoftware.swift.model.field;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;
import com.prowidesoftware.swift.model.*;
import com.prowidesoftware.swift.utils.SwiftFormatUtils;

/**
 * Field 37H<br /><br />
 *
 * validation pattern: <DC><AMOUNT>12<br />
 * parser pattern: SN<br />
 * components pattern: SN<br />
 *
 * <h1>Components Data types</h1>
 * <ul> 
 * 		<li>component1: <code>String</code></li> 
 * 		<li>component2: <code>Number</code></li> 
 * </ul>
 *		 
 * <em>NOTE: this source code has been generated.</em>
 * <p>Template id: <br>
 * $Id: Field37H.java,v 1.11 2012/05/11 17:44:53 zubri Exp $ </p>
 *
 * @author www.prowidesoftware.com
 * @since 6.0
 */
@SuppressWarnings("unused")
public class Field37H extends Field implements Serializable, AmountContainer {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public Field37H() {
        super(2);
    }

    /**
	 * Creates the field parsing the parameter value into fields' components
	 * @param value
	 */
    public Field37H(String value) {
        this();
        setComponent1(SwiftParseUtils.getAlphaPrefix(value));
        setComponent2(SwiftParseUtils.getNumericSuffix(value));
    }

    /**
	 * Serializes the fields' components into the single string value (SWIFT format)
	 */
    @Override
    public String getValue() {
        final StringBuilder result = new StringBuilder();
        result.append(joinComponents());
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

    /**
	 * Get the component2
	 * @return the component2
	 */
    public String getComponent2() {
        return getComponent(2);
    }

    /**
	 * Get the component2 as Number
	 * @return the component2 converted to Number or <code>null</code> if cannot be converted
	 */
    public java.lang.Number getComponent2AsNumber() {
        return (java.lang.Number) getComponentAs(2, java.lang.Number.class);
    }

    /**
	 * Set the component2.
	 * @param component2 the component2 to set
	 */
    public void setComponent2(String component2) {
        setComponent(2, component2);
    }

    /**
    * NOT YET IMPLEMENTED
    */
    public List<BigDecimal> amounts() {
        final ArrayList<BigDecimal> result = new ArrayList<BigDecimal>();
        return result;
    }
}
