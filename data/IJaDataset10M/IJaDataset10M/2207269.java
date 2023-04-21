package org.ontoware.rdf2go.model;

import java.io.Serializable;
import org.ontoware.rdf2go.model.node.Node;
import org.ontoware.rdf2go.model.node.Resource;
import org.ontoware.rdf2go.model.node.URI;

/**
 * Statement representation in rdf2go
 * 
 * Implementations of Statement should have valid implementations of hashCode
 * and equals.
 * 
 * Compared by subject
 * 
 * @author mvo
 * 
 */
public interface Statement extends Comparable<Statement>, TriplePattern, Serializable {

    /**
	 * Note: this was set to be a <code>Model</code> before, but that would have
	 * caused problems, when people accesssed the model via the Statement.
	 * (compare to Jena, where graph and Model are separated. We are here on
	 * graph level)
	 * 
	 * @return the context, where this statement was created. Returns null when
	 *         not in a ModelSet.
	 */
    public URI getContext();

    /**
	 * 
	 * @return URI or BlankNode
	 */
    @Override
    public Resource getSubject();

    /**
	 * @return The URI representing the predicate (property)
	 */
    @Override
    public URI getPredicate();

    /**
	 * @return URI, String, TypedLiteral, LanguageTaggedLiteral or BlankNode
	 */
    @Override
    public Node getObject();

    /**
	 * debug output. Lazy implementation can just do nothing.
	 * 
	 * @param options undocumented :-) Depends on underlying implementation. See
	 *            source code.
	 */
    public void dump(String[] options);

    @Override
    public int hashCode();

    @Override
    public boolean equals(Object other);
}
