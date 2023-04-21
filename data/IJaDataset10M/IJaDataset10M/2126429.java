package com.idna.dm.service.execution.xml.response;

import com.idna.dm.domain.results.DecisionOutcomeSummary;

/**
 * 
 * Generates the XML Response for a Decision within a Decision Matrix.
 * 
 * @author matthew.cosgrove
 *
 */
public interface DecisionResponseGenerator {

    /**
	 * Generates the XML Response for the Decision Matrix.
	 * 
	 * @param inputXml
	 * @param outcome
	 * @return the response XML populated with the decision
	 */
    public String generateResponse(String inputXml, DecisionOutcomeSummary outcome);

    /**
	 * Generates the XML responses for DecisionMatrix with error
	 * 
	 * @param errorCode
	 * @param errorMessage
	 * @return
	 */
    String generateErrorResponse(String errorCode, String errorMessage);
}
