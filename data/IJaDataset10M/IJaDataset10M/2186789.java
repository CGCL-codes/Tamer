package org.webcockpit.builder.config;

import org.dom4j.Document;
import org.dom4j.Element;
import org.webcockpit.beans.JspBind;

/**
 */
public class Bind extends JspBind implements Configurable {

    public static final String ELEMENT_BIND = "bind";

    private static final String ATTRIBUTE_BINDVAR = "bindvar";

    private static final String ATTRIBUTE_TYPE = "type";

    /**
	 * Initialize the instance from the XML Document. The 
	 * child instances of this instance are initialized 
	 * recursively until the entire instance is initialized.  
	 * 
	 * @param config The document containing all configuration information
	 * @param element The element
	 */
    public void init(Document config, Element element) throws ConfigurationException {
        setType(element.attributeValue(ATTRIBUTE_TYPE));
        setBindvar(element.attributeValue(ATTRIBUTE_BINDVAR));
        setData(element.getText());
    }

    /**
	 * Save the configurable item to an XML Document. The item
	 * adds elements to the parent element for itself and all
	 * it's children.
	 * 
	 * @param config The document being created
	 * @param element The parent element
	 */
    public void save(Document config, Element element) throws ConfigurationException {
        Element bind = element.addElement(ELEMENT_BIND);
        bind.addAttribute(ATTRIBUTE_TYPE, getType());
        bind.addAttribute(ATTRIBUTE_BINDVAR, getBindvar());
        if (getData() != null) {
            bind.setText(getData());
        }
    }
}
