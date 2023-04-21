package org.richa.tags.extjs.form;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;
import org.xml.sax.SAXException;

/**
 * This class represents a FormButton tag
 * 
 * @author ram
 * 
 */
public class FormButton extends BaseControlTag {

    /**
	 * Return the javascript object name to generate
	 */
    protected String getObjectName() {
        return ("Ext.Button");
    }

    /**
	 * Generate html and js before the body is processed
	 */
    protected void beforeBody(final XMLOutput output) throws JellyTagException, SAXException {
        String name = getName();
        if (isEmpty(name)) throw new JellyTagException("name is a required parameter for this tag");
        serialize("null", false);
        String formName = getCurrentFormName();
        scriptBuffer.appendln("    " + formName + ".buttons.push(" + getName() + ");");
        serializeEvents();
    }
}
