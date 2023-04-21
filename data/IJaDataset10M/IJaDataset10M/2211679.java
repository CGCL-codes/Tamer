package com.idna.dm.service.execution.retail;

import com.idna.dm.domain.impl.Rule.RuleType;
import com.idna.dm.service.execution.BaseDecisionExecutionServicesIntegrationTest;
import com.idna.dm.util.IntegrationTestViewee;

public class TestDecisionRETAIL_PROVEID_3ExecutionServicesIntegrationTest extends BaseDecisionExecutionServicesIntegrationTest {

    @Override
    public void onSetUp() throws Exception {
        specifyUser(IntegrationTestViewee.JAVA_DEVELOPER);
        expectedRuleTypesOrder = new RuleType[] { RuleType.OR, RuleType.OR, RuleType.BASIC_EXPRESSION_WRAPPER, RuleType.AND };
    }
}
