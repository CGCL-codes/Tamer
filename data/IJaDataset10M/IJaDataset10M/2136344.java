package com.sptci.cms.admin.view.table;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;

/**
 * A table model used to display the child nodes of the specified node.
 * The check for existence of sub-nodes must be done prior to using this
 * model.
 * 
 * <p>&copy; Copyright 2009 <a href='http://sptci.com/' target='_new'>Sans
 * Pareil Technologies, Inc.</a></p>
 *
 * @author Rakesh 2009-08-13
 * @version $Id: SubNodesTableModel.java 28 2010-02-02 20:08:42Z spt $
 */
public class SubNodesTableModel extends NodesTableModel {

    private static final long serialVersionUID = 1L;

    /** The node iterator that represents the nodes to be displayed. */
    @SuppressWarnings({ "TransientFieldNotInitialized" })
    protected transient Node node;

    public SubNodesTableModel(final Node node) {
        this.node = node;
    }

    @Override
    protected NodeIterator getNodes() throws RepositoryException {
        return node.getNodes();
    }

    public Node getNode() {
        return node;
    }
}
