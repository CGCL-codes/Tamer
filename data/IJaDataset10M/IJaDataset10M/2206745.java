package com.idna.dm.service.batchtestingtool;

import static com.idna.dm.global.GlobalTestValues.LOGIN_ID_AS_UUID;
import static com.idna.dm.global.GlobalTestValues.LOGIN_ID_MATT_F_AS_UUID;
import static com.idna.dm.global.GlobalTestValues.TEST_DATA_SET_ID_AS_UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.idna.dm.domain.input.ExecutionRequestData;
import com.idna.dm.logging.activity.helper.ThreadLocalDecisionId;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:dm-entry-point.xml", "classpath:dm-services.xml", "classpath:dm-xmlparse-config-inferred-only.xml", "classpath:dm-activity-logging.xml", "classpath:dm-db-activity-logging.xml" })
public class BatchTestingToolEntryPointExecutionTests {

    @Autowired
    BatchTestingToolEntryPoint batchTestingToolEntryPoint;

    ExecutionRequestData executionRequestData;

    @Before
    public void setUp() throws Exception {
        executionRequestData = new ExecutionRequestData(LOGIN_ID_MATT_F_AS_UUID, TEST_DATA_SET_ID_AS_UUID);
        executionRequestData.setDecisionId(9);
        ThreadLocalDecisionId.setIsActivityLoggable(true);
    }

    @Test
    public void testRunBatchDecisionTest() throws Exception {
        batchTestingToolEntryPoint.runBatchDecisionTest(executionRequestData);
    }
}
