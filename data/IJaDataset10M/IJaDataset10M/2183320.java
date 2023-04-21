package edu.ucdavis.genomics.metabolomics.binbase.dsl;

import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.Experiment;

/**
 * basic interface to generate dsls
 * @author wohlgemuth
 *
 */
public interface DSLGenerator {

    /**
	 * generates a dsl for this experiment
	 * @param experiment
	 * @param server
	 * @return
	 */
    public String generateDSL(Experiment experiment, String server);
}
