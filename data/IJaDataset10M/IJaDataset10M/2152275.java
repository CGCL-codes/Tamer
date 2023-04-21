package com.quikj.application.web.talk.feature.proactive.messaging;

import java.util.Enumeration;
import java.util.Vector;
import net.n3.nanoxml.IXMLElement;
import org.w3c.dom.Node;
import com.quikj.application.web.talk.messaging.TalkMessageParser;

/**
 * 
 * @author amit
 */
public class ProactiveNotificationMessage implements ProactiveMessageInterface {

    private String errorMessage = "";

    private Vector sessionList = new Vector();

    /** Creates a new instance of ConferenceInformationMessage */
    public ProactiveNotificationMessage() {
    }

    public void addProactiveSessionElement(ProactiveSessionElement element) {
        sessionList.addElement(element);
    }

    public ProactiveSessionElement elementAt(int index) {
        return (ProactiveSessionElement) sessionList.elementAt(index);
    }

    public String format() {
        StringBuffer buffer = new StringBuffer("<?xml version=\"1.0\" encoding=\"us-ascii\"?>\n" + "<proactive-notification>\n");
        int size = numProactiveSessionElements();
        for (int i = 0; i < size; i++) {
            buffer.append(elementAt(i).format());
        }
        buffer.append("</proactive-notification>\n");
        return buffer.toString();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int numProactiveSessionElements() {
        return sessionList.size();
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
        return validateParameters();
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
        return validateParameters();
    }

    private boolean setElement(String name, Object element) {
        if (name.equals("proactive-session-element") == true) {
            ProactiveSessionElement session = new ProactiveSessionElement();
            if (session.parse(element) == true) {
                sessionList.addElement(session);
            } else {
                errorMessage = session.getErrorMessage();
                return false;
            }
        }
        return true;
    }

    private boolean validateParameters() {
        return true;
    }
}
