package org.xmldb.common.xml.queries.xt;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.xmldb.common.xml.queries.XObject;
import java.util.Enumeration;

/**
 * @version $Revision: 1.1 $ $Date: 2004/04/11 10:45:41 $
 * @author <a href="http://www.softwarebuero.de">SMB</a>
 */
public final class XObjectImpl implements XObject {

    static final long serialVersionUID = 1;

    private NodeList nodeList;

    public XObjectImpl(Enumeration e) throws IllegalArgumentException {
        if (e == null) {
            throw new IllegalArgumentException("XtXObject(): Argument was null!");
        }
        nodeList = new NodeListImpl(e);
    }

    public int getType() throws Exception {
        return XObject.CLASS_NODESET;
    }

    public boolean bool() throws Exception {
        throw new UnsupportedOperationException();
    }

    public double num() throws Exception {
        throw new UnsupportedOperationException();
    }

    public String str() throws Exception {
        return null;
    }

    public NodeList nodeset() throws Exception {
        return nodeList;
    }

    public DocumentFragment rtree() {
        return null;
    }
}
