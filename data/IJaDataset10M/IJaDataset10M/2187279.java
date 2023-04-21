package org.fao.waicent.kids.server;

import java.io.IOException;
import org.fao.waicent.util.XMLUtil;
import org.fao.waicent.util.XMLable;
import org.fao.waicent.util.XMLableHashtable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class kidsResponse implements XMLable {

    protected kidsSession session = null;

    public kidsSession getSession() {
        return session;
    }

    protected String action = null;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    protected XMLableHashtable attributes = null;

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /********************************************************************/
    public kidsResponse(kidsSession session, XMLableHashtable attributes) {
        this.session = session;
        this.attributes = (attributes == null ? new XMLableHashtable() : attributes);
    }

    /********************************************************************/
    public kidsResponse(Document doc, Element ele) throws IOException {
        load(doc, ele);
    }

    public void save(Document doc, Element ele) throws IOException {
        XMLUtil.setType(doc, ele, this);
    }

    public void load(Document doc, Element ele) throws IOException {
        XMLUtil.checkType(doc, ele, this);
    }
}
