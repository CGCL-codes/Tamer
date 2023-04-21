package de.fzi.kadmos.api;

import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Abstract factory interface for creating {@link Alignment} objects.
 * <p>
 * Developers providing alignment implementations are encouraged to
 * implement a corresponding factory and place it in the same package
 * as the alignment implementation class.
 *
 * @author Juergen Bock
 * @version 1.2.0
 * @since 1.0.0
 * @see Alignment
 */
public interface AlignmentFactory {

    /**
     * Creates a new alignment of two ontologies.
     *
     * @param ontology1 First ontology of the alignment to be created.
     * @param ontology2 Second ontology of the alignment to be created.
     * @return The newly created alignment.
     * @since 1.0.0
     */
    public Alignment createAlignment(OWLOntology ontology1, OWLOntology ontology2);
}
