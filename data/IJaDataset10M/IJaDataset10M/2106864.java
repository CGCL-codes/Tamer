package org.apache.batik.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

/**
 * This class implements the {@link org.w3c.dom.Notation} interface.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: GenericNotation.java,v 1.1 2005/11/21 09:51:20 dev Exp $
 */
public class GenericNotation extends AbstractNotation {

    /**
     * Is this node immutable?
     */
    protected boolean readonly;

    /**
     * Creates a new Notation object.
     */
    protected GenericNotation() {
    }

    /**
     * Creates a new Notation object.
     */
    public GenericNotation(String name, String pubId, String sysId, AbstractDocument owner) {
        ownerDocument = owner;
        setNodeName(name);
        setPublicId(pubId);
        setSystemId(sysId);
    }

    /**
     * Tests whether this node is readonly.
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * Sets this node readonly attribute.
     */
    public void setReadonly(boolean v) {
        readonly = v;
    }

    /**
     * Returns a new uninitialized instance of this object's class.
     */
    protected Node newNode() {
        return new GenericNotation();
    }

    public String getBaseURI() {
        return null;
    }

    public short compareDocumentPosition(Node arg0) throws DOMException {
        return 0;
    }

    public String getTextContent() throws DOMException {
        return null;
    }

    public void setTextContent(String arg0) throws DOMException {
    }

    public boolean isSameNode(Node arg0) {
        return false;
    }

    public String lookupPrefix(String arg0) {
        return null;
    }

    public boolean isDefaultNamespace(String arg0) {
        return false;
    }

    public String lookupNamespaceURI(String arg0) {
        return null;
    }

    public boolean isEqualNode(Node arg0) {
        return false;
    }

    public Object getFeature(String arg0, String arg1) {
        return null;
    }

    public Object setUserData(String arg0, Object arg1, UserDataHandler arg2) {
        return null;
    }

    public Object getUserData(String arg0) {
        return null;
    }
}
