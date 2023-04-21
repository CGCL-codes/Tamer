package org.ozoneDB.xml.dom;

import org.w3c.dom.*;

/**
 * Implements a notation. A notation node merely associates the notation's
 * name with its system and/or public identifiers. The notation has no contents.
 * This node is immutable.
 * <P>
 * Notes:
 * <OL>
 * <LI>Node type is {@link org.w3c.dom.Node#NOTATION_NODE}
 * <LI>Node does not support childern
 * <LI>Node does not have a value
 * <LI>Node only accessible from {@link org.w3c.dom.DocumentType}
 * </OL>
 *
 *
 * @version $Revision: 1.1 $ $Date: 2003/11/22 19:29:51 $
 * @author <a href="mailto:arkin@trendline.co.il">Assaf Arkin</a>
 * @see org.w3c.dom.Notation
 * @see NodeImpl
 */
public final class NotationImpl extends NodeImpl implements NotationProxy {

    static final long serialVersionUID = 1;

    public short getNodeType() {
        return NOTATION_NODE;
    }

    public final void setNodeValue(String value) {
        throw new DOMExceptionImpl(DOMException.NO_DATA_ALLOWED_ERR, "This node type does not support values.");
    }

    public String getPublicId() {
        return _publicID;
    }

    public void setPublicId(String publicID) {
        _publicID = publicID;
    }

    public String getSystemId() {
        return _systemID;
    }

    public void setSystemId(String systemID) {
        _systemID = systemID;
    }

    public synchronized boolean equals(Object other) {
        NotationProxy otherX;
        if (super.equals(other)) {
            otherX = (NotationProxy) other;
            return (this.getPublicId() == null && otherX.getPublicId() == null || this.getPublicId() != null && this.getPublicId().equals(otherX.getPublicId())) && (this.getSystemId() == null && otherX.getSystemId() == null || this.getSystemId() != null && this.getSystemId().equals(otherX.getSystemId()));
        }
        return false;
    }

    public final Object clone() {
        TextProxy clone = null;
        try {
            clone = (TextProxy) database().createObject(TextImpl.class.getName());
            clone.init(_ownerDocument, getNodeValue());
            cloneInto(clone, true);
        } catch (Exception except) {
            throw new DOMExceptionImpl(DOMExceptionImpl.PDOM_ERR, except.getMessage());
        }
        return clone;
    }

    public final Node cloneNode(boolean deep) {
        TextProxy clone = null;
        try {
            clone = (TextProxy) database().createObject(TextImpl.class.getName());
            clone.init(_ownerDocument, getNodeValue());
            cloneInto(clone, deep);
        } catch (Exception except) {
            throw new DOMExceptionImpl(DOMExceptionImpl.PDOM_ERR, except.getMessage());
        }
        return clone;
    }

    public String toString() {
        String name;
        name = getNodeName();
        if (name.length() > 32) {
            name = name.substring(0, 32) + "..";
        }
        if (getSystemId() != null) {
            name = name + "] SYSTEM [" + getSystemId();
        }
        if (getPublicId() != null) {
            name = name + "] PUBLIC [" + getPublicId();
        }
        return "Notation decl: [" + name + "]";
    }

    public synchronized void cloneInto(NodeProxy into, boolean deep) {
        super.cloneInto(into, deep);
        ((NotationProxy) into).setSystemId(_systemID);
        ((NotationProxy) into).setPublicId(_publicID);
    }

    protected final boolean supportsChildern() {
        return false;
    }

    /**
     * Constructor requires owner document, notation name and all its attributes.
     *
     * @param owner The owner document
     * @param name The entity name
     * @param systemID The system identifier, if specified
     * @param publicID The public identifier, if specified
     */
    public NotationImpl(DocumentImpl owner, String name, String systemID, String publicID) {
        init(owner, name, systemID, publicID);
    }

    public NotationImpl() {
        super();
    }

    public void init(DocumentProxy owner, String name, String systemID, String publicID) {
        super.init(owner, name, null, true);
        if (_systemID == null && _publicID == null) {
            throw new IllegalArgumentException("Both 'systemID' and 'publicID' are missing.");
        }
        _systemID = systemID;
        _publicID = publicID;
    }

    /**
     * The system identifier of this notation, if specified.
     */
    private String _systemID;

    /**
     * The public identifier of this notation, if specified.
     */
    private String _publicID;
}
