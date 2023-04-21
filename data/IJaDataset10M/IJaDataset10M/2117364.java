package uk.co.ordnancesurvey.rabbitparser.owlapiconverter;

import org.semanticweb.owlapi.model.OWLOntology;

public interface RbtDefaultTargetOntologyProvider {

    /**
	 * Returns the default target ontology that should be used during rabbit
	 * parsing, when no explicit target ontology is specified. This method
	 * always must return a valid {@link OWLOntology}, if no {@link OWLOntology}
	 * can be returned, this method will throw a {@link RuntimeException}.
	 * 
	 * @return
	 */
    OWLOntology getDefaultTargetOntology();
}
