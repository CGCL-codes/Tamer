package daimanager;

import encryption.EncryptionWrapper;
import interfaces.IDataModel;
import interfaces.IXMLManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

/**
 * XMLManager.java
 * 
 * Saves and loads the data from the encrypted XML file
 * 
 * @author Andy Dunkel andy.dunkel"at"ekiwi.de
 * @author published under the terms and conditions of the
 *      GNU General Public License,
 *      for details see file gpl.txt in the distribution
 *      package of this software
 *
 */
public class XMLManager implements IXMLManager {

    private String filename = "";

    private IDataModel model;

    private Document dom;

    public XMLManager() {
    }

    public XMLManager(String filename, IDataModel model) {
        this.filename = filename;
        this.model = model;
    }

    /**
     * Using JAXP in implementation independent manner create a document object
     * using which we create a xml tree in memory
     */
    @Override
    public void saveDocument() throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        dom = db.newDocument();
        createDOMTree();
        printToFile();
    }

    /**
     * The real workhorse which creates the XML structure
     */
    private void createDOMTree() {
        Element rootEle = dom.createElement("xml");
        dom.appendChild(rootEle);
        Element fileInfo = dom.createElement("fileinfo");
        Element appname = dom.createElement("appname");
        appname.setAttribute("version", "1");
        Text appnameText = dom.createTextNode("DA-CryptPad");
        appname.appendChild(appnameText);
        fileInfo.appendChild(appname);
        rootEle.appendChild(fileInfo);
        Element entries = dom.createElement("entries");
        Element element = nodeToXML(model.getRootNode());
        entries.appendChild(element);
        rootEle.appendChild(entries);
    }

    /**
     * Save a node to xml 
     * @param node
     * @return
     */
    private Element nodeToXML(EntryTreeNode node) {
        Element element = dom.createElement("entry");
        Element title = dom.createElement("title");
        Element content = dom.createElement("content");
        Text titleText = dom.createTextNode(node.toString());
        Text contentText = dom.createTextNode(node.getContent());
        content.setAttribute("type", "text");
        title.appendChild(titleText);
        content.appendChild(contentText);
        element.appendChild(title);
        element.appendChild(content);
        for (Enumeration e = node.children(); e.hasMoreElements(); ) {
            EntryTreeNode currentNode = (EntryTreeNode) e.nextElement();
            element.appendChild(nodeToXML(currentNode));
        }
        return element;
    }

    /**
     * This method uses Xerces specific classes
     * prints the XML document to file.
     */
    private void printToFile() throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        File file = new File(this.filename);
        Source source = new DOMSource(dom);
        file.createNewFile();
        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        transformer.transform(source, sr);
        String xmlText = sw.toString();
        EncryptionWrapper encryption = new EncryptionWrapper();
        xmlText = encryption.encryptFile(xmlText, model.getPassword());
        FileWriter fileWriter = new FileWriter(this.filename);
        fileWriter.write(xmlText);
        fileWriter.close();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public IDataModel getModel() {
        return model;
    }

    public void setModel(IDataModel model) {
        this.model = model;
    }

    @Override
    public void loadDocument(String filename) throws Exception {
        BufferedReader buffReader;
        String inputText = "";
        buffReader = new BufferedReader(new FileReader(filename));
        String inputLine;
        while ((inputLine = buffReader.readLine()) != null) {
            inputText += inputLine + "\n";
        }
        buffReader.close();
        EncryptionWrapper encryption = new EncryptionWrapper();
        inputText = encryption.decryptMessage(inputText, model.getPassword());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader inStream = new StringReader(inputText);
        InputSource inSource = new InputSource(inStream);
        dom = db.parse(inSource);
        model.clearModel();
        Element docEle = dom.getDocumentElement();
        NodeList nl = docEle.getElementsByTagName("entry");
        Element firstEl = (Element) nl.item(0);
        parseEntries(firstEl, model.getRootNode());
    }

    private void parseEntries(Element el, EntryTreeNode node) {
        NodeList nl = el.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Element child = (Element) nl.item(i);
            if (child.getNodeName().equals("entry")) {
                EntryTreeNode childNode = new EntryTreeNode(getTextValue(child, "title"));
                childNode.setContent(getTextValue(child, "content"));
                node.add(childNode);
                if (child.hasChildNodes() == true) parseEntries(child, childNode);
            }
        }
    }

    /**
     * I take a xml element and the tag name, look for the tag and get
     */
    private String getTextValue(Element ele, String tagName) {
        String textVal = null;
        try {
            NodeList nl = ele.getElementsByTagName(tagName);
            if (nl != null && nl.getLength() > 0) {
                Element el = (Element) nl.item(0);
                textVal = el.getFirstChild().getNodeValue();
            }
        } catch (Exception ex) {
            textVal = "";
        }
        return textVal;
    }

    /**
     * Calls getTextValue and returns a int value
     */
    private int getIntValue(Element ele, String tagName) {
        return Integer.parseInt(getTextValue(ele, tagName));
    }
}
