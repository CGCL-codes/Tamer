package com.prowidesoftware.swift.model.field;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.commons.lang.StringUtils;
import com.prowidesoftware.swift.model.*;
import com.prowidesoftware.swift.utils.SwiftFormatUtils;

/**
 * Field 31S<br /><br />
 *
 * validation pattern: <DATE2><br />
 * parser pattern: S<br />
 * components pattern: E<br />
 *
 * <h1>Components Data types</h1>
 * <ul> 
 * 		<li>component1: <code>Calendar</code></li> 
 * </ul>
 *		 
 * <em>NOTE: this source code has been generated.</em>
 * <p>Template id: <br>
 * $Id: Field31S.java,v 1.12 2012/05/11 17:44:53 zubri Exp $ </p>
 *
 * @author www.prowidesoftware.com
 * @since 6.0
 */
@SuppressWarnings("unused")
public class Field31S extends Field implements Serializable, DateContainer {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public Field31S() {
        super(1);
    }

    /**
	 * Creates the field parsing the parameter value into fields' components
	 * @param value
	 */
    public Field31S(String value) {
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
	 * Get the component1 as Calendar
	 * @return the component1 converted to Calendar or <code>null</code> if cannot be converted
	 */
    public java.util.Calendar getComponent1AsCalendar() {
        return SwiftFormatUtils.getDate2(getComponent(1));
    }

    /**
	 * Set the component1.
	 * @param component1 the component1 to set
	 */
    public void setComponent1(String component1) {
        setComponent(1, component1);
    }

    public List<Calendar> dates() {
        List<Calendar> result = new java.util.ArrayList<Calendar>();
        result.add(SwiftFormatUtils.getDate2(getComponent(1)));
        return result;
    }
}
