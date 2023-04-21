package org.wsml.reasoner.transformation;

import java.util.Collection;
import java.util.Set;
import org.omwg.ontology.Axiom;
import org.wsmo.common.Entity;

/**
 * An interface for the normalization of ontologies written in WSML.
 * 
 * @author Uwe Keller, DERI Innsbruck
 */
public interface OntologyNormalizer {

    /**
     * Applies a normalization step to an ontology.
     * 
     * Class that implement this interface represent some sort of transformation
     * of ontologies that are relevant for applications.
     * 
     * NOTE: Implementations are not required to give back work on a copy of the
     * original ontoloy. Thus, they could change the original ontology
     * destructively during normalization.
     * 
     * @param ontology -
     *            the ontology to be transformed (according to a normalization
     *            process)
     * @return an ontology that represents the original ontology after
     *         application of the normalization step.
     */
    public Set<Entity> normalizeEntities(Collection<Entity> theEntities);

    public Set<Axiom> normalizeAxioms(Collection<Axiom> theAxioms);
}
