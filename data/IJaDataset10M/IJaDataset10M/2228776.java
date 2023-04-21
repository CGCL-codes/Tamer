package cx.ath.contribs.internal.xerces.dom;

/**
 * Text nodes hold the non-markup, non-Entity content of
 * an Element or Attribute.
 * <P>
 * When a document is first made available to the DOM, there is only
 * one Text object for each block of adjacent plain-text. Users (ie,
 * applications) may create multiple adjacent Texts during editing --
 * see {@link org.w3c.dom.Element#normalize} for discussion.
 * <P>
 * Note that CDATASection is a subclass of Text. This is conceptually
 * valid, since they're really just two different ways of quoting
 * characters when they're written out as part of an XML stream.
 * 
 * @xerces.internal
 *
 * @version $Id: DeferredTextImpl.java,v 1.1 2007/06/02 09:58:57 paul Exp $
 * @since  PR-DOM-Level-1-19980818.
 */
public class DeferredTextImpl extends TextImpl implements DeferredNode {

    /** Serialization version. */
    static final long serialVersionUID = 2310613872100393425L;

    /** Node index. */
    protected transient int fNodeIndex;

    /**
     * This is the deferred constructor. Only the fNodeIndex is given here.
     * All other data, can be requested from the ownerDocument via the index.
     */
    DeferredTextImpl(DeferredDocumentImpl ownerDocument, int nodeIndex) {
        super(ownerDocument, null);
        fNodeIndex = nodeIndex;
        needsSyncData(true);
    }

    /** Returns the node index. */
    public int getNodeIndex() {
        return fNodeIndex;
    }

    /** Synchronizes the underlying data. */
    protected void synchronizeData() {
        needsSyncData(false);
        DeferredDocumentImpl ownerDocument = (DeferredDocumentImpl) this.ownerDocument();
        data = ownerDocument.getNodeValueString(fNodeIndex);
        isIgnorableWhitespace(ownerDocument.getNodeExtra(fNodeIndex) == 1);
    }
}
