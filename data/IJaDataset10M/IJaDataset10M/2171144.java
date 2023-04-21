package net.dadajax.xml;

import java.util.Map;
import java.util.TreeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author dadajax
 *
 */
public class XmlSettingsManager extends XmlBase {

    protected Document document;

    private static final String ROOT_ELEMENT = "settings";

    public XmlSettingsManager(String fileName) {
        super(fileName, ROOT_ELEMENT);
        document = super.getDocument();
    }

    public void saveToFile(Map<String, String> settings) {
        document = super.createDocument();
        Node mainNode = document.getDocumentElement();
        for (Map.Entry<String, String> map : settings.entrySet()) {
            Element property = document.createElement("property");
            property.setAttribute(map.getKey(), map.getValue());
            mainNode.appendChild(property);
        }
        super.saveDocument(document);
    }

    public Map<String, String> getSettingsMap() {
        Map<String, String> settings = new TreeMap<String, String>();
        NodeList nodeList = document.getElementsByTagName("property");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            NamedNodeMap nnm = node.getAttributes();
            settings.put(nnm.item(0).getNodeName(), nnm.item(0).getTextContent());
        }
        return settings;
    }
}
