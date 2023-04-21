package org.ucl.xpath.ast;

/**
 * Support for Step operations.
 */
public abstract class Step extends XPathNode {

    private NodeTest _node_test;

    /**
	 * Constructor for Step.
	 * @param node_test Nodes for operation.
	 */
    public Step(NodeTest node_test) {
        _node_test = node_test;
    }

    /**
	 * Support for NodeTest interface.
	 * @return Result of NodeTest operation.
	 */
    public NodeTest node_test() {
        return _node_test;
    }
}
