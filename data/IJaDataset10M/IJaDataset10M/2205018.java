package uk.ac.ed.rapid.jsp;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;

/**
 * An element name and a map of name->attribute pairs. Used by the generator in a stack to keep 
 * @author jos
 *
 */
public class ElementMap {

    Map<String, String> attributeMap = new HashMap<String, String>();

    private String localName = null;

    public ElementMap(String localName, Attributes attributes) {
        this.localName = localName;
        for (int i = 0; i < attributes.getLength(); i++) attributeMap.put(attributes.getLocalName(i), attributes.getValue(i));
    }

    public String getValue(String name) {
        return attributeMap.get(name);
    }

    public String getName() {
        return this.localName;
    }
}
