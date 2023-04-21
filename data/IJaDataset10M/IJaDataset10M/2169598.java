package com.quikj.application.web.talk.messaging;

import net.n3.nanoxml.IXMLElement;
import org.w3c.dom.Node;

public class HREFElement implements MediaElementInterface {

    private String errorMessage = "";

    private String url = "";

    public HREFElement() {
    }

    public String format() {
        return new String("<href>" + TalkMessageParser.encodeXMLString(url) + "</href>" + '\n');
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getURL() {
        return url;
    }

    public boolean parse(Object node) {
        if (TalkMessageParser.getParserType() == TalkMessageParser.DOM_PARSER) {
            return parseDOM((Node) node);
        } else {
            return parseNANO((IXMLElement) node);
        }
    }

    private boolean parseDOM(Node node) {
        Node message_node = node.getFirstChild();
        if (message_node == null) {
            errorMessage = "There is no URL specified for the HREF element";
            return false;
        }
        url = message_node.getNodeValue();
        return true;
    }

    private boolean parseNANO(IXMLElement node) {
        url = node.getContent();
        if (url == null) {
            errorMessage = "There is no URL specified for the HREF element";
            return false;
        }
        return true;
    }

    public void setURL(String url) {
        this.url = url;
    }
}
