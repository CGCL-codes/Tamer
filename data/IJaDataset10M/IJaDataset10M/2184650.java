package com.cbsgmbh.xi.af.edifact.module.transform.xmltoedifact;

import java.util.Vector;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.cbsgmbh.xi.af.edifact.module.transform.edifact.EdifactConstants;
import com.cbsgmbh.xi.af.edifact.module.transform.edifact.EdifactStringHelper;
import com.cbsgmbh.xi.af.edifact.module.transform.util.Helper;
import com.cbsgmbh.xi.af.edifact.module.transform.xml.XmlNodeAndValues;
import com.sap.aii.af.mp.module.ModuleException;

/**
 * An XML message is parsed and the elements and their values are extracted. The
 * extracted data are made available in a vector of beans. In the further
 * processing the content of the vector is drawed on to generate an EDIFACT
 * message.
 * 
 * @author Jutta Boehme
 */
public class XmlExtractor {

    private Vector vTransactionSetElementsBeans = new Vector();

    private XmlNodeAndValues tseb = new XmlNodeAndValues();

    private Node edifactElementNode = null;

    private Vector vTransactions = new Vector();

    private int maxElementNodes = 100;

    private String[] straElements = new String[maxElementNodes];

    public static final String VERSION_ID = "$Id://OPI2_EDIFACT_TransformModule/com/cbsgmbh/opi2/xi/af/edifact/module/transform/xmltoedifact/XmlExtractor.java#1 $";

    /**
     * Constructor which receives a configuration object.
     * 
     * @param configuration
     *            ConfigurationSapImpl object
     */
    public XmlExtractor() {
    }

    /**
     * This method element checks whether an element can be assigned to a
     * transaction set name.
     * 
     * @param elementName
     *            element name
     * @return boolean true or false
     */
    private boolean isTransactionSetIdentifierCode(String elementName) {
        return (EdifactConstants.containsTRANSACTION_SET_NAME(elementName));
    }

    /**
     * This method initiates the extraction of the elements of all transactions
     * which are included in the xml.
     * 
     * @param node
     *            node
     * @return Vector vector containing vectors of TransactionSetElementBeans
     * @throws ModuleException
     */
    public Vector extractNode(Node node) throws ModuleException {
        Node node2 = null;
        NodeList nodeList = null;
        Vector vTransactionSetBeans = new Vector();
        int recursionCount = -1;
        nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            node2 = nodeList.item(i);
            node = node2;
            if (isTransactionSetIdentifierCode(node2.getNodeName())) {
                recursionCount = 0;
                vTransactionSetBeans = extractElements(node2, recursionCount);
                if (vTransactionSetBeans != null) {
                    vTransactionSetBeans = comprimeComponentElements(vTransactionSetBeans);
                    this.vTransactions.addElement(vTransactionSetBeans);
                    this.vTransactionSetElementsBeans = new Vector();
                }
            } else {
                extractNode(node2);
            }
        }
        return this.vTransactions;
    }

    /**
     * This method extracts all elements of a transaction set.
     * 
     * @param node
     *            node
     * @param recursionCount
     *            recursion level
     * @return Vector vector of TransactionSetElementBeans
     * @throws ModuleException
     */
    private Vector extractElements(Node node, int recursionCount) throws ModuleException {
        NodeList nodeList = null;
        Node node2 = null;
        int vIndex = -1;
        String strIndex = null;
        int endIndex = -1;
        String edifactElementNodeName = null;
        String parentNodeName = null;
        nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            node2 = nodeList.item(i);
            if ((node2.getNodeType() == Node.TEXT_NODE) && (node2.getNodeValue() == null) && (node2.getNodeValue().trim().equals(""))) continue;
            if ((node2.getNodeType() == Node.TEXT_NODE) && (node2.getNodeValue() != null) && (!node2.getNodeValue().trim().equals(""))) {
                if (this.edifactElementNode == null) this.edifactElementNode = node2.getParentNode().getParentNode();
                edifactElementNodeName = node2.getParentNode().getParentNode().getNodeName();
                parentNodeName = node2.getParentNode().getNodeName();
                endIndex = parentNodeName.length();
                strIndex = parentNodeName.substring(endIndex - 2, endIndex);
                if (!Helper.checkForInt(strIndex)) continue;
                vIndex = Integer.valueOf(strIndex).intValue();
                if (this.tseb.getElementName() == null) {
                    this.tseb.setElementName(edifactElementNodeName);
                } else {
                    if (!this.edifactElementNode.equals(node2.getParentNode().getParentNode())) {
                        this.tseb.setValues(this.straElements);
                        this.vTransactionSetElementsBeans.addElement(this.tseb);
                        this.straElements = new String[this.maxElementNodes];
                        this.tseb = new XmlNodeAndValues();
                        this.tseb.setElementName(edifactElementNodeName);
                    }
                }
                this.straElements[vIndex] = EdifactStringHelper.insertReleaseCharacters(node2.getNodeValue(), EdifactConstants.getUNA_RELEASE_CHARACTER(), EdifactConstants.getUNA_ELEMENT_SEPARATOR(), EdifactConstants.getUNA_COMPONENTELEMENTSEPARATOR(), EdifactConstants.getUNA_SEGMENTTERMINATOR());
                this.edifactElementNode = node2.getParentNode().getParentNode();
            } else {
                extractElements(node2, recursionCount + 1);
            }
        }
        if (recursionCount == 0) {
            this.tseb.setValues(this.straElements);
            this.vTransactionSetElementsBeans.addElement(this.tseb);
            this.straElements = new String[this.maxElementNodes];
            this.tseb = new XmlNodeAndValues();
            this.edifactElementNode = null;
        }
        return this.vTransactionSetElementsBeans;
    }

    /**
     * This method rearranges the incoming XmlNodeAndValues Vector by inserting
     * the component element content into the upper element.
     * 
     * @param vTransactionSetElementBean
     *            Vector of TransactionSetElementBeans
     * @return Vector rearranged Vector of TransactionSetElementBeans
     * @throws ModuleException
     */
    private Vector comprimeComponentElements(Vector vTransactionSetElementBean) throws ModuleException {
        String[] tsStraValues = new String[this.maxElementNodes];
        String[] prevTsStraValues = null;
        String tsName = null;
        String prevTsName = null;
        String savTsName = null;
        String componentElementValue = null;
        int endIndex = -1;
        int aIndex = -1;
        String strIndex = null;
        Vector vOutTransactionSet;
        XmlNodeAndValues tseb = null;
        vOutTransactionSet = new Vector();
        for (int i = 0; i < vTransactionSetElementBean.size(); i++) {
            tsName = ((XmlNodeAndValues) vTransactionSetElementBean.elementAt(i)).getElementName();
            tsStraValues = ((XmlNodeAndValues) vTransactionSetElementBean.elementAt(i)).getValues();
            if (((prevTsName != null) && (tsName.equals(savTsName))) || ((prevTsName != null) && (!tsName.substring(0, EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH()).equals(prevTsName.substring(0, EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH()))))) {
                tseb = new XmlNodeAndValues();
                tseb.setElementName(prevTsName.substring(0, EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH()));
                tseb.setValues(prevTsStraValues);
                vOutTransactionSet.addElement(tseb);
                prevTsStraValues = null;
            }
            if (tsName.length() > EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH()) {
                endIndex = tsName.length();
                strIndex = tsName.substring(endIndex - 2, endIndex);
                if (!Helper.checkForInt(strIndex)) continue;
                aIndex = Integer.valueOf(strIndex).intValue();
                componentElementValue = concatArrayValues(tsStraValues);
                if (prevTsStraValues == null) prevTsStraValues = new String[this.maxElementNodes];
                prevTsStraValues[aIndex] = componentElementValue;
                prevTsName = tsName.substring(0, EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH());
                savTsName = tsName;
            } else {
                if ((savTsName != null) && (savTsName.length() > EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH()) && (savTsName.substring(0, EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH()).equals(tsName.substring(0, EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH())))) {
                    endIndex = savTsName.length();
                    strIndex = savTsName.substring(endIndex - 2, endIndex);
                    if (Helper.checkForInt(strIndex)) aIndex = Integer.valueOf(strIndex).intValue();
                    if ((tsStraValues[aIndex] == null) && (!((XmlNodeAndValues) vTransactionSetElementBean.elementAt(i + 1)).getElementName().equals(savTsName)) && (Helper.getLastIndexNotNull(prevTsStraValues) < Helper.getLastIndexNotNull(tsStraValues))) {
                        for (int j = 0; j <= aIndex; j++) {
                            if (prevTsStraValues[j] != null) tsStraValues[j] = prevTsStraValues[j];
                        }
                    } else {
                        tseb = new XmlNodeAndValues();
                        tseb.setElementName(prevTsName.substring(0, EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH()));
                        tseb.setValues(prevTsStraValues);
                        vOutTransactionSet.addElement(tseb);
                        prevTsStraValues = null;
                    }
                }
                prevTsStraValues = tsStraValues;
                prevTsName = tsName.substring(0, EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH());
                savTsName = tsName;
            }
            if (i == vTransactionSetElementBean.size() - 1) {
                tseb = new XmlNodeAndValues();
                tseb.setElementName(prevTsName.substring(0, EdifactConstants.getELEMENT_NAME_PREFIX_LENGTH()));
                tseb.setValues(prevTsStraValues);
                vOutTransactionSet.addElement(tseb);
            }
        }
        return vOutTransactionSet;
    }

    /**
     * This method concatenates the elements of a string array. The component
     * element separator is set between the concatenated elements.
     * 
     * @param straValues
     *            String array
     * @return String concatenated text values
     * @throws ModuleException
     */
    private String concatArrayValues(String[] straValues) throws ModuleException {
        StringBuffer sbValues = null;
        int lastIndex = -1;
        sbValues = new StringBuffer();
        lastIndex = Helper.getLastIndexNotNull(straValues);
        for (int j = 1; j <= lastIndex; j++) {
            if (straValues[j] != null) sbValues.append(straValues[j]);
            if (j < lastIndex) sbValues.append(EdifactConstants.getUNA_COMPONENTELEMENTSEPARATOR());
        }
        return sbValues.toString();
    }
}
