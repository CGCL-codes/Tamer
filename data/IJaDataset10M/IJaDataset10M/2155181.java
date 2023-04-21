package org.apache.ibatis.abator.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.ibatis.abator.api.dom.xml.Attribute;
import org.apache.ibatis.abator.api.dom.xml.XmlElement;

/**
 * @author Jeff Butler
 */
public abstract class PropertyHolder {

    private Map properties;

    /**
	 *  
	 */
    public PropertyHolder() {
        super();
        properties = new HashMap();
    }

    public void addProperty(String name, String value) {
        properties.put(name, value);
    }

    public Map getProperties() {
        return properties;
    }

    protected void addPropertyXmlElements(XmlElement xmlElement) {
        Iterator iter = properties.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            XmlElement propertyElement = new XmlElement("property");
            propertyElement.addAttribute(new Attribute("name", (String) entry.getKey()));
            propertyElement.addAttribute(new Attribute("value", (String) entry.getValue()));
            xmlElement.addElement(propertyElement);
        }
    }
}
