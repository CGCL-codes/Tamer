package net.sf.jimo.loader.xml;

import java.util.Map;
import net.sf.jimo.common.filtermap.PropertiesParser;
import net.sf.jimo.loader.Loadable;
import net.sf.jimo.loader.xmlutil.Xml;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * 
 * <p>
 * Type: <strong><code>net.sf.jimo.loader.xml.XmlPropertiesWriter</code></strong>
 * </p>
 *
 * @version $Rev$
 * @author logicfish
 * @since 0.2
 *
 */
public class XmlPropertiesWriter {

    public static void createPropertiesNode(Node item, Map<? extends String, ? extends Object> props) {
        Document document = item.getOwnerDocument();
        if (props == null) return;
        for (String key : props.keySet()) {
            Object value = props.get(key);
            String[] valueText = PropertiesParser.getPropertyStrings(value);
            Element elementProp = document.createElement(Xml.XMLLOADER_PROPERTYTAG);
            item.appendChild(elementProp);
            elementProp.setAttribute(Xml.XMLLOADER_KEYATTTR, key);
            for (String string : valueText) {
                Element elementValue = document.createElement(Xml.XMLLOADER_VALUETAG);
                elementProp.appendChild(elementValue);
                Text textValue = document.createTextNode(string);
                elementValue.appendChild(textValue);
            }
        }
    }

    public static void createItemNode(Node node, Loadable item) {
        if (item.getId() == null) return;
        Document document = node.getOwnerDocument();
        Element element = document.createElement(Xml.XMLLOADER_LOADTAG);
        document.getDocumentElement().appendChild(element);
        element.setAttribute(Xml.XMLLOADER_IDATTTR, item.getId());
        createPropertiesNode(element, item.getProperties());
    }
}
