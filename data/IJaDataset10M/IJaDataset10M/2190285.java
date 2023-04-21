package com.dbxml.db.client.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.*;
import com.dbxml.db.client.CollectionClient;
import com.dbxml.db.client.ResultSetClient;
import com.dbxml.db.core.query.helpers.ResultSetWrapper;
import com.dbxml.util.dbXMLException;
import com.dbxml.xml.dom.DOMHelper;
import com.dbxml.xml.dom.TextWriter;

/**
 * TextResultSetClient
 */
public final class TextResultSetClient implements ResultSetClient {

    private CollectionClient col;

    private Iterator iterator;

    private Document doc;

    private Element elem;

    private int count = -1;

    private String style;

    private String query;

    private String resCol;

    private String resKey;

    public TextResultSetClient(CollectionClient col, String text, String style, String query) {
        this.col = col;
        this.style = style;
        this.query = query;
        try {
            doc = DOMHelper.parseText(text);
            Element root = doc.getDocumentElement();
            String cs = root.getAttribute(ResultSetWrapper.COUNT);
            if (cs != null && cs.length() > 0) count = Integer.parseInt(cs);
            NodeList nl = root.getChildNodes();
            List nodes = new ArrayList(nl.getLength());
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if (n.getNodeType() == Node.ELEMENT_NODE) nodes.add(n);
            }
            iterator = nodes.iterator();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public CollectionClient getCollection() throws dbXMLException {
        return col;
    }

    public String getQueryStyle() throws dbXMLException {
        return style;
    }

    public String getQueryString() throws dbXMLException {
        return query;
    }

    public boolean next() throws dbXMLException {
        checkClosed();
        if (iterator.hasNext()) {
            elem = (Element) iterator.next();
            resCol = elem.getAttribute(ResultSetWrapper.COL);
            resKey = elem.getAttribute(ResultSetWrapper.KEY);
            return true;
        } else {
            elem = null;
            return false;
        }
    }

    public void close() throws dbXMLException {
        iterator = null;
    }

    private void checkClosed() throws dbXMLException {
        if (iterator == null) throw new dbXMLException("ResultSet is closed");
    }

    public int getCount() throws dbXMLException {
        checkClosed();
        return count;
    }

    public Node getResult() throws dbXMLException {
        checkClosed();
        if (elem != null) {
            NodeList nl = elem.getChildNodes();
            if (nl.getLength() == 1) return nl.item(0); else {
                DocumentFragment df = doc.createDocumentFragment();
                for (int i = 0; i < nl.getLength(); i++) df.appendChild(nl.item(i));
                return df;
            }
        } else return null;
    }

    public String getResultAsText() throws dbXMLException {
        Node n = getResult();
        if (n != null) return TextWriter.toString(n); else return null;
    }

    public CollectionClient getResultCollection() throws dbXMLException {
        checkClosed();
        if (resCol != null && resCol.length() > 0) return col.getClient().getCollection(resCol); else return col;
    }

    public String getResultKey() throws dbXMLException {
        checkClosed();
        return resKey;
    }
}
