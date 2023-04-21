package com.ivis.xprocess.properties.impl;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ivis.xprocess.framework.properties.PropertyType;
import com.ivis.xprocess.framework.properties.Xproperty;

public class TimestampProperty extends SimplePropertyImpl {

    private static final Logger logger = Logger.getLogger(TimestampProperty.class.getName());

    private Date value;

    public TimestampProperty(org.jdom.Element element) {
        super(element);
    }

    public TimestampProperty(String name, Date value) {
        super(name);
        this.value = value;
    }

    public Xproperty clone() {
        return new TimestampProperty(this.getName(), this.value);
    }

    public Date getDateValue() {
        return value;
    }

    public String retrieveValue() {
        if (value == null) {
            return "";
        }
        return Long.toString(value.getTime());
    }

    @Override
    protected void restoreValue(String text) {
        if ((text == null) || (text.length() == 0)) {
            value = null;
        } else {
            try {
                value = new Date(Long.parseLong(text));
            } catch (NumberFormatException e) {
                logger.log(Level.FINE, "Problem parsing a Long from text " + text, e);
                value = null;
            }
        }
    }

    public Object retrieveObjectValue() {
        return value;
    }

    public PropertyType getPropertyType() {
        return PropertyType.TIMESTAMP;
    }
}
