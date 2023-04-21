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
 * Field 33V<br /><br />
 *
 * validation pattern: <CUR><AMOUNT>15<br />
 * parser pattern: SN<br />
 * components pattern: CN<br />
 *
 * <h1>Components Data types</h1>
 * <ul> 
 * 		<li>component1: <code>Currency</code></li> 
 * 		<li>component2: <code>Number</code></li> 
 * </ul>
 *		 
 * <em>NOTE: this source code has been generated.</em>
 * <p>Template id: <br>
 * $Id: Field33V.java,v 1.12 2012/05/11 17:44:53 zubri Exp $ </p>
 *
 * @author www.prowidesoftware.com
 * @since 6.0
 */
@Deprecated
@SuppressWarnings("unused")
public class Field33V extends Field implements Serializable, CurrencyContainer, AmountContainer {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public Field33V() {
        super(2);
    }

    /**
	 * Creates the field parsing the parameter value into fields' components
	 * @param value
	 */
    public Field33V(String value) {
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
	 * Get the component1 as Currency
	 * @return the component1 converted to Currency or <code>null</code> if cannot be converted
	 */
    public java.util.Currency getComponent1AsCurrency() {
        return (java.util.Currency) getComponentAs(1, java.util.Currency.class);
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
