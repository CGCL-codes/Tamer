package unbbayes.prs.mebn;

import org.semanticweb.owlapi.model.IRI;
import unbbayes.prs.INode;

/**
 * This is an interface representing a node that has a reference to
 * an OWL/RDF property (through its IRI).
 * This is useful if a node represents an OWL/RDF property, and it must
 * store such references.
 * @author Shou Matsumoto
 *
 */
public interface IOWLPropertyAwareNode extends INode {

    /**
	 * The IRI of the OWL/RDF property.
	 * @return the propertyIRI
	 */
    public IRI getPropertyIRI();

    /**
	 * The IRI of the OWL/RDF property.
	 * @param propertyIRI the propertyIRI to set
	 */
    public void setPropertyIRI(IRI propertyIRI);
}
