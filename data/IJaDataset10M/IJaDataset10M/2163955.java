package com.idna.dm.service.execution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.idna.common.utils.ClasspathXmlFileImporter;
import com.idna.dm.domain.input.ExecutionRequestData;
import com.idna.dm.global.GlobalTestValues;
import com.idna.dm.logging.activity.helper.ThreadLocalDecisionId;
import com.idna.dm.service.execution.DecisionExecutionServices;
import com.idna.dm.service.execution.DecisionMatrixExecutionEntryPoint;
import com.idna.dm.util.factory.FileImportHelper;
import com.idna.dm.util.factory.FileImportHelper.FileImportType;

;

public class DecisionExecutionServicesLoadTest {

    ApplicationContext ctx;

    private DecisionExecutionServices decisionMatrixRunner;

    private DecisionMatrixExecutionEntryPoint decisionMatrixEntryPoint;

    public enum FullEndToEndOrIsolatedDecisionTest {

        FULL_END_TO_END_TEST, ISOLATED_DECISION_TEST_BASED_ON_ID_ONLY
    }

    private FullEndToEndOrIsolatedDecisionTest testType;

    private List<String> requestPackets;

    private List<DecisionExecutor> decisionsToExecute;

    private ExecutorService threadExecutor;

    private ExecutionRequestData metaData;

    private boolean runWithTestDataSet;

    boolean logActivity = true;

    private boolean useTestPacketsMultipleTimes;

    private int testPacketsMultiplier;

    private Log logger = LogFactory.getLog(this.getClass());

    static final UUID LOGIN_ID = GlobalTestValues.LOGIN_ID_AS_UUID;

    static final UUID SEARCH_ID = LOGIN_ID;

    static final UUID TEST_DATA_SET_ID = LOGIN_ID;

    @Before
    public void setUp() throws Exception {
        wireUpSpring();
        testType = FullEndToEndOrIsolatedDecisionTest.FULL_END_TO_END_TEST;
        runWithTestDataSet = true;
        if (runWithTestDataSet) {
            metaData = new ExecutionRequestData(LOGIN_ID, TEST_DATA_SET_ID);
        } else {
            metaData = new ExecutionRequestData(LOGIN_ID);
        }
        requestPackets = prepareInputXML();
        useTestPacketsMultipleTimes = true;
        testPacketsMultiplier = 5000;
        decisionsToExecute = new ArrayList<DecisionExecutor>();
        decisionsToExecute = prepareTasks(useTestPacketsMultipleTimes, testPacketsMultiplier);
        logger.info("Ready to execute " + decisionsToExecute.size() + " XML Packets");
        threadExecutor = Executors.newFixedThreadPool(requestPackets.size() + 1);
    }

    private List<DecisionExecutor> prepareTasks(boolean boostBatchSize, int multiplier) {
        int sizeOfBatch = requestPackets.size();
        if (boostBatchSize) {
            sizeOfBatch = requestPackets.size() * multiplier;
        }
        List<DecisionExecutor> tasksToExecute = new ArrayList<DecisionExecutor>();
        tasksToExecute = new ArrayList<DecisionExecutor>(sizeOfBatch);
        if (!boostBatchSize) {
            for (String xml : requestPackets) {
                tasksToExecute.add(new DecisionExecutor(xml));
            }
        } else {
            for (int i = 0; i < sizeOfBatch; i++) {
                int packetIndex = i % requestPackets.size();
                tasksToExecute.add(new DecisionExecutor(requestPackets.get(packetIndex)));
            }
        }
        return tasksToExecute;
    }

    @Test
    public void loadTestDecisionMatrix() throws Exception {
        if (threadExecutor == null) throw new UnsupportedOperationException("threadExecutor is null, test aborted.");
        threadExecutor.invokeAll(decisionsToExecute);
    }

    @Test
    public void quickWayOfRunnningAFullOnEndToEndTestForADecisionMatrix() throws Exception {
        decisionMatrixEntryPoint.runDecisionMatrix(metaData, null);
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    private List<String> prepareInputXML() throws Exception {
        List<String> xmlPackets = new ArrayList<String>();
        FileImportType fileType = null;
        switch(testType) {
            case ISOLATED_DECISION_TEST_BASED_ON_ID_ONLY:
                fileType = FileImportType.BARE_BONES;
                break;
            case FULL_END_TO_END_TEST:
                fileType = FileImportType.BARE_BONES_FOR_LOAD;
                break;
        }
        List<String> files = FileImportHelper.populateFilesToTest(fileType);
        for (String fileName : files) {
            xmlPackets.add(ClasspathXmlFileImporter.importXMLFromClasspath(fileName));
        }
        return xmlPackets;
    }

    int incrementingDecision = 1;

    class DecisionExecutor implements Callable<String> {

        private String inputXml;

        public DecisionExecutor(String xml) {
            this.inputXml = xml;
        }

        @Override
        public String call() throws Exception {
            ThreadLocalDecisionId.setIsActivityLoggable(logActivity);
            System.err.println("Callable started " + (logActivity ? "WITH" : "WITHOUT") + " Activity Logging Configured");
            String xmlResponse = null;
            switch(testType) {
                case ISOLATED_DECISION_TEST_BASED_ON_ID_ONLY:
                    metaData.setSearchId(SEARCH_ID);
                    if (incrementingDecision > 8) {
                        incrementingDecision = 1;
                    }
                    metaData.setDecisionId(incrementingDecision++);
                    xmlResponse = decisionMatrixRunner.executeDecision(metaData, inputXml);
                    break;
                case FULL_END_TO_END_TEST:
                    xmlResponse = decisionMatrixEntryPoint.runDecisionMatrix(metaData, inputXml);
                    break;
            }
            logger.trace(xmlResponse);
            return xmlResponse;
        }
    }

    private void wireUpSpring() {
        ctx = new ClassPathXmlApplicationContext(new String[] { "classpath:dm-entry-point.xml", "classpath:dm-xmlparse-config-inferred-only.xml", "classpath:dm-services.xml", "classpath:dm-activity-logging.xml" });
        decisionMatrixRunner = (DecisionExecutionServices) ctx.getBean("decisionExecutionServices");
        decisionMatrixEntryPoint = (DecisionMatrixExecutionEntryPoint) ctx.getBean("decisionMatrixEntryPoint");
    }

    public static void main(String[] args) throws Exception {
        DecisionExecutionServicesLoadTest loadTest = new DecisionExecutionServicesLoadTest();
        pauseFromConsole();
        while (true) {
            loadTest.setUp();
            loadTest.loadTestDecisionMatrix();
            allowExitFromConsole();
        }
    }

    private static void pauseFromConsole() throws Exception {
        System.out.println("Press Enter To Start First Load Test...");
        while (System.in.read() != '\n') ;
    }

    private static void allowExitFromConsole() throws Exception {
        System.out.println("Press 'q' and Enter To Exit Load Test or just Enter to run again...");
        String line = new BufferedReader(new InputStreamReader(System.in)).readLine();
        if (line.equals("q")) {
            System.exit(0);
        }
    }
}
