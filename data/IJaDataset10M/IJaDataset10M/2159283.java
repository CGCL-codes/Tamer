package org.semanticweb.skos;

import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import java.util.Set;

/**
 * Author: Simon Jupp<br>
 * Date: Apr 21, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 * <br>
 * You can have multiple concept schemes in a <code>SKOSContainer</code>. Each concpet scheme can contain
 * concepts. This class provides some conenience methods for getting access to those concepts.
 * <br>
 * SKOS reference has a notion of importing concept schemes but the spec is not satisfactory at the moment to provide any concrete implementatiuon.
 */
public interface SKOSConceptScheme extends SKOSEntity {

    OWLClassAssertionAxiom getOWLAxiom();

    Set<SKOSConcept> getTopConcepts(SKOSContainer vocab);

    Set<SKOSConcept> getConceptsInScheme(SKOSContainer vocab);

    Set<SKOSConceptScheme> getImportedConceptSchemes(SKOSContainer vocab);
}
