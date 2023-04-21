package com.gusto.engine.semsim.measures.impl;

import org.apache.log4j.Logger;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import com.gusto.engine.semsim.measures.ValueSimilarity;
import com.gusto.engine.semsim.measures.exception.SimilarityException;
import com.hp.hpl.jena.rdf.model.Literal;

public class JaroWinklerStringSimilarity implements ValueSimilarity {

    private Logger log = Logger.getLogger(JaroWinklerStringSimilarity.class);

    public String getId() {
        String id = "[" + getClass().getName() + "]";
        return id;
    }

    public Double getSimilarity(Literal value1, Literal value2) throws SimilarityException {
        JaroWinkler jw = new JaroWinkler();
        Double sim = new Double(jw.getSimilarity(value1.getString(), value2.getString()));
        if (sim.isInfinite() || sim.isNaN()) {
            log.info("Jaro Winkler " + value1.getString() + " " + value2.getString() + " " + sim + ", returning 0.0 ");
            return 0.0;
        }
        log.info("Jaro Winkler " + value1.getString() + " " + value2.getString() + " " + sim);
        return sim;
    }
}
