package org.semanticweb.owlapi.api.test;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLEntity;

/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group
 * Date: 25-Oct-2006
 */
public class OWLDataPropertyTestCase extends AbstractOWLEntityTestCase {

    @Override
    protected OWLEntity createEntity(IRI iri) throws Exception {
        return getFactory().getOWLDataProperty(iri);
    }
}
