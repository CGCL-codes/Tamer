package com.ext_it.mane.server;

import java.util.*;
import java.io.*;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class CUtil {

    /**
	 * Generic method to get a value from a XML Node as a string.
	 * @param oNode	start node
	 * @param sXsl 	XSL path starting at oNode to get the value for. If empty string, use oNode.
	 * @return		The string with the value at the node. 
	 */
    public static String GetXml(Node oNode, String sXsl) {
        if (sXsl.length() == 0) return oNode.getText();
        Node oNodeSub = oNode.selectSingleNode(sXsl);
        if (oNodeSub != null) {
            return oNodeSub.getText().trim();
        }
        return "";
    }

    /**
	 * Get the value of an attributed sub node, e.g. '<parent><value type="server">tazzadoro</value></parent>'
	 * @param oNode		Parent of the sub node collection, e.g. '<parent>'
	 * @param sTagName 	Name of the sub-node, e.g. 'value'
	 * @param sAttrName	Name of the Attribute, e.g. 'type'
	 * @param sAttrValue Value of the attribute, e.g. 'server'
	 * @return
	 */
    public static String GetXmlAttributedNode(Node oNode, String sTagName, String sAttrName, String sAttrValue) {
        String sXsl = sTagName + "[@" + sAttrName + "='" + sAttrValue + "']";
        return GetXml(oNode, sXsl);
    }

    public static Node GetAttributedNode(Node oNode, String sTagName, String sAttrName, String sAttrValue) {
        String sXsl = sTagName + "[@" + sAttrName + "='" + sAttrValue + "']";
        Node oNodeSub = oNode.selectSingleNode(sXsl);
        return oNodeSub;
    }

    /**
	 * Create the specified node and set is value - if the node already exists, just set the value.
	 * Only works with single node xsl operations.
	 * @param oNode
	 * @param sXsl
	 * @param sValue
	 */
    public static void SetXmlAndCreateNode(Node oNode, String sXsl, String sValue) {
        if (sXsl.length() > 0) {
            Node oNodeSub = oNode.selectSingleNode(sXsl);
            if (oNodeSub == null) CUtil.AddNode(oNode, sXsl, sValue); else CUtil.SetXml(oNode, sXsl, sValue);
        }
    }

    public static void SetXml(Node oNode, String sXsl, String sValue) {
        Node oNodeSub = null;
        if (sXsl.length() > 0) oNodeSub = oNode.selectSingleNode(sXsl); else oNodeSub = oNode;
        if (oNodeSub != null) {
            oNodeSub.setText(sValue);
        }
        oNodeSub = null;
    }

    public static Node AddNode(Node oNodeParent, String sTagName, String sValue) {
        Element el = (Element) oNodeParent;
        Element elNew = el.addElement(sTagName);
        if (sValue != "") {
            elNew.addText(sValue);
        }
        return (Node) elNew;
    }

    public static void AddNodes(Node oNodeParent, String sXPath) {
        if (oNodeParent == null) return;
        StringTokenizer st = new StringTokenizer(sXPath, "/");
        Node oNode = oNodeParent;
        while (st.hasMoreElements()) {
            String sTag = st.nextToken();
            if (sTag.length() > 0) {
                if (oNode.selectSingleNode(sTag) == null) oNode = CUtil.AddNode(oNode, sTag, ""); else oNode = oNode.selectSingleNode(sTag);
            }
        }
    }

    public static Node AddAttribute(Node oNodeParent, String sAttributeName, String sValue) {
        Element el = (Element) oNodeParent;
        Element elNew = el.addAttribute(sAttributeName, sValue);
        return (Node) elNew;
    }

    public static void RemoveAttribute(Node oNodeParent, String sAttributeName) {
        Element el = (Element) oNodeParent;
        Attribute att = el.attribute(sAttributeName);
        if (att != null) el.remove(att);
    }

    public static String GetNodeAttributeValue(Node oNode, String sXsl, String sAttribute) {
        assert sAttribute.length() > 0 : "CUtil.GetNodeAttributeValue - no attribute value passed in.";
        Node oNodeSub = null;
        if (sXsl.length() > 0) oNodeSub = oNode.selectSingleNode(sXsl); else oNodeSub = oNode;
        if (oNodeSub != null) {
            Node oNodeAtt = oNodeSub.selectSingleNode("@" + sAttribute);
            if (oNodeAtt != null) return oNodeAtt.getText();
        }
        return "";
    }

    public static void SetNodeAttributeValue(Node oNode, String sXsl, String sAttribute, String sValue) {
        assert sAttribute.length() > 0 : "CUtil.SetNodeAttributeValue - no attribute value passed in.";
        Node oNodeSub = null;
        if (sXsl.length() > 0) oNodeSub = oNode.selectSingleNode(sXsl); else oNodeSub = oNode;
        if (oNodeSub != null) {
            Node oNodeAtt = oNodeSub.selectSingleNode("@" + sAttribute);
            if (oNodeAtt != null) {
                oNodeAtt.setText(sValue);
            } else {
                CUtil.AddAttribute(oNodeSub, sAttribute, sValue);
            }
        }
    }

    /**
	 * Import a node from one DOM into another and position the new node 
	 * left of the 2nd parameter.
	 * @param oSrcNode
	 * @param oDstNodeRightSibling
	 * @return The node imported.
	 */
    public static Node ImportAndPositionNode(Node oSrcNode, Node oDstNodeRightSibling) {
        Node oNodeNew = ImportNode(oSrcNode, oDstNodeRightSibling.getParent());
        List lstParent = oDstNodeRightSibling.getParent().content();
        Element elRightSibling = (Element) oDstNodeRightSibling;
        int iIndex = elRightSibling.getParent().indexOf(oDstNodeRightSibling);
        lstParent.add(iIndex, (Element) oNodeNew);
        lstParent.remove(lstParent.size() - 1);
        return oNodeNew;
    }

    public static Node ImportNode(Node oSrcNode, Node oDstNode) {
        assert oSrcNode != null : "CUtil.ImportNode - source node is null.";
        assert oDstNode != null : "CUtil.ImportNode - destination node is null.";
        if (oSrcNode == null || oDstNode == null) return (Node) null;
        return CopyThisAndAllChildNodes(oSrcNode, oDstNode);
    }

    /**
	 * Recursively copy the current source node and all its children to the destination.
	 * @param oSrcNode	Source Node
	 * @param oDstNode	Destination Node
	 * @return	The newly created node under oDstNode.
	 */
    public static Node CopyThisAndAllChildNodes(Node oSrcNode, Node oDstNode) {
        assert oSrcNode != null : "CUtil.CopyChildNodes - source node is null.";
        assert oDstNode != null : "CUtil.CopyChildNodes - destination node is null.";
        String sNodeName = oSrcNode.getName();
        Node oNewNode = AddNode(oDstNode, sNodeName, "");
        Element el = (Element) oSrcNode;
        List<?> attrList = el.attributes();
        for (int ia = 0; ia < attrList.size(); ia++) {
            Attribute att = (Attribute) attrList.get(ia);
            String sName = att.getName();
            String sValue = att.getText();
            AddAttribute(oNewNode, sName, sValue);
        }
        for (int in = 0; in < el.nodeCount(); in++) {
            Node oNode = el.node(in);
            if (oNode.getNodeType() == Node.TEXT_NODE || oNode.getNodeType() == Node.COMMENT_NODE) {
                String sText = oNode.getText();
                oNewNode.setText(sText);
            } else {
                CopyThisAndAllChildNodes(oNode, oNewNode);
            }
        }
        return oNewNode;
    }

    /**
	 * Recursively copy all children to the destination.
	 * The node oSrcParentNode will not be copied!
	 * @param oSrcParentNode	Parent Source Node: all children of this node will be copied.
	 * @param oDstNode	Destination Node
	 */
    public static void CopyAllChildNodes(Node oSrcParentNode, Node oDstNode) {
        Element elMain = (Element) oSrcParentNode;
        for (int iChild = 0; iChild < elMain.nodeCount(); iChild++) {
            Node oNode = elMain.node(iChild);
            if (oNode.getNodeType() == Node.ELEMENT_NODE) CopyThisAndAllChildNodes(oNode, oDstNode);
        }
    }

    /**
	 * Remove all children of the node passed in.
	 * @param oNodeParent
	 */
    public static void RemoveAllChildren(Node oNodeParent) {
        if (oNodeParent == null) return;
        Element elParent = (Element) oNodeParent;
        while (elParent.nodeCount() > 0) {
            Node oNode = elParent.node(0);
            elParent.remove(oNode);
        }
    }

    public static void WriteXml(Document oDoc, String sFilename) throws IOException {
        FileWriter out = new FileWriter(sFilename);
        XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
        writer.write(oDoc);
        out.flush();
        out.close();
    }

    public static boolean IsInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
