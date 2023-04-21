package com.idna.batchid.processor.batchjob;

import com.idna.batchid.model.ProductResponse;

public interface ProductResponseValidator {

    /**
	 * Verify the given response is not of type error. Only valid ones
	 * can be sent to the subsequent services. 
	 * 
	 * @param productlResponse
	 * @return
	 */
    boolean isErrorResponse(ProductResponse productlResponse);

    /**
	 * Verify if the DecisionMatrix datablock has been written to the response. 
	 * 
	 * @param xmlResponse
	 * @return
	 */
    boolean isDecisionMatrixLoaded(String xmlResponse);
}
