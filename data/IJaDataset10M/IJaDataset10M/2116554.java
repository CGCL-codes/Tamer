package com.ecyrd.jspwiki.workflow;

import java.util.Properties;
import junit.framework.TestCase;
import com.ecyrd.jspwiki.TestEngine;
import com.ecyrd.jspwiki.WikiEngine;
import com.ecyrd.jspwiki.WikiException;
import com.ecyrd.jspwiki.auth.GroupPrincipal;
import com.ecyrd.jspwiki.auth.WikiPrincipal;

public class WorkflowManagerTest extends TestCase {

    protected Workflow w;

    protected WorkflowManager wm;

    protected WikiEngine m_engine;

    protected void setUp() throws Exception {
        super.setUp();
        Properties props = new Properties();
        props.load(TestEngine.findTestProperties());
        m_engine = new TestEngine(props);
        wm = m_engine.getWorkflowManager();
        w = new Workflow("workflow.key", new WikiPrincipal("Owner1"));
        w.setWorkflowManager(m_engine.getWorkflowManager());
        Step startTask = new TaskTest.NormalTask(w);
        Step endTask = new TaskTest.NormalTask(w);
        Decision decision = new SimpleDecision(w, "decision.editWikiApproval", new WikiPrincipal("Actor1"));
        startTask.addSuccessor(Outcome.STEP_COMPLETE, decision);
        decision.addSuccessor(Outcome.DECISION_APPROVE, endTask);
        w.setFirstStep(startTask);
        w.addMessageArgument("MyPage");
    }

    public void testStart() throws WikiException {
        assertEquals(Workflow.ID_NOT_SET, w.getId());
        assertFalse(w.isStarted());
        wm.start(w);
        assertFalse(Workflow.ID_NOT_SET == w.getId());
        assertTrue(w.isStarted());
    }

    public void testWorkflows() throws WikiException {
        assertEquals(0, wm.getWorkflows().size());
        assertEquals(0, wm.getCompletedWorkflows().size());
        wm.start(w);
        assertEquals(1, wm.getWorkflows().size());
        assertEquals(0, wm.getCompletedWorkflows().size());
        Workflow workflow = (Workflow) wm.getWorkflows().iterator().next();
        assertEquals(w, workflow);
        assertEquals(1, workflow.getId());
        Decision d = (Decision) w.getCurrentStep();
        d.decide(Outcome.DECISION_APPROVE);
        assertEquals(0, wm.getWorkflows().size());
        assertEquals(1, wm.getCompletedWorkflows().size());
    }

    public void testRequiresApproval() {
        assertFalse(wm.requiresApproval("workflow.saveWikiPage"));
        assertTrue(wm.requiresApproval("workflow.foo"));
        assertTrue(wm.requiresApproval("workflow.bar"));
    }

    public void testGetApprover() throws WikiException {
        assertEquals(new WikiPrincipal("janne", WikiPrincipal.LOGIN_NAME), wm.getApprover("workflow.foo"));
        assertEquals(new GroupPrincipal("Admin"), wm.getApprover("workflow.bar"));
        try {
            assertEquals(new GroupPrincipal("Admin"), wm.getApprover("workflow.saveWikiPage"));
        } catch (WikiException e) {
            return;
        }
        fail("Workflow.bar doesn't need approval!");
    }
}
