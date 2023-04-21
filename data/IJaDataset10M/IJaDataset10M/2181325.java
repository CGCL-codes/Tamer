package net.alinnistor.nk.domain.persistence.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.alinnistor.nk.domain.persistence.xml.exc.NodeNotFoundException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author <a href="mailto:nad7ir@yahoo.com">Alin NISTOR</a>
 */
public class XMLTree {

    public static final String INTEGER_TYPE = "integer";

    public static final String LONG_TYPE = "long";

    public static final String BOOLEAN_TYPE = "boolean";

    public static final String STRING_TYPE = "string";

    private String file;

    private Document doc;

    private Element root;

    public XMLTree(String file) throws ParserConfigurationException, SAXException, IOException {
        this.file = file;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        doc = docBuilder.parse(new File(file));
        doc.getDocumentElement().normalize();
        root = doc.getDocumentElement();
    }

    public XMLTree(Document doc) {
        this.doc = doc;
    }

    public XMLTree(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        doc = docBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        root = doc.getDocumentElement();
    }

    public String getFirstLevelValue(String name) throws NodeNotFoundException {
        NodeList nl = root.getElementsByTagName(name);
        Element node = (Element) nl.item(0);
        if (node == null) throw new NodeNotFoundException();
        Node textNode = node.getFirstChild();
        return textNode.getNodeValue();
    }

    public Document getDoc() {
        return doc;
    }

    public String getSecondLevelValue(String plugin, String name) throws NodeNotFoundException {
        NodeList nl = root.getElementsByTagName(plugin);
        Element node = (Element) nl.item(0);
        if (node == null) throw new NodeNotFoundException();
        nl = node.getElementsByTagName(name);
        node = (Element) nl.item(0);
        if (node == null) throw new NodeNotFoundException();
        Node textNode = node.getFirstChild();
        return textNode.getNodeValue();
    }

    public Node getSecondLevelNode(String firstLvName, String name) throws NodeNotFoundException {
        NodeList nl = root.getElementsByTagName(firstLvName);
        Element node = (Element) nl.item(0);
        if (node == null) throw new NodeNotFoundException();
        nl = node.getElementsByTagName(name);
        node = (Element) nl.item(0);
        if (node == null) throw new NodeNotFoundException();
        Node textNode = node.getFirstChild();
        return textNode;
    }

    public Map<String, String> getSecondLevelValues(String roottag, String wordtag) throws NodeNotFoundException {
        Map<String, String> rv = new HashMap<String, String>();
        NodeList nl = root.getElementsByTagName(roottag);
        Element node = (Element) nl.item(0);
        if (node == null) throw new NodeNotFoundException();
        nl = node.getElementsByTagName(wordtag);
        if (nl == null) throw new NodeNotFoundException();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                Element el = (Element) nl.item(i);
                rv.put(el.getAttribute("name"), el.getFirstChild().getNodeValue());
            }
        }
        return rv;
    }

    public Object[] getSecondLevelValuesAndTypes(String roottag) throws NodeNotFoundException {
        Map<String, String> sml = new HashMap<String, String>();
        List<String> std = new ArrayList<String>();
        Object[] rv = new Object[] { sml, std };
        NodeList nl = root.getElementsByTagName(roottag);
        Element node = (Element) nl.item(0);
        if (node == null) throw new NodeNotFoundException();
        nl = node.getChildNodes();
        if (nl == null) throw new NodeNotFoundException();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i) instanceof Element) {
                Element el = (Element) nl.item(i);
                sml.put(el.getAttribute("name"), el.getFirstChild().getNodeValue());
                String type = el.getAttribute("type");
                if ("std".equals(type)) {
                    std.add(el.getFirstChild().getNodeValue());
                }
            }
        }
        return rv;
    }

    public Node getRoot() {
        return root;
    }

    public void persist() throws Exception {
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.transform(source, result);
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        out.write(sw.toString());
        out.close();
    }
}
