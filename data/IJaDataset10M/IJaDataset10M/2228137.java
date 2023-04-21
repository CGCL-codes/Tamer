package com.zubarev.htmltable.action;

import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * A <b>ServeTableForm</b> is an extension of Struts ActionForm, it can
 * incorporate any number of HTML widgets of any type in a form without
 * a need for further extension.
 * 
 * @author Yuriy Zubarev
 * @see ServeTableAction
 */
public class ServeTableForm extends ActionForm {

    public static final String INDEXED_PROPERTY_SUBSTRING = "indexedProperty";

    public static final String CHECKBOXLIST_PROPERTY_SUBSTRING = "checkBoxList";

    public static final String NAVIGATE = "Navigate";

    public static final String SORT = "Sort";

    protected String action;

    protected String htmlTableDesc;

    protected String htmlTableInternalSubmit;

    protected Hashtable checkBoxLists;

    protected Hashtable ibuttons;

    protected Hashtable rbuttons;

    protected Hashtable properties;

    protected Hashtable indexedProperties;

    public void setHtmlTableDesc(String tableDesc) {
        this.htmlTableDesc = tableDesc;
    }

    public String getHtmlTableDesc() {
        return htmlTableDesc;
    }

    /**
   * Hashtable of text keys and corresponded values
   */
    public void setProperty(String propertyName, String propertyValue) {
        if (propertyValue == null) {
            propertyValue = "";
        }
        properties.put(propertyName, propertyValue);
    }

    /**
   * Hashtable of text keys and corresponded values
   */
    public String getProperty(String propertyName) {
        return (String) properties.get(propertyName);
    }

    public void setIndexedProperty(String propertyName, String[] propertyValue) {
        if (propertyValue == null) {
            propertyValue = new String[0];
        }
        indexedProperties.put(propertyName, propertyValue);
    }

    public String[] getIndexedProperty(String propertyName) {
        return (String[]) indexedProperties.get(propertyName);
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        action = "Submit";
        checkBoxLists = new Hashtable();
        properties = new Hashtable();
        indexedProperties = new Hashtable();
        ibuttons = new Hashtable();
        rbuttons = new Hashtable();
    }

    public void setCheckBoxList(String propertyName, String[] propertyValue) {
        checkBoxLists.put(propertyName, propertyValue);
    }

    public String[] getCheckBoxList(String propertyName) {
        return (String[]) checkBoxLists.get(propertyName);
    }

    /**
   * @return
   */
    public String getAction() {
        return action;
    }

    /**
   * @param string
   */
    public void setAction(String string) {
        action = string;
    }

    /**
   * @return
   */
    public String getHtmlTableInternalSubmit() {
        return htmlTableInternalSubmit;
    }

    /**
   * @param string
   */
    public void setHtmlTableInternalSubmit(String string) {
        htmlTableInternalSubmit = string;
    }

    public HTMLButton getImageButton(String name) {
        HTMLButton result = (HTMLButton) ibuttons.get(name);
        if (result == null) {
            result = new HTMLButton();
            ibuttons.put(name, result);
        }
        return result;
    }

    public void setImageButton(String name, HTMLButton value) {
        ibuttons.put(name, value);
    }

    public String getButton(String name) {
        return (String) rbuttons.get(name);
    }

    public void setButton(String name, String value) {
        rbuttons.put(name, value);
    }

    public HTMLButton getHTMLButton(String name) {
        HTMLButton result = new HTMLButton();
        HTMLButton iButton = getImageButton(name);
        if (iButton.isPressed()) {
            result = iButton;
        } else {
            String rButton = getButton(name);
            if (rButton != null) {
                result = new HTMLButton(rButton);
            }
        }
        return result;
    }

    /**
   * @return
   */
    public Hashtable getProperties() {
        return properties;
    }

    /**
   * @return
   */
    public Hashtable getCheckBoxLists() {
        return checkBoxLists;
    }
}
