package de.fuberlin.wiwiss.jenaext;

import com.hp.hpl.jena.graph.Node;

/**
 * A dictionary that assigns identifiers to RDF nodes.
 *
 * @author Olaf Hartig
 */
public interface NodeDictionary {

    /**
	 * Returns the node identified by the given identifier (or null).
	 */
    public Node getNode(int id);

    /**
	 * Returns the identifier that identifies the given node (or -1).
	 */
    public int getId(Node n);

    /**
	 * Returns an identifier that identifies the given node.
	 * If there is no identifier for the given node yet this method creates a
	 * new identifier and adds it to the dictionary.
	 */
    public int createId(Node n);
}
