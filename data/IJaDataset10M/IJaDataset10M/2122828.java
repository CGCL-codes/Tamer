package tr.com.srdc.isurf.isu.ontologyGenerator.oagisAdapter;

import org.w3c.dom.Element;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tr.com.srdc.isurf.isu.config.Config;
import tr.com.srdc.isurf.isu.xml.XmlUtils;

/**
 *
 * @author yildiray
 */
public class ComponentHandler {

    private static String componentsFilePath = "resources/OAGIS/Components.xsd";

    private Element root;

    private Hashtable<String, String> components;

    private Hashtable<String, String> componentHierarchy;

    private Hashtable<String, Vector> componentTypeContents;

    private Hashtable<String, Vector> groups;

    public ComponentHandler(Hashtable components, Hashtable componentTypeContents, Hashtable componentHierarchy) {
        componentsFilePath = Config.getProperty("oagis.components.path");
        root = XmlUtils.readFromFile(new File(componentsFilePath)).getDocumentElement();
        this.components = components;
        this.componentTypeContents = componentTypeContents;
        this.componentHierarchy = componentHierarchy;
        groups = new Hashtable();
    }

    private void processGroups(Node node) {
        Element element = (Element) node;
        String groupName = element.getAttribute("name");
        Vector<String> content = new Vector();
        NodeList insideElements = element.getElementsByTagName("xsd:element");
        for (int j = 0; j < insideElements.getLength(); j++) {
            Element insideElement = (Element) insideElements.item(j);
            String referenceName = insideElement.getAttribute("ref");
            if (referenceName == null || referenceName.equals("")) {
                referenceName = insideElement.getAttribute("name");
            }
            content.addElement(referenceName);
        }
        insideElements = element.getElementsByTagName("xsd:group");
        for (int j = 0; j < insideElements.getLength(); j++) {
            Element insideElement = (Element) insideElements.item(j);
            String childGroup = insideElement.getAttribute("ref");
            Vector childContent = groups.get(childGroup);
            content.addAll(childContent);
        }
        groups.put(groupName, content);
    }

    private void processElements(Node node) {
        Element element = (Element) node;
        String elementName = element.getAttribute("name");
        String elementType = element.getAttribute("type");
        components.put(elementName, elementType);
    }

    private void processHierarchy() {
        Enumeration keys = componentTypeContents.keys();
        while (keys.hasMoreElements()) {
            String complexTypeName = (String) keys.nextElement();
            Vector content = componentTypeContents.get(complexTypeName);
            String parent = componentHierarchy.get(complexTypeName);
            while (parent != null) {
                Vector parentContent = componentTypeContents.get(parent);
                if (parentContent != null) {
                    content.addAll(parentContent);
                }
                parent = componentHierarchy.get(parent);
            }
            componentTypeContents.put(complexTypeName, content);
        }
    }

    private void processComplexTypes(Element element) {
        String complexTypeName = element.getAttribute("name");
        Vector<String> content = new Vector();
        NodeList elements = element.getElementsByTagName("xsd:element");
        for (int i = 0; i < elements.getLength(); i++) {
            Element insideElement = (Element) elements.item(i);
            String insideElementName = insideElement.getAttribute("ref");
            if (insideElementName.equals("UserArea")) {
                continue;
            }
            content.addElement(insideElementName);
        }
        elements = element.getElementsByTagName("xsd:group");
        for (int i = 0; i < elements.getLength(); i++) {
            Element insideGroup = (Element) elements.item(i);
            String insideGroupName = insideGroup.getAttribute("ref");
            if (groups.get(insideGroupName) != null) {
                content.addAll(groups.get(insideGroupName));
            }
        }
        elements = element.getElementsByTagName("xsd:extension");
        for (int i = 0; i < elements.getLength(); i++) {
            Element baseElement = (Element) elements.item(i);
            String baseName = baseElement.getAttribute("base");
            componentHierarchy.put(complexTypeName, baseName);
        }
        componentTypeContents.put(complexTypeName, content);
    }

    public void parse() {
        NodeList elements = root.getChildNodes();
        for (int i = 0; i < elements.getLength(); i++) {
            Node node = elements.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("xsd:group")) {
                    processGroups(node);
                } else if (node.getNodeName().equals("xsd:element")) {
                    processElements(node);
                }
            }
        }
        elements = root.getElementsByTagName("xsd:complexType");
        for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element) elements.item(i);
            processComplexTypes(element);
        }
        processHierarchy();
    }
}
