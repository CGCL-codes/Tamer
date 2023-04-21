package org.slasoi.seval.prediction;

import java.util.List;
import org.slasoi.models.scm.ServiceBuilder;

/**
 * Represents the results of one evaluated service realization.
 * 
 * @author kuester, brosch
 */
public interface IEvaluationResult {

    /**
	 * Sets single results that constitute the overall evaluation result.
	 * @param resultSet the single results to set
	 */
    void addResult(ISingleResult result);

    /**
     * Returns the service builder that describes the service realization.
     * @return the service builder
     */
    ServiceBuilder getBuilder();

    /**
	 * Retrieves the set of single results that constitute the overall evaluation result.
	 * @return the set of single results
	 */
    List<ISingleResult> getResults();

    /**
	 * Sorts the list of result entries.
	 */
    void sort();
}
