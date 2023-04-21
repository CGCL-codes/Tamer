package org.herakles.ml.selection.trainingData.reasonerTest.test.noOwllink.reasoners;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import com.clarkparsia.pellet.owlapiv3.PelletReasoner;
import com.clarkparsia.pellet.owlapiv3.PelletReasonerFactory;

public class Pellet {

    private PelletReasonerFactory factory;

    public Pellet() {
        factory = PelletReasonerFactory.getInstance();
    }

    public OWLReasoner getReasoner(OWLOntology ontology) {
        OWLReasonerConfiguration config = new SimpleConfiguration(ReasonerPara.TIMEOUT);
        PelletReasoner reasoner = factory.createReasoner(ontology, config);
        return reasoner;
    }
}
