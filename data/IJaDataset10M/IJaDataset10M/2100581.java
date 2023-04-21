package org.xmldb.common.xml.queries.xalan2;

import org.w3c.dom.DocumentFragment;
import org.w3c.dom.NodeList;
import org.xmldb.common.xml.queries.XObject;

/**
 * Wrapper for the Xalan XObject and maybe other XPath implementation
 * specific things.
 * @version $Revision: 1.1 $ $Date: 2004/04/11 10:45:41 $
 * @author <a href="http://www.softwarebuero.de">SMB</a>
 */
public final class XObjectImpl implements XObject {

    private org.apache.xpath.objects.XObject _xobj = null;

    /**
     * Creates a new XalanXObject.
     * @param xobj Xalans native XObject that should be wrapped.
     * @exception IllegalArgumentException If the given XObject was null.
     */
    public XObjectImpl(org.apache.xpath.objects.XObject xobj) throws IllegalArgumentException {
        if (xobj == null) throw new IllegalArgumentException("XalanXObject(): Argument was null!");
        _xobj = xobj;
    }

    public int getType() {
        return _xobj.getType();
    }

    public boolean bool() throws javax.xml.transform.TransformerException {
        return _xobj.bool();
    }

    public double num() throws javax.xml.transform.TransformerException {
        return _xobj.num();
    }

    public String str() {
        return _xobj.str();
    }

    public NodeList nodeset() throws javax.xml.transform.TransformerException {
        return (NodeList) _xobj.nodelist();
    }

    public DocumentFragment rtree() {
        return _xobj.rtree();
    }
}
