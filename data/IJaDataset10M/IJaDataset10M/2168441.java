package org.nixus.core.structure.auxiliary;

import org.nixus.core.structure.nodes.Node;

/**
 * Object to be implemented to visit a node content, for example during a traversal 
 * */
public interface NodeVisitor {

    /**
	 * Used to visit a node during a graph traversal
	 * */
    public void visit(Node node);
}
