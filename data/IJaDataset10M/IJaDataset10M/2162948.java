package org.mandarax.reference;

import org.mandarax.kernel.InferenceEngine;
import org.mandarax.kernel.InferenceEngineFeatureDescriptions;
import org.mandarax.kernel.InferenceException;
import org.mandarax.kernel.Query;
import org.mandarax.kernel.ResultSet;

/**
 * Default inference engine. Instances are just wrappers. The actual inference engine is 
 * the latest stable recommended implementation. This is to support users selecting the 
 * right engine.
 * @author <A HREF="mailto:jens.dietrich@unforgettable.com">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 2.1
 */
public class DefaultInferenceEngine implements InferenceEngine {

    private InferenceEngine delegate = new ResolutionInferenceEngine3();

    /**
	 * Get the feature descriptions.
	 * @return the feature descriptions
	 */
    public InferenceEngineFeatureDescriptions getFeatureDescriptions() {
        return delegate.getFeatureDescriptions();
    }

    /**
	 * Answer a query, retrieve (multiple different) result.
	 * The cardinality contraints describe how many results should be computed. It is either
	 * <ol>
	 * <li> <code>ONE</code> - indicating that only one answer is expected
	 * <li> <code>ALL</code> - indicating that all answers should be computed
	 * <li> <code>an integer value greater than 0 indicating that this number of results expected
	 * </ol>
	 * @see #ONE
	 * @see #ALL
	 * @return the result set of the query
	 * @param query the query clause
	 * @param kb the knowledge base used to answer the query
	 * @param aCardinalityConstraint the number of results expected
	 * @param exceptionHandlingPolicy one of the constants definied in this class (BUBBLE_EXCEPTIONS,TRY_NEXT)
	 * @throws an InferenceException
	 */
    public ResultSet query(Query query, org.mandarax.kernel.KnowledgeBase kb, int aCardinalityConstraint, int exceptionHandlingPolicy) throws InferenceException {
        return delegate.query(query, kb, aCardinalityConstraint, exceptionHandlingPolicy);
    }
}
