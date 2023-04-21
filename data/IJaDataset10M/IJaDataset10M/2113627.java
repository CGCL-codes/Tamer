package au.edu.apsr.mtk.base;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class representing the METS FContent element
 * 
 * @author Scott Yeadon
 *
 */
public class FContent extends METSElement {

    /**
     * Construct a METS FContent
     * 
     * @param n 
     *        A w3c Node, typically an Element
     * 
     * @exception METSException
     */
    public FContent(Node n) throws METSException {
        super(n, Constants.ELEMENT_FCONTENT);
    }

    /**
     * Return an empty FContent.
     * 
     * The returned object has no properties or content and is not part
     * of the METS document, it is essentially a constructor of a METS element
     * which is owned by the METS document. The returned object needs to be
     * "filled out" (e.g. with id, additional sub-elements, etc) before being
     * added to the METS document.
     * 
     * @exception METSException
     *
     */
    public FContent newFContent() throws METSException {
        return new FContent(this.newElement(Constants.ELEMENT_FCONTENT));
    }

    /**
     * Obtain the ID
     * 
     * @return String 
     *      The ID attribute value or empty string if attribute
     *      is empty or not present
     */
    public String getID() {
        return super.getAttributeValue(Constants.ATTRIBUTE_ID);
    }

    /**
     * Set the ID
     * 
     * @param id 
     *      The ID attribute value
     */
    public void setID(String id) {
        super.setAttributeValue(Constants.ATTRIBUTE_ID, id);
    }

    /**
     * Remove the ID attribute
     */
    public void removeID() {
        super.removeAttribute(Constants.ATTRIBUTE_ID);
    }

    /**
     * Obtain the use
     * 
     * @return String 
     *      The USE attribute value or empty string if attribute
     *      is empty or not present
     */
    public String getUse() {
        return super.getAttributeValue(Constants.ATTRIBUTE_USE);
    }

    /**
     * Set the use
     * 
     * @param use 
     *      The USE attribute value
     */
    public void setUse(String use) {
        super.setAttributeValue(Constants.ATTRIBUTE_USE, use);
    }

    /**
     * Remove the USE attribute
     */
    public void removeUse() {
        super.removeAttribute(Constants.ATTRIBUTE_USE);
    }

    /**
     * Obtain the xmlData wrapper node
     * 
     * @return org.w3c.dom.Node
     *      The xmlData Node of this file or <code>null</code>
     *      if none exists
     */
    public Node getXMLData() {
        NodeList nl = super.getElements(Constants.ELEMENT_XMLDATA);
        if (nl.getLength() == 1) {
            return nl.item(0).getFirstChild();
        }
        return null;
    }

    /**
     * Set the xmlData
     * 
     * @param content
     *      The content to be wrapped by the xmlData element
     */
    public void setXMLData(Element content) {
        Element xmlData = super.newElement(Constants.ELEMENT_XMLDATA);
        Node n = this.getElement().getOwnerDocument().importNode(content, true);
        xmlData.appendChild(n);
        this.getElement().appendChild(xmlData);
    }

    /**
     * Obtain base64 encoded data from the binData element.
     * it is expected that users of this class would use FLocat
     * rather than binData
     * 
     * @return String 
     *       the encoded data
     * 
     */
    public String getEncodedData() {
        NodeList nl = super.getElements(Constants.ELEMENT_BINDATA);
        if (nl.getLength() == 1) {
            return nl.item(0).getTextContent();
        }
        return null;
    }

    public void setEncodedData(String content) {
        Element binData = super.newElement(Constants.ELEMENT_BINDATA);
        binData.setTextContent(content);
        this.getElement().appendChild(binData);
    }

    /**
     * Check if this file contains base64 encoded data 
     * 
     * @return boolean
     *  <code>true</code> if this file has encoded data else
     *  <code>false</code> 
     */
    public boolean hasEncodedData() {
        NodeList nl = super.getElements(Constants.ELEMENT_BINDATA);
        if (nl.getLength() > 0) {
            return true;
        }
        return false;
    }
}
