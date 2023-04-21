package uk.ac.shef.wit.textractor.core.algorithm;

import uk.ac.shef.wit.textractor.JATRException;
import uk.ac.shef.wit.textractor.model.Term;

public interface Algorithm {

    /**
	 * Execute the algorithm by analysing the features stored in the AbstractFeatureWrapper and return terms extracted and
	 * sorted by their relevance
	 * @param store
	 * @return
	 * @throws JATRException
	 */
    Term[] execute(AbstractFeatureWrapper store) throws JATRException;

    @Override
    String toString();

    void setName(String name);
}
