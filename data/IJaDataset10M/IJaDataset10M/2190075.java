package org.exist.dom;

import org.exist.numbering.NodeId;

public interface ExtNodeSet extends NodeSet {

    public NodeProxy hasDescendantsInSet(DocumentImpl doc, NodeId ancestorId, boolean includeSelf, int contextId, boolean copyMatches);

    public ByDocumentIterator iterateByDocument();

    public void setSorted(DocumentImpl document, boolean sorted);
}
