package com.gwtext.client.core;

import com.gwtext.client.util.JavaScriptObjectHelper;

/**
 *
 * @author Sanjiv Jivan
 */
public class GenericConfig extends BaseConfig {

    public void setProperty(String property, String value) {
        JavaScriptObjectHelper.setAttribute(jsObj, property, value);
    }

    public String getProperty(String property) {
        return JavaScriptObjectHelper.getAttribute(jsObj, property);
    }

    public void setProperty(String property, int value) {
        JavaScriptObjectHelper.setAttribute(jsObj, property, value);
    }

    public int getPropertyAsInt(String property) {
        return JavaScriptObjectHelper.getAttributeAsInt(jsObj, property);
    }

    public void setProperty(String property, boolean value) {
        JavaScriptObjectHelper.setAttribute(jsObj, property, value);
    }

    public boolean getPropertyAsBoolean(String property) {
        return JavaScriptObjectHelper.getAttributeAsBoolean(jsObj, property);
    }

    public void setProperty(String property, int[] value) {
        JavaScriptObjectHelper.setAttribute(jsObj, property, value);
    }

    public int[] getPropertyAsIntArray(String property) {
        return JavaScriptObjectHelper.getAttributeAsIntArray(jsObj, property);
    }

    public void setProperty(String property, String[] value) {
        JavaScriptObjectHelper.setAttribute(jsObj, property, value);
    }

    public String[] getPropertyAsStringArray(String property) {
        return JavaScriptObjectHelper.getAttributeAsStringArray(jsObj, property);
    }

    public void setProperty(String property, GenericConfig value) {
        JavaScriptObjectHelper.setAttribute(jsObj, property, value.getJsObj());
    }

    public void setProperty(String property, Object value) {
        JavaScriptObjectHelper.setAttribute(jsObj, property, value);
    }

    public Object getPropertyAsObject(String property) {
        return JavaScriptObjectHelper.getAttributeAsObject(jsObj, property);
    }
}
