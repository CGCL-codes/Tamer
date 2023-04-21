package org.tigr.microarray.mev.script.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.apache.xerces.parsers.DOMParser;
import org.tigr.microarray.mev.cluster.algorithm.AlgorithmData;
import org.tigr.microarray.mev.cluster.algorithm.AlgorithmParameters;
import org.tigr.microarray.mev.cluster.gui.impl.dialogs.Progress;
import org.tigr.microarray.mev.script.ScriptManager;
import org.tigr.microarray.mev.script.event.ScriptDocumentEvent;
import org.tigr.microarray.mev.script.event.ScriptEventListener;
import org.tigr.util.FloatMatrix;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/** DocumentBase is the base class for <CODE>ScriptDocument</CODE> objects
 * and maintains core function for DOM creation, modification, and output.
 * @author braisted
 */
public class DocumentBase extends DefaultHandler implements Serializable {

    public static final long serialVersionUID = 1000102010302010001L;

    /** Current verson to append
     */
    protected String tm4ScriptVersion = "1.0";

    /** script version
     */
    protected String mevScriptVersion = "1.0";

    /** <CODE>ScriptManager</CODE> to be used as a outward communication mode.
     */
    protected ScriptManager manager;

    /** XML Document
     */
    protected Document document;

    /** Script text string.  This is kept current for fast rendering.
     */
    protected String scriptText;

    /** Root element
     */
    protected Element root;

    /** mev Element
     */
    protected Element mevElement;

    /** Primary data element
     */
    protected Element primaryDataElement;

    /** main comment element.
     */
    protected Element commentElement;

    /** Analysis element.
     */
    protected Element analysisElement;

    /** Data id incrementer.  Increases as document grows.
     */
    protected int currDataID = 1;

    /** <CODE>AlgorithmSet</CODE> ID incrementer to increase during
     * script expansion.
     */
    protected int currAlgSetID = 1;

    /** Line separator for script rendering.
     */
    protected String lineSeparator = System.getProperty("line.separator");

    /** Indent.
     */
    protected String indent = "   ";

    /** Records number of errors found on validation.
     */
    protected int parseErrors = 0;

    /** The <CODE>ErrorLog</CODE> object dedicated to this object.
     */
    protected ErrorLog errorLog;

    /** Script listner vector.  This will permit update events
     * to be handled correctly.
     */
    private Vector listeners;

    protected boolean parsedScript = false;

    /** Inidicates if the text string represenation is current
     */
    protected boolean isTextCurrent = false;

    /** Creates a new instance of DocumentBase
     * @param manager <CODE>ScriptManager</CODE> object.
     */
    public DocumentBase(ScriptManager manager) {
        this.manager = manager;
        errorLog = new ErrorLog(manager);
        listeners = new Vector();
        DOMImplementationImpl impl = new DOMImplementationImpl();
        DocumentType docType = impl.createDocumentType("TM4ML", null, "../../config/mev_script_dtd.dtd");
        document = impl.createDocument(null, "TM4ML", docType);
        root = document.getDocumentElement();
        root.setAttribute("version", tm4ScriptVersion);
        mevElement = document.createElement("mev");
        mevElement.setAttribute("version", mevScriptVersion);
        root.appendChild(mevElement);
        analysisElement = document.createElement("analysis");
        primaryDataElement = document.createElement("primary_data");
        primaryDataElement.setAttribute("id", "1");
        createAlgorithmSet(1);
        mevElement.appendChild(primaryDataElement);
        mevElement.appendChild(analysisElement);
        scriptText = new String("");
        updateScript();
    }

    /** Creates a new instance of DocumentBase.
     * @param date creation datae
     * @param name name attribute
     * @param description Description attribute.
     * @param manager ScriptManager.
     */
    public DocumentBase(String date, String name, String description, ScriptManager manager) {
        this.manager = manager;
        errorLog = new ErrorLog(manager);
        listeners = new Vector();
        DOMImplementationImpl impl = new DOMImplementationImpl();
        DocumentType docType = impl.createDocumentType("TM4ML", null, "../../config/mev_script_dtd.dtd");
        document = impl.createDocument(null, "TM4ML", docType);
        root = document.getDocumentElement();
        root.setAttribute("version", tm4ScriptVersion);
        mevElement = document.createElement("mev");
        mevElement.setAttribute("version", mevScriptVersion);
        root.appendChild(mevElement);
        if (date != null) {
            setDateComment(date);
        }
        if (name != null) {
            setNameComment(name);
        }
        if (description != null) {
            setDescriptionComment(description);
        }
        analysisElement = document.createElement("analysis");
        primaryDataElement = document.createElement("primary_data");
        primaryDataElement.setAttribute("id", "1");
        createAlgorithmSet(1);
        mevElement.appendChild(primaryDataElement);
        mevElement.appendChild(analysisElement);
        scriptText = new String("");
        updateScript();
    }

    /** Copy constructor
     * @param base base object to copy
     */
    public DocumentBase(DocumentBase base) {
        this.manager = base.getManager();
        this.document = copyDocument(base.getDocument());
    }

    /** Constructs a copy of the document passed
     * @param doc Document to copy
     * @return
     */
    private Document copyDocument(Document doc) {
        DOMImplementationImpl impl = new DOMImplementationImpl();
        DocumentType docType = impl.createDocumentType("TM4ML", null, "../../config/mev_script_dtd.dtd");
        Document newDoc = impl.createDocument(null, "TM4ML", docType);
        copyChildren(doc.getDocumentElement(), newDoc.getDocumentElement(), newDoc);
        isTextCurrent = false;
        return newDoc;
    }

    /** Recursive method to copy document children, used as
     * a Document copy utility.
     * @param docElement Document Element to use as source for
     * children to copy
     * @param newElement destination element for copied children
     * @param newDoc New Document to import new nodes.
     */
    private void copyChildren(org.w3c.dom.Node docElement, org.w3c.dom.Node newElement, org.w3c.dom.Document newDoc) {
        NodeList nodes = docElement.getChildNodes();
        org.w3c.dom.Node newNode;
        org.w3c.dom.Node oldNode;
        for (int i = 0; i < nodes.getLength(); i++) {
            oldNode = (org.w3c.dom.Node) (nodes.item(i));
            newNode = newDoc.importNode(oldNode, false);
            newElement.appendChild(newNode);
            if (oldNode.hasChildNodes()) copyChildren(oldNode, newNode, newDoc);
        }
        isTextCurrent = false;
    }

    /** Set the Document object.
     * @param doc source Document
     */
    public void setDocument(Document doc) {
        document = doc;
        updateScript();
    }

    /** Returns the base Document.
     */
    public Document getDocument() {
        return this.document;
    }

    /** Returns the <CODE>ScriptManager</CODE> for the script
     */
    public ScriptManager getManager() {
        return this.manager;
    }

    /** Sets the date
     * @param comment Date comment
     */
    public void setDateComment(String comment) {
        Comment nameElement = document.createComment(" Original Script Creation Date: " + comment + " ");
        mevElement.appendChild(nameElement);
        updateScript();
    }

    /** Sets name comment.
     * @param comment name
     */
    public void setNameComment(String comment) {
        Comment nameElement = document.createComment(" Script Name: " + comment + " ");
        mevElement.appendChild(nameElement);
        updateScript();
    }

    /** Sets description comment
     * @param comment description
     */
    public void setDescriptionComment(String comment) {
        Comment dElement = document.createComment(" Script Description: " + comment + " ");
        mevElement.appendChild(dElement);
        updateScript();
    }

    /** Creates a new algorithm set with the provided data id.
     * @param dataRef Data reference ID.
     * @return
     */
    private Element createAlgorithmSet(int dataRef) {
        Element algSetElement = document.createElement("alg_set");
        algSetElement.setAttribute("set_id", String.valueOf(currAlgSetID));
        algSetElement.setAttribute("input_data_ref", String.valueOf(dataRef));
        analysisElement.appendChild(algSetElement);
        currAlgSetID++;
        isTextCurrent = false;
        return algSetElement;
    }

    /** Returns an algorithm set by ID
     * @param setID id
     * @return
     */
    private Element getAlgorithmSetByID(int setID) {
        String id = String.valueOf(setID);
        NodeList elements = analysisElement.getElementsByTagName("alg_set");
        for (int i = 0; i < elements.getLength(); i++) {
            if (id.equals(((Element) elements.item(i)).getAttribute("set_id"))) return (Element) elements.item(i);
        }
        return null;
    }

    /** Returns an algorithm set by id
     * @param dataRef ref (data id)
     * @return
     */
    private Element getAlgorithmSetByDataRef(int dataRef) {
        int setID = -1;
        String dataID = String.valueOf(dataRef);
        if (dataRef == 1) return getAlgorithmSetByID(1);
        NodeList list = document.getElementsByTagName("algorithm");
        Node algNode = null, node;
        for (int i = 0; i < list.getLength(); i++) {
            node = list.item(i);
            if (dataID.equals(((Element) node).getAttribute("input_data_ref"))) {
                algNode = node;
                break;
            }
        }
        if (algNode == null) {
            return this.createAlgorithmSet(dataRef);
        } else {
            return (Element) algNode.getParentNode();
        }
    }

    /** Returns true if algorithm is appended.  This is a primary method for
     * script expansion.
     * @param data AlgorithmData with parameters.
     * @param inputDataRef Input data reference (ID)
     * @return
     */
    public boolean appendAlgorithm(AlgorithmData data, int inputDataRef) {
        Element algElement = document.createElement("algorithm");
        Element currElement;
        boolean added = false;
        Element algSetElement = this.getAlgorithmSetByDataRef(inputDataRef);
        if (algSetElement == null) {
            return false;
        }
        Text nameText, keyText, valueText;
        AlgorithmParameters params = data.getParams();
        String name = params.getString("name");
        String alg_type = params.getString("alg-type");
        int algID = algSetElement.getElementsByTagName("algorithm").getLength() + 1;
        if (name != null) {
            algElement.setAttribute("alg_name", name);
            algElement.setAttribute("input_data_ref", String.valueOf(inputDataRef));
            algElement.setAttribute("alg_id", String.valueOf(algID));
            algElement.setAttribute("alg_type", String.valueOf(alg_type));
            addParameterList(algElement, params);
            Element matrices = document.createElement("mlist");
            Map map = data.getIntArrays();
            if (map.size() > 0) {
                addIntArrays(map, matrices);
            }
            map = data.getStringArrays();
            if (map.size() > 0) {
                addStringArrays(map, matrices);
            }
            map = data.getMatrixes();
            if (map.size() > 0) {
                addMatrices(map, matrices);
            }
            if (matrices.getChildNodes().getLength() > 0) algElement.appendChild(matrices);
            String[] outputNodes = data.getStringArray("output-nodes");
            Element outputNodeElement, dataElement;
            String outputClass;
            if (outputNodes != null) {
                outputNodeElement = document.createElement("output_data");
                outputClass = data.getParams().getString("output-class");
                if (outputClass != null) outputNodeElement.setAttribute("output_class", outputClass);
                for (int i = 0; i < outputNodes.length; i++) {
                    currDataID++;
                    dataElement = document.createElement("data_node");
                    dataElement.setAttribute("data_node_id", String.valueOf(this.currDataID));
                    dataElement.setAttribute("name", outputNodes[i]);
                    outputNodeElement.appendChild(dataElement);
                }
                algElement.appendChild(outputNodeElement);
            }
        }
        algSetElement.appendChild(algElement);
        added = true;
        isTextCurrent = false;
        fireScriptEvent();
        return added;
    }

    /** Adds the parameter list to the model.
     * @param algElement Algorithm to recieve
     * @param params AlgorithmParameters
     */
    private void addParameterList(Element algElement, AlgorithmParameters params) {
        Map paramMap = params.getMap();
        String key = "", value = "";
        Element paramsElement, paramElement, keyElement, valueElement;
        if (paramMap.size() > 1) {
            Set keySet = paramMap.keySet();
            Iterator iter = keySet.iterator();
            paramsElement = document.createElement("plist");
            while (iter.hasNext()) {
                key = (String) iter.next();
                value = ((String) (paramMap.get(key)));
                if (key.equals("name") || key.equals("output-class") || key.equals("alg-type")) continue;
                paramElement = document.createElement("param");
                paramElement.setAttribute("key", key);
                paramElement.setAttribute("value", value);
                paramsElement.appendChild(paramElement);
            }
            algElement.appendChild(paramsElement);
        }
    }

    /** Adds integer arrays
     * @param map Map of the values.
     * @param mlist Matrix list Element
     */
    private void addIntArrays(Map map, Element mlist) {
        Object[] arrayObjectNames = map.keySet().toArray();
        Element matrixElement, elementElement;
        int[] currentArray;
        String[] arrayNames = new String[arrayObjectNames.length];
        for (int i = 0; i < arrayNames.length; i++) arrayNames[i] = (String) arrayObjectNames[i];
        for (int i = 0; i < arrayNames.length; i++) {
            currentArray = (int[]) map.get(arrayNames[i]);
            matrixElement = document.createElement("matrix");
            matrixElement.setAttribute("name", arrayNames[i]);
            matrixElement.setAttribute("type", "int-array");
            matrixElement.setAttribute("row_dim", String.valueOf(currentArray.length));
            matrixElement.setAttribute("col_dim", "1");
            for (int j = 0; j < currentArray.length; j++) {
                elementElement = document.createElement("element");
                elementElement.setAttribute("row", String.valueOf(j));
                elementElement.setAttribute("col", "0");
                elementElement.setAttribute("value", String.valueOf(currentArray[j]));
                matrixElement.appendChild(elementElement);
            }
            mlist.appendChild(matrixElement);
        }
    }

    /** Add String arrays to the model
     * @param map value map
     * @param mlist Matrix list element.
     */
    private void addStringArrays(Map map, Element mlist) {
        Object[] arrayObjectNames = map.keySet().toArray();
        Element matrixElement, elementElement;
        String[] currentArray;
        String key;
        Vector arrayNames = new Vector();
        for (int i = 0; i < arrayObjectNames.length; i++) {
            key = (String) arrayObjectNames[i];
            if (!(key.equals("output-nodes"))) arrayNames.add(key);
        }
        if (arrayNames.size() < 1) return;
        for (int i = 0; i < arrayNames.size(); i++) {
            currentArray = (String[]) map.get((String) (arrayNames.elementAt(i)));
            matrixElement = document.createElement("matrix");
            matrixElement.setAttribute("name", ((String) arrayNames.elementAt(i)));
            matrixElement.setAttribute("type", "string-array");
            matrixElement.setAttribute("row_dim", String.valueOf(currentArray.length));
            matrixElement.setAttribute("col_dim", "1");
            for (int j = 0; j < currentArray.length; j++) {
                elementElement = document.createElement("element");
                elementElement.setAttribute("row", String.valueOf(j));
                elementElement.setAttribute("col", "0");
                elementElement.setAttribute("value", currentArray[j]);
                matrixElement.appendChild(elementElement);
            }
            mlist.appendChild(matrixElement);
        }
    }

    /** Adds matices.
     * @param map Matrix refs.
     * @param mlist Matrix Element list.
     */
    private void addMatrices(Map map, Element mlist) {
        Object[] matrixObjectNames = map.keySet().toArray();
        Element matrixElement;
        FloatMatrix currentFM;
        String[] matrixNames = new String[matrixObjectNames.length];
        for (int i = 0; i < matrixNames.length; i++) matrixNames[i] = (String) matrixObjectNames[i];
        for (int i = 0; i < matrixNames.length; i++) {
            currentFM = (FloatMatrix) map.get(matrixNames[i]);
            matrixElement = document.createElement("matrix");
            matrixElement.setAttribute("name", matrixNames[i]);
            matrixElement.setAttribute("type", "FloatMatrix");
            matrixElement.setAttribute("row_dim", String.valueOf(currentFM.getRowDimension()));
            matrixElement.setAttribute("col_dim", String.valueOf(currentFM.getColumnDimension()));
            addMatrixElements(currentFM, matrixElement);
            mlist.appendChild(matrixElement);
        }
    }

    /** Adds actual matrix elements.
     * @param matrix FloatMatrix to append
     * @param matrixElement Element to receive. */
    private void addMatrixElements(FloatMatrix matrix, Element matrixElement) {
        Element elementElement;
        float[][] data = matrix.A;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                elementElement = document.createElement("element");
                elementElement.setAttribute("row", String.valueOf(i));
                elementElement.setAttribute("col", String.valueOf(j));
                elementElement.setAttribute("value", String.valueOf(data[i][j]));
                matrixElement.appendChild(elementElement);
            }
        }
    }

    /** Writes the source to the file name.
     * @param fileName file name
     * @throws IOException
     */
    public void writeDocument(String fileName) throws IOException {
        FileWriter writer = new java.io.FileWriter(fileName);
        serialize(writer);
        writer.flush();
        writer.close();
    }

    /** Outputs document to Writer.
     * @param writer Output Writer
     * @throws IOException
     */
    private void writeDocument(Writer writer) throws IOException {
        serialize(writer);
    }

    /**
     * Serialization Code
     * @param writer Output Writer
     * @throws IOException  */
    private void serialize(Writer writer) throws IOException {
        serializeNode(document, writer, "");
    }

    /** serializes a single node.
     * @param node Node to output
     * @param writer output writer
     * @param indentLevel Indent level
     * @throws IOException
     */
    private void serializeNode(Node node, Writer writer, String indentLevel) throws IOException {
        String name;
        String text;
        StringTokenizer stok;
        switch(node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                writer.write("<?xml version=\"1.0\"?>");
                writer.write(lineSeparator);
                writer.write(ScriptConstants.DOCTYPE_STRING);
                writer.write(lineSeparator);
                Document doc = (Document) node;
                serializeNode(doc.getDocumentElement(), writer, " ");
                break;
            case Node.ELEMENT_NODE:
                boolean haveContent = false;
                name = node.getNodeName();
                writer.write(indentLevel + "<" + name);
                NamedNodeMap attrs = node.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Node attr = attrs.item(i);
                    writer.write(" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
                }
                NodeList children = node.getChildNodes();
                if (children.getLength() > 0) {
                    writer.write(">");
                    if ((children.item(0) != null) && (children.item(0).getNodeType() == Node.ELEMENT_NODE)) {
                        writer.write(lineSeparator);
                    }
                    for (int i = 0; i < children.getLength(); i++) {
                        serializeNode(children.item(i), writer, indentLevel + indent);
                    }
                    if ((children.item(0) != null) && (children.item(children.getLength() - 1).getNodeType() == Node.ELEMENT_NODE)) {
                        writer.write(indentLevel);
                    }
                    writer.write("</" + name + ">");
                } else {
                    writer.write("/>");
                }
                writer.write(lineSeparator);
                break;
            case Node.TEXT_NODE:
                writer.write(node.getNodeValue());
                break;
            case Node.COMMENT_NODE:
                text = node.getNodeValue();
                writer.write("<!--");
                stok = new StringTokenizer(text, " ");
                int charCnt;
                String word;
                while (stok.hasMoreElements()) {
                    charCnt = 0;
                    while (charCnt < 50) {
                        word = stok.nextToken();
                        writer.write(word);
                        charCnt = word.length();
                    }
                    writer.write(lineSeparator);
                }
                writer.write("-->");
                break;
        }
    }

    /** Forces the script to update on events where the
     * underlying information changes.
     */
    public void updateScript() {
        if (!isTextCurrent) {
            try {
                scriptText = "";
                writeScriptText(document, "");
                isTextCurrent = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** Writes node serialized form to the internal text String.
     * @param node node to serialize
     * @param indentLevel indent level.
     * @throws IOException
     */
    private void writeScriptText(Node node, String indentLevel) throws IOException {
        String name;
        String text;
        StringTokenizer stok;
        switch(node.getNodeType()) {
            case Node.DOCUMENT_NODE:
                scriptText += ("<?xml version=\"1.0\"?>");
                scriptText += (lineSeparator);
                NodeList nodes = node.getChildNodes();
                if (nodes != null) {
                    for (int i = 0; i < nodes.getLength(); i++) {
                        writeScriptText(nodes.item(i), "");
                    }
                }
                break;
            case Node.ELEMENT_NODE:
                name = node.getNodeName();
                scriptText += (indentLevel + "<" + name);
                NamedNodeMap attrs = node.getAttributes();
                for (int i = 0; i < attrs.getLength(); i++) {
                    Node attr = attrs.item(i);
                    scriptText += (" " + attr.getNodeName() + "=\"" + attr.getNodeValue() + "\"");
                }
                NodeList children = node.getChildNodes();
                if (children.getLength() > 0) {
                    scriptText += (">");
                    if ((children.item(0) != null)) {
                        scriptText += (lineSeparator);
                    }
                    for (int i = 0; i < children.getLength(); i++) {
                        if (children.item(i).getNodeType() != Node.TEXT_NODE) writeScriptText(children.item(i), indentLevel + indent);
                    }
                    if ((children.item(0) != null)) {
                        scriptText += (indentLevel);
                    }
                    scriptText += ("</" + name + ">");
                } else {
                    scriptText += ("/>");
                }
                scriptText += (lineSeparator);
                break;
            case Node.TEXT_NODE:
                String newText = node.getNodeValue();
                String preText = "";
                int pt;
                if (newText.indexOf("\n") != -1 && scriptText.lastIndexOf('\n') == scriptText.length() - 1) {
                    pt = newText.lastIndexOf("\n") + 1;
                    if (pt < newText.length()) {
                        preText = newText.substring(pt);
                        preText += newText.substring(0, pt - 1);
                        newText = preText;
                    }
                }
                scriptText += newText;
                break;
            case Node.COMMENT_NODE:
                text = node.getNodeValue();
                scriptText += indentLevel + "<!--";
                scriptText += text;
                scriptText += "-->" + lineSeparator + lineSeparator;
                break;
            case Node.DOCUMENT_TYPE_NODE:
                DocumentType docType = (DocumentType) node;
                scriptText += ("<!DOCTYPE " + docType.getName());
                scriptText += " SYSTEM ";
                scriptText += "\"" + docType.getSystemId() + "\">";
                scriptText += lineSeparator;
                break;
        }
    }

    /** Returns the current String representation of the script.
     * @return  */
    public String toString() {
        return scriptText;
    }

    /** Returns the document's <CODE>ErrorLog</CODE>
     * @return  */
    public ErrorLog getErrorLog() {
        return errorLog;
    }

    /** Opens the document's <CODE>ErrorLog</CODE>
     */
    public void showErrorLog() {
    }

    /** Returns the number of validation errors found during a load or requested
     * validation.
     */
    public int getErrorCount() {
        return parseErrors;
    }

    /** Loads the specified file using the passed progress bar.
     * @param inputFile File
     * @param progress Progress monitor
     * @throws Exception
     * @return
     */
    public boolean loadXMLFile(File inputFile, Progress progress) throws Exception {
        if (!inputFile.exists()) return false;
        progress.setUnits(4);
        progress.setValue(1);
        progress.setDescription("Parsing File");
        errorLog.setFile(inputFile);
        DOMParser parser = new DOMParser();
        try {
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setErrorHandler(this);
            parser.parse(inputFile.toURL().toString());
        } catch (Exception e) {
            return false;
        }
        document = parser.getDocument();
        root = document.getDocumentElement();
        NodeList list = root.getElementsByTagName("mev");
        if (list != null && list.getLength() > 0) mevElement = (Element) list.item(0);
        list = root.getElementsByTagName("primary_data");
        if (list != null && list.getLength() > 0) primaryDataElement = (Element) list.item(0);
        list = root.getElementsByTagName("analysis");
        if (list != null && list.getLength() > 0) analysisElement = (Element) list.item(0);
        progress.setValue(2);
        progress.setDescription("Checking Algorithm Sets");
        list = root.getElementsByTagName("alg_set");
        if (list != null && list.getLength() > 0) {
            int maxID = -1;
            for (int i = 0; i < list.getLength(); i++) {
                currAlgSetID = Integer.parseInt(((Element) list.item(i)).getAttribute("set_id"));
                if (currAlgSetID > maxID) maxID = currAlgSetID;
            }
            if (currAlgSetID < 0) currAlgSetID = 0; else currAlgSetID = maxID;
            currAlgSetID++;
        }
        progress.setValue(2);
        progress.setDescription("Setting Output ID");
        list = root.getElementsByTagName("data_node");
        if (list != null && list.getLength() > 0) {
            int maxID = -1;
            for (int i = 0; i < list.getLength(); i++) {
                currDataID = Integer.parseInt(((Element) list.item(i)).getAttribute("data_node_id"));
                if (currDataID > maxID) maxID = currDataID;
            }
            if (currDataID < 0) currDataID = 0; else currDataID = maxID;
        }
        progress.setValue(3);
        progress.setDescription("Internal Serialization");
        parsedScript = true;
        updateScript();
        progress.setValue(4);
        progress.setDescription("Done");
        progress.dispose();
        return validateDocument();
    }

    /** Validates the document
     */
    public boolean validateDocument() {
        return true;
    }

    /** Modifies a parameter
     * @param attributes parameter attributes
     * @param modLine moddification line
     * @param value value
     * @return
     */
    public boolean modifyParameter(Hashtable attributes, String modLine, String value) {
        NodeList list = document.getElementsByTagName("algorithm");
        Element algElement;
        for (int i = 0; i < list.getLength(); i++) {
            algElement = (Element) list.item(i);
            if (algorithmMatches(attributes, algElement)) {
                return modifyAlgorithmParameter(algElement, modLine, value);
            }
        }
        return false;
    }

    /** Modifies an a parameter given an elemement.
     * @param elem Element to mod.
     * @param modLine modified line
     * @param value value
     * @return
     */
    private boolean modifyAlgorithmParameter(Element elem, String modLine, String value) {
        NodeList list = elem.getElementsByTagName("param");
        Element param;
        String key;
        isTextCurrent = false;
        for (int i = 0; i < list.getLength(); i++) {
            param = (Element) list.item(i);
            key = param.getAttribute("key");
            if (modLine.indexOf(key) > -1) {
                param.setAttribute("value", value);
                fireScriptEvent();
                return true;
            }
        }
        return false;
    }

    /** Verifies algorithm identity
     * @param attributes Attribute hash
     * @param algorithm Algorithm element
     * @return
     */
    private boolean algorithmMatches(Hashtable attributes, Element algorithm) {
        Enumeration _enum = attributes.keys();
        String key, value, algValue;
        boolean match = true;
        while (_enum.hasMoreElements()) {
            key = (String) _enum.nextElement();
            value = (String) attributes.get(key);
            algValue = algorithm.getAttribute(key);
            if (!algValue.equals(value)) return false;
        }
        return true;
    }

    /** Reports Parser Exceptions (Warning level exp.)
     * @param e reported exception
     * @throws SAXException
     */
    public void warning(SAXParseException e) throws SAXException {
        System.err.println("Warning:  " + e);
        parseErrors++;
        errorLog.recordWarning(e);
    }

    /** Parse error reporting.
     */
    public void error(SAXParseException e) throws SAXException {
        System.err.println("Error:  " + e);
        errorLog.recordError(e);
        parseErrors++;
    }

    /** Parse Fatal errors
     */
    public void fatalError(SAXParseException e) throws SAXException {
        System.err.println("Fatal Error:  " + e);
        errorLog.recordFatalError(e);
        parseErrors++;
    }

    /** Adds a <CODE>ScriptEventListener</CODE> instance.
     * @param listener Listener to add
     */
    public void addDocumentListener(ScriptEventListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    /** Fires events to cached listeners.
     */
    private void fireScriptEvent() {
        ScriptDocumentEvent event = new ScriptDocumentEvent(this);
        for (int i = 0; i < listeners.size(); i++) {
            ((ScriptEventListener) listeners.elementAt(i)).documentChanged(event);
        }
    }

    /** Removes a specified listener
     * @param listener Listener to remove.
     */
    public void removeScriptListener(ScriptEventListener listener) {
        listeners.remove(listener);
    }

    /** Removes a specified algorithm from the model.
     * @param node <CODE>AlgorithmNode</CODE> to delete.
     */
    public void removeAlgorithm(AlgorithmNode node) {
        int algID = node.getID();
        int dataRef = node.getDataNodeRef();
        String name = node.getAlgorithmName();
        Element algSetElement = getAlgorithmSetByDataRef(dataRef);
        Element algorithmElement = null;
        NodeList list;
        boolean found = false;
        if (algSetElement != null) {
            list = algSetElement.getElementsByTagName("algorithm");
            for (int i = 0; i < list.getLength(); i++) {
                algorithmElement = (Element) list.item(i);
                if ((String.valueOf(algID)).equals(algorithmElement.getAttribute("alg_id")) && (name.equals(algorithmElement.getAttribute("alg_name")))) {
                    found = true;
                    break;
                }
            }
            if (found) {
                if (algorithmElement == null) return;
                Node parentNode = algorithmElement.getParentNode();
                if (parentNode != null) {
                    list = algorithmElement.getElementsByTagName("data_node");
                    String[] data_refs = new String[list.getLength()];
                    Node dataNode;
                    for (int i = 0; i < list.getLength(); i++) {
                        dataNode = list.item(i);
                        data_refs[i] = ((Element) dataNode).getAttribute("data_node_id");
                    }
                    Vector algSetIDs = getDependentDataIDs(algorithmElement, list);
                    list = analysisElement.getElementsByTagName("alg_set");
                    Node algSetNode;
                    String ref;
                    Vector setsToRemove = new Vector();
                    int listLength = list.getLength();
                    for (int i = 0; i < listLength; i++) {
                        algSetNode = list.item(i);
                        ref = ((Element) algSetNode).getAttribute("set_id");
                        for (int j = 0; j < algSetIDs.size(); j++) {
                            if (ref.equals((String) (algSetIDs.elementAt(j)))) setsToRemove.addElement(algSetNode);
                        }
                    }
                    for (int i = 0; i < setsToRemove.size(); i++) analysisElement.removeChild(((Element) (setsToRemove.elementAt(i))));
                    parentNode.removeChild(algorithmElement);
                    if (parentNode.getChildNodes().getLength() == 0) {
                        Node grandparent = parentNode.getParentNode();
                        if (grandparent != null && parentNode.getNodeName().equals("alg_set") && !(((Element) parentNode).getAttribute("set_id")).equals("1")) {
                            grandparent.removeChild(parentNode);
                        }
                    }
                }
                isTextCurrent = false;
                fireScriptEvent();
            }
        }
    }

    /** Returns the data ID's under the algorithm element
     */
    private Vector getDependentDataIDs(Element algElement, NodeList dataElements) {
        Element dataElement;
        Vector indices = new Vector();
        for (int i = 0; i < dataElements.getLength(); i++) {
            dataElement = ((Element) (dataElements.item(i)));
            indices.addElement(dataElement.getAttribute("data_node_id"));
        }
        getDependentDataIDs(indices, 0, analysisElement.getElementsByTagName("alg_set"));
        return indices;
    }

    /** accumulates data ID's below the passed data id indices
     */
    private void getDependentDataIDs(Vector indices, int start, NodeList algSets) {
        if (indices.size() <= start) return;
        int initSize = indices.size();
        int newHits = 0;
        String index, newIndex, algSetID;
        Element algSet;
        for (int i = start; i < indices.size(); i++) {
            index = (String) (indices.elementAt(i));
            for (int j = 0; j < algSets.getLength(); j++) {
                algSet = ((Element) (algSets.item(j)));
                algSetID = algSet.getAttribute("set_id");
                if (algSetID.equals(index)) {
                    NodeList dataList = algSet.getElementsByTagName("data_node");
                    for (int k = 0; k < dataList.getLength(); k++) {
                        newIndex = ((Element) dataList.item(k)).getAttribute("data_node_id");
                        if (!indices.contains(newIndex)) {
                            newHits++;
                            indices.addElement(newIndex);
                        }
                    }
                }
            }
        }
        getDependentDataIDs(indices, initSize, algSets);
    }

    private void writeObject(java.io.ObjectOutputStream oos) throws java.io.IOException {
        oos.writeObject(tm4ScriptVersion);
        oos.writeObject(mevScriptVersion);
        oos.writeObject(manager);
        oos.writeObject(document);
        oos.writeObject(scriptText);
        oos.writeObject(root);
        oos.writeObject(mevElement);
        oos.writeObject(this.primaryDataElement);
        oos.writeObject(this.commentElement);
        oos.writeObject(this.analysisElement);
        oos.writeInt(currDataID);
        oos.writeInt(currAlgSetID);
        oos.writeObject(lineSeparator);
        oos.writeObject(indent);
        oos.writeBoolean(errorLog != null);
        oos.writeBoolean(parsedScript);
    }

    private void readObject(java.io.ObjectInputStream ois) throws java.io.IOException, ClassNotFoundException {
        this.tm4ScriptVersion = (String) ois.readObject();
        this.mevScriptVersion = (String) ois.readObject();
        this.manager = (ScriptManager) ois.readObject();
        this.document = (Document) ois.readObject();
        this.scriptText = (String) ois.readObject();
        this.root = (Element) ois.readObject();
        this.mevElement = (Element) ois.readObject();
        this.primaryDataElement = (Element) ois.readObject();
        this.commentElement = (Element) ois.readObject();
        this.analysisElement = (Element) ois.readObject();
        this.currDataID = ois.readInt();
        this.currAlgSetID = ois.readInt();
        this.lineSeparator = (String) ois.readObject();
        this.indent = (String) ois.readObject();
        this.parsedScript = ois.readBoolean();
    }
}
