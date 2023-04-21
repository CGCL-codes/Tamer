package com.quikj.application.web.talk.messaging;

import java.util.Enumeration;
import java.util.Vector;
import net.n3.nanoxml.IXMLElement;
import org.w3c.dom.Node;

/**
 * 
 * @author amit
 */
public class GroupElement implements TalkMessageInterface {

    public static final String MESSAGE_TYPE = "group_element";

    private String errorMessage = "";

    private Vector elements = new Vector();

    /** Creates a new instance of GroupElement */
    public GroupElement() {
    }

    public void addElement(GroupMemberElement element) {
        elements.addElement(element);
    }

    public GroupMemberElement elementAt(int index) {
        return (GroupMemberElement) elements.elementAt(index);
    }

    public String format() {
        StringBuffer buffer = new StringBuffer("<group>\n");
        int size = numElements();
        for (int i = 0; i < size; i++) {
            buffer.append(elementAt(i).format());
        }
        buffer.append("</group>\n");
        return buffer.toString();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String messageType() {
        return MESSAGE_TYPE;
    }

    public int numElements() {
        return elements.size();
    }

    public boolean parse(Object node) {
        if (TalkMessageParser.getParserType() == TalkMessageParser.DOM_PARSER) {
            return parseDOM((Node) node);
        } else {
            return parseNANO((IXMLElement) node);
        }
    }

    private boolean parseDOM(Node node) {
        Node element = null;
        for (element = node.getFirstChild(); element != null; element = element.getNextSibling()) {
            if (element.getNodeType() == Node.ELEMENT_NODE) {
                String name = element.getNodeName();
                if (setElement(name, element) == false) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean parseNANO(IXMLElement node) {
        Enumeration e = node.enumerateChildren();
        while (e.hasMoreElements() == true) {
            IXMLElement child = (IXMLElement) e.nextElement();
            String name = child.getFullName();
            if (name == null) {
                continue;
            }
            if (setElement(name, child) == false) {
                return false;
            }
        }
        return true;
    }

    private boolean setElement(String name, Object element) {
        if (name.equals("member") == true) {
            GroupMemberElement p_element = new GroupMemberElement();
            if (p_element.parse(element) == false) {
                errorMessage = p_element.getErrorMessage();
                return false;
            } else {
                addElement(p_element);
            }
        }
        return true;
    }
}
