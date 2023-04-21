package com.ohua.tests;

import junit.framework.Assert;
import org.junit.Test;
import com.ohua.eai.SimpleProcessListener;
import com.ohua.engine.AbstractProcessManager;
import com.ohua.engine.OhuaProcessRunner;
import com.ohua.engine.ProcessNature;
import com.ohua.engine.UserRequest;
import com.ohua.engine.UserRequestType;
import com.ohua.engine.resource.management.ResourceManager;
import com.ohua.etl.resources.DerbyServerUtils;
import com.ohua.tests.jms.AbstractJMSTestCase;

public class testFlowGraphNature extends AbstractJMSTestCase {

    /**
   * Load a flow with an EAI output operator.
   */
    @Test
    public void testSourceDrivenGraphDetection() throws Throwable {
        AbstractProcessManager manager = loadProcess(getTestMethodInputDirectory() + "source-driven-flow.xml");
        manager.initializeProcess();
        manager.awaitSystemPhaseCompletion();
        Assert.assertTrue(manager.getProcess().getProcessNature() == ProcessNature.SOURCE_DRIVEN);
        manager.runGraphAnalysisAlgorithms();
        manager.awaitSystemPhaseCompletion();
        manager.tearDownProcess();
        manager.awaitSystemPhaseCompletion();
    }

    /**
   * Load a flow with an EAI input operator.
   */
    @Test
    public void testUserDrivenGraphDetection() throws Throwable {
        AbstractProcessManager manager = loadProcess(getTestMethodInputDirectory() + "user-driven-flow.xml");
        manager.initializeProcess();
        manager.awaitSystemPhaseCompletion();
        Assert.assertTrue(manager.getProcess().getProcessNature() == ProcessNature.USER_DRIVEN);
        manager.runGraphAnalysisAlgorithms();
        manager.awaitSystemPhaseCompletion();
        manager.tearDownProcess();
        manager.awaitSystemPhaseCompletion();
        Assert.assertFalse(ResourceManager.getInstance().hasExternalActivators());
    }

    /**
   * This is the BI flow with a JMSReader in the second phase. The point is that this flow would
   * not produce any results at all if no EOS marker would be injected for Phase 1 (because of
   * the Join in Phase 2!).
   * @throws Throwable
   */
    @Test
    public void testUserDrivenWithSourceDrivenInput() throws Throwable {
        runFlowNoAssert(getTestMethodInputDirectory() + "jms-writer-sap-flow.xml");
        DerbyServerUtils.startServer(null);
        OhuaProcessRunner runner = new OhuaProcessRunner(getTestMethodInputDirectory() + "bi-flow.xml");
        runner.loadRuntimeConfiguration(getTestMethodInputDirectory() + "runtime-parameters.properties");
        SimpleProcessListener listener = new SimpleProcessListener();
        runner.register(listener);
        new Thread(runner, "bi-process").start();
        runner.submitUserRequest(new UserRequest(UserRequestType.INITIALIZE));
        listener.awaitProcessingCompleted();
        listener.reset();
        runner.submitUserRequest(new UserRequest(UserRequestType.START_COMPUTATION));
        waitForShutDown();
        runner.submitUserRequest(new UserRequest(UserRequestType.FINISH_COMPUTATION));
        listener.awaitProcessingCompleted();
        listener.reset();
        runner.submitUserRequest(new UserRequest(UserRequestType.SHUT_DOWN));
        listener.awaitProcessingCompleted();
        AbstractIOTestCase.tableRegressionCheck("derby03", "BI.LEVEL_1_TA", 8882);
        AbstractIOTestCase.tableRegressionCheck("derby03", "BI.LEVEL_2_TA", 22);
        DerbyServerUtils.shutdown();
    }
}
