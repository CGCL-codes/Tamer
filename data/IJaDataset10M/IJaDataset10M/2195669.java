package com.extentech.ExtenBean.swing;

import org.jdom.*;

/** TreeNode backed by an XML DataObject 
 * 
 *
 */
public class XMLBeanNode extends ExtenBeanTreeNode {

    public static final String DEFAULTNAME = "[Root]";

    /** create a new child node which
		conforms to the type of node
		contained by this node.
	 */
    public ExtenBeanTreeNode getNewChildNode(String nodetype) {
        XMLBeanNode newnode = new XMLBeanNode();
        return super.getNewChildNode(newnode, nodetype);
    }

    private String xmlfname = "";

    private Element myelement = null;

    public String getNodeType() {
        return this.getKeyColName();
    }

    public String getXMLFileName() {
        return xmlfname;
    }

    public void setXMLFileName(String s) {
        this.xmlfname = s;
    }

    /**
	 * @return
	 */
    public Element getElement() {
        return myelement;
    }

    /**
	 * @param element
	 */
    public void setElement(Element element) {
        myelement = element;
    }
}
