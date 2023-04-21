package net.sourceforge.fluxion.runcible.graph;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLObject;
import net.sourceforge.fluxion.runcible.graph.exception.UnsupportedTypeException;

/**
 * A node within the target ontology, which is used to assert that a set of
 * individuals must exist. Individuals must exist within a class - in other
 * words, the {@link OWLObject} must be set as the {@link OWLClass} into which
 * these individuals can be placed.
 *
 * @author Tony Burdett
 * @version 0.1
 * @date 17-Aug-2006
 * @see net.sourceforge.fluxion.graph.Graph
 * @see net.sourceforge.fluxion.graph.Edge
 */
public class IndividualNode extends OWLMappingNode {

    public void setOWLObject(OWLObject owlObject) throws UnsupportedTypeException {
        if (owlObject instanceof OWLClass) {
            this.owlObject = owlObject;
        } else {
            throw new UnsupportedTypeException("IndividualNode can only handle members of an OWLClass");
        }
    }
}
