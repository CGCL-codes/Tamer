package org.exist.xquery;

import org.exist.dom.DocumentImpl;
import org.exist.dom.NodeProxy;
import org.exist.numbering.NodeId;

/**
 * @author Wolfgang Meier (wolfgang@exist-db.org)
 */
public interface NodeSelector {

    public NodeProxy match(DocumentImpl doc, NodeId nodeId);
}
