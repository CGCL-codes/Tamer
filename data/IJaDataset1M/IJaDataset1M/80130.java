package cx.ath.contribs.internal.xerces.dom;

import java.util.Iterator;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import cx.ath.contribs.attributedTree.xml.dom.LinkedNodeList;

/**
 * DocumentFragment is a "lightweight" or "minimal" Document
 * object. It is very common to want to be able to extract a portion
 * of a document's tree or to create a new fragment of a
 * document. Imagine implementing a user command like cut or
 * rearranging a document by moving fragments around. It is desirable
 * to have an object which can hold such fragments and it is quite
 * natural to use a Node for this purpose. While it is true that a
 * Document object could fulfil this role, a Document object can
 * potentially be a heavyweight object, depending on the underlying
 * implementation... and in DOM Level 1, nodes aren't allowed to cross
 * Document boundaries anyway. What is really needed for this is a
 * very lightweight object.  DocumentFragment is such an object.
 * <P>
 * Furthermore, various operations -- such as inserting nodes as
 * children of another Node -- may take DocumentFragment objects as
 * arguments; this results in all the child nodes of the
 * DocumentFragment being moved to the child list of this node.
 * <P>
 * The children of a DocumentFragment node are zero or more nodes
 * representing the tops of any sub-trees defining the structure of
 * the document.  DocumentFragment do not need to be well-formed XML
 * documents (although they do need to follow the rules imposed upon
 * well-formed XML parsed entities, which can have multiple top
 * nodes). For example, a DocumentFragment might have only one child
 * and that child node could be a Text node. Such a structure model
 * represents neither an HTML document nor a well-formed XML document.
 * <P>
 * When a DocumentFragment is inserted into a Document (or indeed any
 * other Node that may take children) the children of the
 * DocumentFragment and not the DocumentFragment itself are inserted
 * into the Node. This makes the DocumentFragment very useful when the
 * user wishes to create nodes that are siblings; the DocumentFragment
 * acts as the parent of these nodes so that the user can use the
 * standard methods from the Node interface, such as insertBefore()
 * and appendChild().
 * 
 * @xerces.internal
 *
 * @version $Id: DocumentFragmentImpl.java,v 1.4 2007/07/13 07:23:26 paul Exp $
 * @since  PR-DOM-Level-1-19980818.
 */
public class DocumentFragmentImpl extends ParentNode implements DocumentFragment {

    /** Serialization version. */
    static final long serialVersionUID = -7596449967279236746L;

    /** Factory constructor. */
    public DocumentFragmentImpl(CoreDocumentImpl ownerDoc) {
        super(ownerDoc);
    }

    /** Constructor for serialization. */
    public DocumentFragmentImpl() {
    }

    /** 
     * A short integer indicating what type of node this is. The named
     * constants for this value are defined in the org.w3c.dom.Node interface.
     */
    public short getNodeType() {
        return Node.DOCUMENT_FRAGMENT_NODE;
    }

    /** Returns the node name. */
    public String getNodeName() {
        return "#document-fragment";
    }

    /**
     * Override default behavior to call normalize() on this Node's
     * children. It is up to implementors or Node to override normalize()
     * to take action.
     */
    public void normalize() {
        if (isNormalized()) {
            return;
        }
        if (needsSyncChildren()) {
            synchronizeChildren();
        }
        ChildNode kid, next;
        for (kid = firstChild; kid != null; kid = next) {
            next = kid.nextSibling;
            if (kid.getNodeType() == Node.TEXT_NODE) {
                if (next != null && next.getNodeType() == Node.TEXT_NODE) {
                    ((Text) kid).appendData(next.getNodeValue());
                    removeChild((NodeImpl) next);
                    next = kid;
                } else {
                    if (kid.getNodeValue() == null || kid.getNodeValue().length() == 0) {
                        removeChild((NodeImpl) kid);
                    }
                }
            }
            kid.normalize();
        }
        isNormalized(true);
    }

    protected LinkedNodeList linkedNodes;

    public DocumentFragmentImpl addLinkedNode(String key, NodeImpl node) {
        if (linkedNodes == null) linkedNodes = new LinkedNodeList();
        linkedNodes.put(key, node);
        return this;
    }

    public NodeImpl getLinkedNode(String key) {
        return linkedNodes == null ? null : linkedNodes.get(key);
    }

    public Iterator<NodeImpl> getLinkedNodes() {
        if (linkedNodes == null) linkedNodes = new LinkedNodeList();
        return linkedNodes.getLinkedNodes();
    }
}
