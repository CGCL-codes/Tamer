package com.ivis.xprocess.properties.impl;

import org.jdom.Element;
import com.ivis.xprocess.framework.properties.SimpleProperty;
import com.ivis.xprocess.framework.properties.Xproperty;

/**
 * SimplePropertyImpl is a convenience for simple xproperties.
 *
 * example of simple xproperties: <PROPERTY_1 type="BOOLEAN"> FALSE
 * </PROPERTY_1> <PROPERTY_2 type="DAY"> 12345 </PROPERTY_2> <PROPERTY_3
 * type="FLOAT"> 25 </PROPERTY_3>
 */
public abstract class SimplePropertyImpl extends PropertyImpl implements SimpleProperty {

    public SimplePropertyImpl(Element element) {
        initializeFromJDOMElement(element);
    }

    public SimplePropertyImpl(String name) {
        super(name);
    }

    @Override
    public void initializeFromJDOMElement(org.jdom.Element element) {
        super.initializeFromJDOMElement(element);
        restoreValue(element.getText());
    }

    @Override
    public Element getJDOMElement() {
        Element element = super.getJDOMElement();
        element.setText(retrieveValue());
        return element;
    }

    /**
     * Implement a nice safe way of restoring your value from the string
     * parameter
     */
    protected abstract void restoreValue(String text);

    protected boolean equals(Xproperty xproperty) {
        if ((xproperty == null) || !(xproperty instanceof SimplePropertyImpl) || !(this.getName().equals(xproperty.getName()))) {
            return false;
        }
        String thatValue = ((SimplePropertyImpl) xproperty).retrieveValue();
        if (this.retrieveValue() == null) {
            return (thatValue == null);
        }
        return (this.retrieveValue().equals(thatValue));
    }

    @SuppressWarnings("unchecked")
    public static MergedProperty merge(Xproperty base, Xproperty local, Xproperty latest) {
        return PropertyImpl.merge(base, local, latest);
    }
}
