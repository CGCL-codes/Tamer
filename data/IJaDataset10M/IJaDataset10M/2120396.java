package org.ozoneDB.xml.dom;

import org.w3c.dom.*;

/**
 * Implements an entity reference. Entity references are read-only when an XML
 * document is parsed, but are modifiable when an XML document is created in
 * memory.
 * <P>
 * Notes:
 * <OL>
 * <LI>Node type is {@link org.w3c.dom.Node#ENTITY_REFERENCE_NODE}
 * <LI>Node supports childern
 * <LI>Node does not have a value
 * <LI>One of two nodes that may be added to an attribute or an element
 * </OL>
 *
 *
 * @version $Revision: 1.1 $ $Date: 2003/11/22 19:29:51 $
 * @author <a href="mailto:arkin@trendline.co.il">Assaf Arkin</a>
 * @see org.w3c.dom.EntityReference
 * @see NodeImpl
 */
public final class EntityReferenceImpl extends NodeImpl implements EntityReferenceProxy {

    static final long serialVersionUID = 1;

    public short getNodeType() {
        return this.ENTITY_REFERENCE_NODE;
    }

    public final void setNodeValue(String value) {
        throw new DOMExceptionImpl(DOMException.NO_DATA_ALLOWED_ERR, "This node type does not support values.");
    }

    public final Object clone() {
        EntityReferenceProxy clone = null;
        try {
            clone = (EntityReferenceProxy) database().createObject(EntityReferenceImpl.class.getName());
            clone.init(_ownerDocument, getNodeName());
            cloneInto(clone, true);
        } catch (Exception except) {
            throw new DOMExceptionImpl(DOMExceptionImpl.PDOM_ERR, except.getMessage());
        }
        return clone;
    }

    public final Node cloneNode(boolean deep) {
        EntityReferenceProxy clone = null;
        try {
            clone = (EntityReferenceProxy) database().createObject(EntityReferenceImpl.class.getName());
            clone.init(_ownerDocument, getNodeName());
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
        return "Entity ref: [" + name + "]";
    }

    protected final boolean supportsChildern() {
        return true;
    }

    /**
     * Constructor requires only owner document and entity name.
     *
     * @param owner The owner of this document
     * @param name The entity name
     */
    EntityReferenceImpl(DocumentImpl owner, String name) {
        super(owner, name, null, true);
    }

    public EntityReferenceImpl() {
        super();
    }

    public void init(DocumentProxy owner, String name) {
        super.init(owner, name, null, true);
    }
}
