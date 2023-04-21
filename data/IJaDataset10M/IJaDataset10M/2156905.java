package com.jabberwookie.ns.jabber.iq;

import java.util.Vector;
import java.util.Hashtable;
import com.ssttr.xml.XMLElement;

/**
 * This class and its sister-class DiscoInfo implement the namspaces described in JEP-0030
 * which can currently be found at <a href="http://www.jabber.org/jeps/jep-0030.html">http://www.jabber.org/jeps/jep-0030.html</a>.
 * @author  smeiners
 */
public class DiscoItems extends Query {

    public static final String NAMESPACE = "http://jabber.org/protocol/disco#items";

    /** Creates a new instance of DiscoItems */
    public DiscoItems() {
        setNameSpace(NAMESPACE);
    }

    public String getNode() {
        return getAttribute("node");
    }

    public void setNode(String node) {
        setAttribute("node", node);
    }

    public XMLElement addChild(String tag) {
        return null;
    }

    public XMLElement addChild(String tag, Hashtable attrs) {
        if (tag.equals("item")) {
            final Item i = new Item(attrs);
            addChild(i);
            return i;
        }
        return null;
    }

    public void addChild(XMLElement child) {
        if (child instanceof DiscoItems.Item) super.addChild(child);
    }

    public Vector getItems() {
        return getChildren();
    }

    public Item addItem(String jid) {
        Item i = new Item(jid);
        addChild(i);
        return i;
    }

    public Item addItem(String jid, String node) {
        Item i = new Item(jid, node);
        addChild(i);
        return i;
    }

    public Item addItem(String jid, String node, String name) {
        Item i = new Item(jid, node, name);
        addChild(i);
        return i;
    }

    public class Item extends XMLElement {

        public Item() {
            super("item");
        }

        public Item(Hashtable attrs) {
            super("item", attrs);
        }

        public Item(String jid) {
            this();
            setJID(jid);
        }

        public Item(String jid, String node) {
            this();
            setJID(jid);
            setNode(node);
        }

        public Item(String jid, String node, String name) {
            this();
            setJID(jid);
            setNode(node);
            setItemName(name);
        }

        public String getJID() {
            return getAttribute("jid");
        }

        public void setJID(String jid) {
            setAttribute("jid", jid);
        }

        public String getItemName() {
            return getAttribute("name");
        }

        public void setItemName(String name) {
            setAttribute("name", name);
        }

        public String getNode() {
            return getAttribute("node");
        }

        public void setNode(String node) {
            setAttribute("node", node);
        }

        public XMLElement addChild(String tag) {
            return null;
        }

        public XMLElement addChild(String tag, Hashtable attrs) {
            return null;
        }

        public void addChild(XMLElement child) {
        }
    }
}
