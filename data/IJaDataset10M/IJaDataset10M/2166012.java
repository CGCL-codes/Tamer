package com.quikj.application.web.talk.messaging;

import net.n3.nanoxml.IXMLElement;
import org.w3c.dom.Node;

/**
 * 
 * @author amit
 */
public class TypingElement implements MediaElementInterface {

    private String errorMessage = "";

    /** Creates a new instance of BuzzElement */
    public TypingElement() {
    }

    public String format() {
        return new String("<typing/>\n");
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean parse(Object node) {
        if (TalkMessageParser.getParserType() == TalkMessageParser.DOM_PARSER) {
            return parseDOM((Node) node);
        } else {
            return parseNANO((IXMLElement) node);
        }
    }

    private boolean parseDOM(Node node) {
        return true;
    }

    private boolean parseNANO(IXMLElement node) {
        return true;
    }
}
