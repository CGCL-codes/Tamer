package com.idna.dm.service.execution.finance;

import org.junit.Ignore;
import com.idna.dm.domain.impl.Rule.RuleType;
import com.idna.dm.service.execution.BaseDecisionExecutionServicesIntegrationTest;
import com.idna.dm.util.IntegrationTestViewee;

/**
 * Performs an end to end test on RETAIL_CHECKID_1.
 * 
 * @author oren.berenson
 * 
 */
@Ignore
public class TestFinanceKYC1ExecutionServicesIntegrationTest extends BaseDecisionExecutionServicesIntegrationTest {

    @Override
    public void onSetUp() throws Exception {
        specifyUser(IntegrationTestViewee.JAVA_DEVELOPER);
        expectedRuleTypesOrder = new RuleType[] { RuleType.AND, RuleType.BASIC_EXPRESSION_WRAPPER, RuleType.OR, RuleType.OR, RuleType.OR, RuleType.BASIC_EXPRESSION_WRAPPER, RuleType.OR, RuleType.OR, RuleType.OR, RuleType.BASIC_EXPRESSION_WRAPPER, RuleType.AND, RuleType.BASIC_EXPRESSION_WRAPPER, RuleType.BASIC_EXPRESSION_WRAPPER };
    }
}
