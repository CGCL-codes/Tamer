package org.wings.template.propertymanagers;

import org.wings.SAbstractButton;
import org.wings.SComponent;

/**
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 */
public class SAbstractButtonPropertyManager extends SAbstractIconTextCompoundPropertyManager {

    static final Class[] classes = { SAbstractButton.class };

    public SAbstractButtonPropertyManager() {
    }

    public void setProperty(SComponent comp, String name, String value) {
        SAbstractButton c = (SAbstractButton) comp;
        if (name.equals("ACCESSKEY")) c.setMnemonic(value); else if (name.equals("TARGET")) c.setEventTarget(value); else super.setProperty(comp, name, value);
    }

    public Class[] getSupportedClasses() {
        return classes;
    }
}
