package com.kenstevens.stratdom.site.parser;

import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.kenstevens.stratdom.main.ParseConstants;
import com.kenstevens.stratdom.model.Data;
import com.kenstevens.stratdom.model.Message;
import com.kenstevens.stratdom.model.MessageList;
import com.kenstevens.stratdom.site.ParseResult;

public abstract class MessagesPageParser extends PageParser {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Autowired
    protected Data db;

    @Override
    protected void parse(Document doc, ParseResult result) {
        NodeList divs = doc.getElementsByTagName("div");
        parseGameTime(getDivWithStyle(divs, ParseConstants.GAME_TIME_DIV_STYLE));
        List<Message> messages = parseMessages(doc);
        getMessageList().addAll(messages);
    }

    abstract MessageList getMessageList();

    private List<Message> parseMessages(Document doc) {
        List<Message> messages = new ArrayList<Message>();
        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "//FORM[@name='mainform']/TABLE/TR/TD";
        Node td;
        try {
            td = (Node) xpath.evaluate(expression, doc, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e);
        }
        Node div = td.getFirstChild();
        assertNode("div", div);
        Node node = div.getNextSibling().getNextSibling().getNextSibling();
        while (moreMessages(node)) {
            assertNode("hr", node);
            node = node.getNextSibling();
            assertNode("#text", node);
            Message message = new Message();
            String dateString = node.getTextContent();
            message.setDateString(dateString);
            node = node.getNextSibling();
            assertNode("br", node);
            node = node.getNextSibling();
            assertNode("#text", node);
            node = node.getNextSibling();
            assertNode("b", node);
            String player = node.getTextContent();
            message.setPlayer(player);
            node = node.getNextSibling();
            while (moreLines(node)) {
                appendMessageFromNode(node, message);
                node = node.getNextSibling();
            }
            messages.add(message);
        }
        return messages;
    }

    protected void appendMessageFromNode(Node node, Message message) {
        if ("#text".equalsIgnoreCase(node.getNodeName())) {
            String textContent = node.getTextContent();
            textContent = StringUtils.replace(textContent, "\\\"", "\"");
            textContent = StringUtils.replace(textContent, "\\\'", "'");
            message.append(textContent);
        } else if ("br".equalsIgnoreCase(node.getNodeName())) {
            message.append("\n");
        }
    }

    private boolean moreMessages(Node node) {
        return node != null && "hr".equalsIgnoreCase(node.getNodeName());
    }

    private boolean moreLines(Node node) {
        return node != null && !"hr".equalsIgnoreCase(node.getNodeName());
    }
}
