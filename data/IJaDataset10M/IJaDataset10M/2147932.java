package com.prowidesoftware.swift.model.field;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Currency;
import java.math.BigDecimal;
import org.apache.commons.lang.StringUtils;
import com.prowidesoftware.swift.model.*;
import com.prowidesoftware.swift.utils.SwiftFormatUtils;

/**
 * Field 92F<br /><br />
 *
 * validation pattern: :4!c//<CUR><AMOUNT>15<br />
 * parser pattern: :S//SN<br />
 * components pattern: SCN<br />
 *
 * <h1>Components Data types</h1>
 * <ul> 
 * 		<li>component1: <code>String</code></li> 
 * 		<li>component2: <code>Currency</code></li> 
 * 		<li>component3: <code>Number</code></li> 
 * </ul>
 *		 
 * <em>NOTE: this source code has been generated.</em>
 * <p>Template id: <br>
 * $Id: Field92F.java,v 1.8 2012/05/11 17:44:53 zubri Exp $ </p>
 *
 * @author www.prowidesoftware.com
 * @since 6.0
 */
@SuppressWarnings("unused")
public class Field92F extends Field implements Serializable, CurrencyContainer, AmountContainer {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public Field92F() {
        super(3);
    }

    /**
	 * Creates the field parsing the parameter value into fields' components
	 * @param value
	 */
    public Field92F(String value) {
        this();
        setComponent1(SwiftParseUtils.getTokenFirst(value, ":", "//"));
        String toparse = SwiftParseUtils.getTokenSecondLast(value, "//");
        setComponent2(SwiftParseUtils.getAlphaPrefix(toparse));
        setComponent3(SwiftParseUtils.getNumericSuffix(toparse));
    }

    /**
	 * Serializes the fields' components into the single string value (SWIFT format)
	 */
    @Override
    public String getValue() {
        final StringBuilder result = new StringBuilder();
        result.append(":");
        result.append(StringUtils.trimToEmpty(getComponent1()));
        result.append("//");
        result.append(StringUtils.trimToEmpty(getComponent2()));
        result.append(StringUtils.trimToEmpty(getComponent3()));
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
	 * Get the component2 as Currency
	 * @return the component2 converted to Currency or <code>null</code> if cannot be converted
	 */
    public java.util.Currency getComponent2AsCurrency() {
        return (java.util.Currency) getComponentAs(2, java.util.Currency.class);
    }

    /**
	 * Set the component2.
	 * @param component2 the component2 to set
	 */
    public void setComponent2(String component2) {
        setComponent(2, component2);
    }

    /**
	 * Get the component3
	 * @return the component3
	 */
    public String getComponent3() {
        return getComponent(3);
    }

    /**
	 * Get the component3 as Number
	 * @return the component3 converted to Number or <code>null</code> if cannot be converted
	 */
    public java.lang.Number getComponent3AsNumber() {
        return (java.lang.Number) getComponentAs(3, java.lang.Number.class);
    }

    /**
	 * Set the component3.
	 * @param component3 the component3 to set
	 */
    public void setComponent3(String component3) {
        setComponent(3, component3);
    }

    public List<String> currencyStrings() {
        final ArrayList<String> result = new ArrayList<String>();
        return result;
    }

    public List<Currency> currencies() {
        final List<String> l = currencyStrings();
        if (l.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        final ArrayList<Currency> result = new ArrayList<Currency>();
        for (String s : l) {
            result.add(Currency.getInstance(s));
        }
        return result;
    }

    /**
    * NOT YET IMPLEMENTED
    */
    public List<BigDecimal> amounts() {
        final ArrayList<BigDecimal> result = new ArrayList<BigDecimal>();
        return result;
    }
}
