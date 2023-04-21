package com.prowidesoftware.swift.model.field;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import org.apache.commons.lang.StringUtils;
import com.prowidesoftware.swift.model.*;
import com.prowidesoftware.swift.utils.SwiftFormatUtils;

/**
 * Field 31H<br /><br />
 *
 * validation pattern: (<DATE2>[<HHMM>])|7!a<br />
 * parser pattern: (<DATE2>[<HHMM>])|S<br />
 * components pattern: EHS<br />
 *
 * <h1>Components Data types</h1>
 * <ul> 
 * 		<li>component1: <code>Calendar</code></li> 
 * 		<li>component2: <code>Calendar</code></li> 
 * 		<li>component3: <code>String</code></li> 
 * </ul>
 *		 
 * <em>NOTE: this source code has been generated.</em>
 * <p>Template id: <br>
 * $Id: Field31H.java,v 1.13 2012/05/11 17:44:52 zubri Exp $ </p>
 *
 * @author www.prowidesoftware.com
 * @since 6.0
 */
@Deprecated
@SuppressWarnings("unused")
public class Field31H extends Field implements Serializable, DateContainer {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public Field31H() {
        super(3);
    }

    /**
	 * Creates the field parsing the parameter value into fields' components
	 * @param value
	 */
    public Field31H(String value) {
        this();
        throw new org.apache.commons.lang.NotImplementedException("Missing parserPattern in Field.vm : (<DATE2>[<HHMM>])|S");
    }

    /**
	 * Serializes the fields' components into the single string value (SWIFT format)
	 */
    @Override
    public String getValue() {
        final StringBuilder result = new StringBuilder();
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

    /**
	 * Get the component2
	 * @return the component2
	 */
    public String getComponent2() {
        return getComponent(2);
    }

    /**
	 * Get the component2 as Calendar
	 * @return the component2 converted to Calendar or <code>null</code> if cannot be converted
	 */
    public java.util.Calendar getComponent2AsCalendar() {
        return SwiftFormatUtils.getHhmm(getComponent(2));
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
	 * Get the component3 as String
	 * @return the component3 converted to String or <code>null</code> if cannot be converted
	 */
    public java.lang.String getComponent3AsString() {
        return (java.lang.String) getComponentAs(3, java.lang.String.class);
    }

    /**
	 * Set the component3.
	 * @param component3 the component3 to set
	 */
    public void setComponent3(String component3) {
        setComponent(3, component3);
    }

    public List<Calendar> dates() {
        List<Calendar> result = new java.util.ArrayList<Calendar>();
        result.add(SwiftFormatUtils.getDate2(getComponent(1)));
        result.add(SwiftFormatUtils.getHhmm(getComponent(2)));
        return result;
    }
}
