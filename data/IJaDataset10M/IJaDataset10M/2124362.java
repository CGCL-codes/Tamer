package org.keel.services.model.defaultmodel;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.keel.services.model.Input;

/**
 * 
 * @avalon.component
 * @avalon.service type=org.keel.services.model.Input
 * @x-avalon.info name=default-input
 * @x-avalon.lifestyle type=transient
 *  
 */
public class DefaultInput extends AbstractResponseElement implements Input {

    {
        this.logPrefix = "[org.keel.services.model.defaultmodel.DefaultInput] ";
    }

    private String label = null;

    private Map myValidValues = null;

    private Object defaultValue = null;

    public String getLabel() {
        return label;
    }

    public void setLabel(String newLabel) {
        assert newLabel != null;
        label = newLabel;
    }

    public void setValidValues(Map newValues) {
        assert newValues != null;
        myValidValues = new LinkedHashMap(newValues);
    }

    public void setDefaultValue(Object newDefault) {
        assert newDefault != this;
        defaultValue = newDefault;
    }

    public Object getDefaultValue() {
        if (defaultValue == null) {
            return "";
        }
        return defaultValue;
    }

    public Map getValidValues() {
        Map returnValue = null;
        if (myValidValues == null) {
            returnValue = new LinkedHashMap();
        } else {
            returnValue = myValidValues;
        }
        return returnValue;
    }
}
