package com.beanview.test;

import com.beanview.annotation.PropertyOptions;

/**
 * 
 * @author $Author: wiverson $
 * @version $Revision: 1.1.1.1 $, $Date: 2006/09/19 04:21:43 $
 */
public class ConfigurationTestObject {

    String firstName;

    String ignoreThisField;

    String dontTouchThisField;

    String lastName;

    public void setDontTouchThisField(String dontTouchThisField) {
        this.dontTouchThisField = dontTouchThisField;
    }

    @PropertyOptions(label = "Familiar Name")
    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @PropertyOptions(ignore = true)
    public String getIgnoreThisField() {
        return this.ignoreThisField;
    }

    public void setIgnoreThisField(String ignoreThisField) {
        this.ignoreThisField = ignoreThisField;
    }

    @PropertyOptions(editable = false)
    public String getDontTouchThisField() {
        return this.dontTouchThisField;
    }

    public void getIgnoreThisField(String dontTouchThisField) {
        this.dontTouchThisField = dontTouchThisField;
    }

    public String getReadOnly() {
        return "fixed!";
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
