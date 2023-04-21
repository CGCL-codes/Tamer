package tornado.conf;

import tornado.Logger;
import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;

public class XMLReader {

    /** The interface to the configuration system. When a configuration
      * value is parsed, it is inserted here so it is accessible to the
      * rest of the application.
      */
    private final Configuration confAPI;

    private final File configFile;

    XMLReader(Configuration confAPI, File configFile) {
        this.confAPI = confAPI;
        this.configFile = configFile;
        DOMParser parser = new DOMParser();
        try {
            parser.parse(configFile.toString());
            Document doc = parser.getDocument();
            processNode(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processNode(Node node) {
        switch(node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                processDocument((Document) node);
                break;
            case Node.ELEMENT_NODE:
                processElement((Element) node);
                break;
            case Node.TEXT_NODE:
                break;
            case Node.CDATA_SECTION_NODE:
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                break;
            case Node.ENTITY_REFERENCE_NODE:
                break;
            case Node.DOCUMENT_TYPE_NODE:
                break;
        }
    }

    private void processDocument(Document doc) {
        processNode(doc.getDocumentElement());
    }

    private void processElement(Element element) {
        String name = element.getTagName();
        if (name.equals("tornado")) {
            processChildNodes(element);
        } else if (name.equals("host")) {
            processChildNodes(element);
        } else if (name.equals("logging")) {
            processChildNodes(element);
        } else if (name.equals("threads")) {
            processThreads(element);
        } else if (name.equals("mime-types")) {
            processChildNodes(element);
        } else if (name.equals("type")) {
            processType(element);
        } else {
            processConf(element);
        }
    }

    private void processChildNodes(Node node) {
        NodeList children = node.getChildNodes();
        if (children != null) {
            for (int i = 0; i < children.getLength(); ++i) {
                processNode(children.item(i));
            }
        }
    }

    private void processThreads(Element element) {
        addThreadVar("startThreads", element);
        addThreadVar("minIdleThreads", element);
        addThreadVar("maxIdleThreads", element);
    }

    private void addThreadVar(String name, Element element) {
        String rawVal = element.getAttribute(name);
        Integer val = Integer.decode(rawVal);
        confAPI.add(name, val);
    }

    private void processType(Element element) {
        String type = element.getAttribute("name");
        NodeList exts = element.getElementsByTagName("ext");
        if (exts != null) {
            for (int i = 0; i < exts.getLength(); ++i) {
                addTypeVar(type, exts.item(i));
            }
        }
    }

    private void addTypeVar(String type, Node node) {
        Node textNode = node.getFirstChild();
        if (textNode.getNodeType() == Node.TEXT_NODE) {
            Text text = (Text) textNode;
            confAPI.addType(text.getData(), type);
        }
    }

    private void processConf(Element element) {
        String name = element.getTagName();
        Node dataNode = element.getFirstChild();
        if (dataNode.getNodeType() == Node.TEXT_NODE) {
            Text data = (Text) dataNode;
            addConfVar(name, data);
        }
    }

    private void addConfVar(String name, Text val) {
        String rawData = val.getData();
        Object data = null;
        try {
            data = Integer.decode(rawData);
        } catch (NumberFormatException e) {
            data = rawData;
        }
        confAPI.add(name, data);
    }
}
