package com.nhncorp.usf.core.xwork;

import java.util.Map;
import com.nhncorp.usf.core.config.runtime.Action;
import com.nhncorp.usf.core.config.runtime.PageInfo;
import com.nhncorp.usf.core.config.runtime.ResultPageInfo;
import com.nhncorp.usf.core.result.ResultGenerator;
import com.nhncorp.usf.core.service.ServiceExecutor;
import com.nhncorp.usf.core.service.ServiceExecutor.RESULT_STATUS;

/**
 * @author Web Platform Development Team
 */
@SuppressWarnings("serial")
public class CommonUsfActionInvocation extends DefaultUsfActionInvocation {

    /**
     * Instantiates a new common usf action invocation.
     *
     * @param serviceExecutor the service executor
     * @param resultGenerator the result generator
     * @param pageInfo        the page info
     * @param actionInfo      the action info
     * @param dataMap         the data map
     * @throws Exception the exception
     */
    public CommonUsfActionInvocation(ServiceExecutor serviceExecutor, ResultGenerator resultGenerator, PageInfo pageInfo, Action actionInfo, Map<String, Object> dataMap) throws Exception {
        super(serviceExecutor, resultGenerator, pageInfo, actionInfo, dataMap);
    }

    /**
     *
     * @param resultStatus the result status type
     * @return the ResultPageInfo
     * @throws Exception the Exception
     */
    @Override
    protected ResultPageInfo createResultPageInfo(RESULT_STATUS resultStatus) throws Exception {
        return null;
    }
}
