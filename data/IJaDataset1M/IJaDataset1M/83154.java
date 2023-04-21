package com.lovejoy.jabapper.digester;

import org.apache.commons.digester.ObjectCreateRule;
import org.xml.sax.Attributes;

/**
 * DOCUMENT ME!
 *
 * @author  (latest modification by $Author: roman_garcia $).
 * @version $Revision: 1.2 $ $Date: 2005/04/27 21:09:19 $
 */
public class CaseObjectCreateRule extends ObjectCreateRule {

    /** the values to compare */
    private Object[] values;

    /** the classes to instance when compare is true */
    private String[] classNames;

    /**
	 * Creates a new CaseObjectCreateRule object.
	 *
	 * @param attributeName DOCUMENT ME!
	 * @param clazz DOCUMENT ME!
	 */
    public CaseObjectCreateRule(String attributeName, Object[] values, String[] classNames) {
        super((String) null, attributeName);
        this.values = values;
        this.classNames = classNames;
    }

    public void begin(Attributes attributes) throws Exception {
        String attValue = attributes.getValue(this.attributeName);
        String realClassName = attValue;
        for (int i = 0; i < values.length; i++) {
            if (attValue.equals(values[i])) {
                realClassName = classNames[i];
                break;
            }
        }
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[ObjectCreateRule]{" + digester.getMatch() + "}New " + realClassName);
        }
        Class clazz = digester.getClassLoader().loadClass(realClassName);
        Object instance = clazz.newInstance();
        digester.push(instance);
    }

    /**
     * Process the end of this element.
     */
    public void end() throws Exception {
        Object top = digester.pop();
        if (digester.getLogger().isDebugEnabled()) {
            digester.getLogger().debug("[ObjectCreateRule]{" + digester.getMatch() + "} Pop " + top.getClass().getName());
        }
    }

    /**
     * Render a printable version of this Rule.
     */
    public String toString() {
        StringBuffer sb = new StringBuffer("CaseObjectCreateRule[");
        sb.append("attribute=");
        sb.append(this.attributeName);
        sb.append(", cases=");
        sb.append(this.values);
        sb.append(", classes=");
        sb.append(this.classNames);
        sb.append("]");
        return (sb.toString());
    }
}
