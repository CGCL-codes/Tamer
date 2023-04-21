package com.prowidesoftware.swift.model.field;

import java.io.Serializable;
import org.apache.commons.lang.StringUtils;
import com.prowidesoftware.swift.model.*;
import com.prowidesoftware.swift.utils.SwiftFormatUtils;

/**
 * Field 54D<br /><br />
 *
 * validation pattern: [[/<DC>][/34x]$]35x[$35x]0-3<br />
 * parser pattern: [[/c][/S]$]S[$S]0-3<br />
 * components pattern: SSSSSS<br />
 *
 * <h1>Components Data types</h1>
 * <ul> 
 * 		<li>component1: <code>String</code></li> 
 * 		<li>component2: <code>String</code></li> 
 * 		<li>component3: <code>String</code></li> 
 * 		<li>component4: <code>String</code></li> 
 * 		<li>component5: <code>String</code></li> 
 * 		<li>component6: <code>String</code></li> 
 * </ul>
 *		 
 * <em>NOTE: this source code has been generated.</em>
 * <p>Template id: <br>
 * $Id: Field54D.java,v 1.11 2012/05/11 17:44:51 zubri Exp $ </p>
 *
 * @author www.prowidesoftware.com
 * @since 6.0
 */
@SuppressWarnings("unused")
public class Field54D extends Field implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor
	 */
    public Field54D() {
        super(6);
    }

    /**
	 * Creates the field parsing the parameter value into fields' components
	 * @param value
	 */
    public Field54D(String value) {
        this();
        java.util.List<String> lines = SwiftParseUtils.getLines(value);
        if (lines.size() > 0) {
            if (lines.get(0).startsWith("/")) {
                String t1 = SwiftParseUtils.getTokenFirst(lines.get(0), "/", "/");
                if (org.apache.commons.lang.StringUtils.isNotEmpty(t1)) {
                    if (t1.length() == 1) {
                        setComponent1(t1);
                        setComponent2(SwiftParseUtils.getTokenSecondLast(org.apache.commons.lang.StringUtils.substring(lines.get(0), 1), "/"));
                    } else {
                        setComponent2(org.apache.commons.lang.StringUtils.substring(lines.get(0), 1));
                    }
                } else {
                    setComponent2(SwiftParseUtils.getTokenSecondLast(org.apache.commons.lang.StringUtils.substring(lines.get(0), 1), "/"));
                }
                SwiftParseUtils.setComponentsFromLines(this, 3, 6, 1, lines);
            } else {
                SwiftParseUtils.setComponentsFromLines(this, 3, 6, 0, lines);
            }
        }
    }

    /**
	 * Serializes the fields' components into the single string value (SWIFT format)
	 */
    @Override
    public String getValue() {
        final StringBuilder result = new StringBuilder();
        boolean wroteSomething = false;
        if (org.apache.commons.lang.StringUtils.isNotEmpty(getComponent1())) {
            result.append("/" + getComponent1());
            wroteSomething = true;
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(getComponent2())) {
            result.append("/" + getComponent2());
            wroteSomething = true;
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(getComponent3())) {
            if (wroteSomething) {
                result.append(com.prowidesoftware.swift.io.writer.FINWriterVisitor.SWIFT_EOL);
            }
            result.append(getComponent3());
            wroteSomething = true;
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(getComponent4())) {
            if (wroteSomething) {
                result.append(com.prowidesoftware.swift.io.writer.FINWriterVisitor.SWIFT_EOL);
            }
            result.append(getComponent4());
            wroteSomething = true;
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(getComponent5())) {
            if (wroteSomething) {
                result.append(com.prowidesoftware.swift.io.writer.FINWriterVisitor.SWIFT_EOL);
            }
            result.append(getComponent5());
            wroteSomething = true;
        }
        if (org.apache.commons.lang.StringUtils.isNotEmpty(getComponent6())) {
            if (wroteSomething) {
                result.append(com.prowidesoftware.swift.io.writer.FINWriterVisitor.SWIFT_EOL);
            }
            result.append(getComponent6());
        }
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
	 * Get the component2 as String
	 * @return the component2 converted to String or <code>null</code> if cannot be converted
	 */
    public java.lang.String getComponent2AsString() {
        return (java.lang.String) getComponentAs(2, java.lang.String.class);
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

    /**
	 * Get the component4
	 * @return the component4
	 */
    public String getComponent4() {
        return getComponent(4);
    }

    /**
	 * Get the component4 as String
	 * @return the component4 converted to String or <code>null</code> if cannot be converted
	 */
    public java.lang.String getComponent4AsString() {
        return (java.lang.String) getComponentAs(4, java.lang.String.class);
    }

    /**
	 * Set the component4.
	 * @param component4 the component4 to set
	 */
    public void setComponent4(String component4) {
        setComponent(4, component4);
    }

    /**
	 * Get the component5
	 * @return the component5
	 */
    public String getComponent5() {
        return getComponent(5);
    }

    /**
	 * Get the component5 as String
	 * @return the component5 converted to String or <code>null</code> if cannot be converted
	 */
    public java.lang.String getComponent5AsString() {
        return (java.lang.String) getComponentAs(5, java.lang.String.class);
    }

    /**
	 * Set the component5.
	 * @param component5 the component5 to set
	 */
    public void setComponent5(String component5) {
        setComponent(5, component5);
    }

    /**
	 * Get the component6
	 * @return the component6
	 */
    public String getComponent6() {
        return getComponent(6);
    }

    /**
	 * Get the component6 as String
	 * @return the component6 converted to String or <code>null</code> if cannot be converted
	 */
    public java.lang.String getComponent6AsString() {
        return (java.lang.String) getComponentAs(6, java.lang.String.class);
    }

    /**
	 * Set the component6.
	 * @param component6 the component6 to set
	 */
    public void setComponent6(String component6) {
        setComponent(6, component6);
    }
}
