package com.idna.dm.service.reporting.impl;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.idna.dm.dao.reporting.DecisionEngineTestDao;
import com.idna.dm.domain.reporting.DecisionRuleLogReportRow;
import com.idna.dm.domain.reporting.DecisionRuleLogSearchesReportRow;
import com.idna.dm.service.reporting.DecisionEngineTestService;

@Component("decisionEngineTestService")
public class DecisionEngineTestServiceImpl implements DecisionEngineTestService {

    @Autowired
    private DecisionEngineTestDao decisionEngineTestDao;

    @Override
    public List<DecisionRuleLogSearchesReportRow> getReportingDecisionTestDetail(UUID loginId, Integer decisionId, Integer outcomeId, String startDate, String endDate, UUID testExecId) {
        return decisionEngineTestDao.getReportingDecisionTestDetail(loginId, decisionId, outcomeId, startDate, endDate, testExecId);
    }

    @Override
    public List<DecisionRuleLogSearchesReportRow> getReportingDecisionTestRuleDetail(UUID loginId, Integer decisionId, Integer ruleId, Integer outcomeId, String startDate, String endDate, UUID testExecId) {
        return decisionEngineTestDao.getReportingDecisionTestRuleDetail(loginId, decisionId, ruleId, outcomeId, startDate, endDate, testExecId);
    }

    @Override
    public DecisionRuleLogReportRow getReportingDecisionTestRuleSummary(UUID loginId, Integer decisionId, Integer ruleId, String startDate, String endDate, UUID testExecId) {
        return decisionEngineTestDao.getReportingDecisionTestRuleSummary(loginId, decisionId, ruleId, startDate, endDate, testExecId);
    }
}
