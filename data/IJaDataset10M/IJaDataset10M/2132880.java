package org.trdf.trdf4jena.benchmark.gen;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import org.trdf.trdf4jena.InformationConsumer;
import org.trdf.trdf4jena.TrustValue;
import org.trdf.trdf4jena.vocab.TrustVocabulary;

/**
 * A benchmark data generator that uses the tRDF trust vocabulary to express
 * trust assessments.
 *
 * @author Olaf Hartig
 */
public class TRDFTrustVocabBasedGenerator extends GeneratorBase {

    protected void createStatements(Model m, InformationConsumer ic, String graphURI, TrustValue tv) {
        Resource tvResource = m.createResource();
        tvResource.addLiteral(RDF.value, tv.getValue().floatValue());
        tvResource.addProperty(TrustVocabulary.truster, m.createResource(ic.getURI().toString()));
        Resource graphResource = m.createResource(graphURI);
        graphResource.addProperty(TrustVocabulary.trustworthiness, tvResource);
    }
}
