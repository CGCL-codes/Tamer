package net.sourceforge.trust.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import net.sourceforge.trust.wf.Resolver;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WfConfig {

    private Character delimiter = '.';

    private String messages;

    private ResourceBundle bundle;

    private ArrayList<WfParser> parsers = new ArrayList<WfParser>();

    private HashMap<String, WfAction> actions = new HashMap<String, WfAction>();

    public Character getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(Character delimiter) {
        this.delimiter = delimiter;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    public HashMap<String, WfAction> getActions() {
        return actions;
    }

    public ArrayList<WfParser> getParsers() {
        return parsers;
    }

    public void read(Node node) {
        Node delimiterItem = node.getAttributes().getNamedItem(XmlNames.ELEMENT_WFCONFIG_ATTRIBUTE_DELIMITER);
        if (delimiterItem != null) {
            delimiter = delimiterItem.getNodeValue().charAt(0);
        }
        messages = node.getAttributes().getNamedItem(XmlNames.ELEMENT_WFCONFIG_ATTRIBUTE_MESSAGES).getNodeValue();
        bundle = ResourceBundle.getBundle(node.getAttributes().getNamedItem(XmlNames.ELEMENT_WFCONFIG_ATTRIBUTE_BUNDLE).getNodeValue());
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (XmlNames.ELEMENT_PARSER.equals(item.getNodeName())) {
                WfParser parser = new WfParser();
                parser.read(this, item);
                Resolver.registerParser(parser.getType(), parser.getParser());
            } else if (XmlNames.ELEMENT_ACTION.equals(item.getNodeName())) {
                WfAction action = new WfAction();
                action.read(this, item);
                actions.put(action.getUri(), action);
            }
        }
    }
}
