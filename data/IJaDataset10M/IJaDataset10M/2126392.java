package com.idna.trace.searchrulesengine;

import net.icdpublishing.dream.domain.complex.SearchRequest;
import com.idna.trace.searchrulesengine.rulesengine.RulesEngine;
import com.idna.trace.searchrulesengine.validators.RequestValidatorImpl;
import com.idna.trace.utils.SearchType;
import com.idna.trace.utils.parameters.consumer.ParametersConsumer;

public class SearchRulesEngineImpl implements SearchRulesEngine<ParametersConsumer> {

    private final RulesEngine rulesEngine;

    private final RequestValidatorImpl requestValidator;

    public SearchRulesEngineImpl(RulesEngine rulesEngine, RequestValidatorImpl requestValidator) {
        this.rulesEngine = rulesEngine;
        this.requestValidator = requestValidator;
    }

    public SearchRulesEngineRequest getDreamRequest(ParametersConsumer parameters) {
        SearchRequest dreamRequest = rulesEngine.buildDreamRequest(parameters);
        SearchType searchType = parameters.getLogicsParameters().getSearchType();
        if (searchType.checkBTRules()) return new SearchRulesEngineRequest(dreamRequest, requestValidator.validate(dreamRequest), searchType); else return new SearchRulesEngineRequest(dreamRequest, searchType);
    }
}
