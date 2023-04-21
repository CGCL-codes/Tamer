package com.idna.dm.service.batchtestingtool.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.idna.dm.domain.input.ExecutionRequestData;
import com.idna.dm.service.batchtestingtool.BatchTestingToolEntryPoint;
import com.idna.dm.service.execution.DecisionExecutionServices;
import com.idna.dm.service.testdataset.TestDataSetHelperService;

public class SimpleBatchTestingToolEntryPointImpl implements BatchTestingToolEntryPoint {

    private TestDataSetHelperService testDataSetHelperService;

    private DecisionExecutionServices decisionExecutionServices;

    private String[] ids = { "A523EE0E-70F3-4B14-85F5-00059CEB187B" };

    private Log logger = LogFactory.getLog(this.getClass());

    @Override
    public UUID runBatchDecisionTest(ExecutionRequestData executionRequestData) {
        Map<UUID, String> testData = testDataSetHelperService.getTestDataResponses(executionRequestData);
        UUID testExecutionId = UUID.randomUUID();
        for (Map.Entry<UUID, String> entry : testData.entrySet()) {
            ExecutionRequestData requestSpecificExecutionRequestData = new ExecutionRequestData(executionRequestData, entry.getKey());
            String xmlResponse = null;
            try {
                xmlResponse = decisionExecutionServices.executeDecision(requestSpecificExecutionRequestData, entry.getValue());
            } catch (RuntimeException re) {
                logger.error(String.format("Could not process Decision %d for Search ID = %s", requestSpecificExecutionRequestData.getDecisionId(), entry.getKey().toString()), re);
                logger.error("Will continue batch test...");
            }
            logger.info("BATCH RESPONSE RETURNED FOR TEST EXECUTION " + testExecutionId + " WITH SEARCH ID " + executionRequestData.getSearchId());
            logger.info("BATCH RESPONSE IS: " + xmlResponse);
        }
        return testExecutionId;
    }

    public void setTestDataSetHelperService(TestDataSetHelperService testDataSetHelperService) {
        this.testDataSetHelperService = testDataSetHelperService;
    }

    public void setDecisionExecutionServices(DecisionExecutionServices decisionExecutionServices) {
        this.decisionExecutionServices = decisionExecutionServices;
    }

    @Override
    public List<Future<String>> executeBatch(ExecutionRequestData executionRequestData) throws Exception {
        return null;
    }
}
