package com.wpjr.simulator.system;

import java.util.Queue;
import com.wpjr.simulator.entity.action.Action;
import com.wpjr.simulator.entity.action.GeneralAction;
import com.wpjr.simulator.system.Scheduler;
import com.wpjr.simulator.system.SimulationException;
import junit.framework.TestCase;

public class TestScheduler extends TestCase {

    private Scheduler scheduler;

    protected void setUp() throws Exception {
        scheduler = Scheduler.getInstance();
        scheduler.reset();
    }

    protected void tearDown() throws Exception {
        scheduler = null;
    }

    public void testGetActions2() throws SimulationException {
        GeneralAction ga = new GeneralAction(0);
        GeneralAction ga1 = new GeneralAction(0);
        GeneralAction ga2 = new GeneralAction(0);
        GeneralAction ga3 = new GeneralAction(1);
        scheduler.schedule(ga);
        scheduler.schedule(ga1);
        scheduler.schedule(ga3);
        scheduler.schedule(ga2);
        scheduler.setCurrentTime(0);
        scheduler.run();
        assertTrue(scheduler.getActions().contains(ga3));
        scheduler.setCurrentTime(1);
        scheduler.run();
        assertEquals(0, scheduler.getActions().size());
    }

    public void testReset() throws SimulationException {
        Action generalAction = new GeneralAction(0);
        scheduler.schedule(generalAction);
        Action action = scheduler.run();
        assertEquals(1, scheduler.getActionsAlreadyExecuted());
        assertEquals(action.getScheduleTime(), generalAction.getScheduleTime());
        assertEquals(0, action.getScheduleTime());
        scheduler.reset();
        assertEquals(0, scheduler.getCurrentTime());
        assertEquals(Action.MINIMUM_STEP_LEVEL, scheduler.getMinimumStepLevel());
        assertEquals(0, scheduler.getActionsAlreadyExecuted());
        assertEquals(null, scheduler.getNextActionToBeExecuted());
        assertEquals("There should not be any actions in the scheduler", 0, scheduler.getActions().size());
    }

    public void testSchedule() {
        Action generalAction = new GeneralAction(0);
        assertTrue("The action should not have been scheduled yet", !scheduler.getActions().contains(generalAction));
        assertEquals("There should not be any actions in the scheduler", 0, scheduler.getActions().size());
        scheduler.schedule(generalAction);
        assertEquals("There should not be any actions in the scheduler", 1, scheduler.getActions().size());
        assertTrue("The action should have been scheduled", scheduler.getActions().contains(generalAction));
    }

    public void testHasMoreScheduledActions() {
        scheduler.reset();
        Action generalAction = new GeneralAction(0);
        assertFalse("The scheduler should not have any scheduled actions", scheduler.hasMoreScheduledActionsConsideringScheduleTime());
        assertEquals("There should not be any actions in the scheduler", 0, scheduler.getActions().size());
        scheduler.schedule(generalAction);
        assertTrue("The scheduler should have one scheduled action", scheduler.hasMoreScheduledActionsConsideringScheduleTime());
        assertEquals("There should not be any actions in the scheduler", 1, scheduler.getActions().size());
        assertFalse("The action should have been executed", generalAction.isActionExecuted());
    }

    public void testHasMoreScheduledActionsAndRun() throws SimulationException {
        Action generalAction = new GeneralAction(0);
        assertFalse("The scheduler should not have any scheduled actions", scheduler.hasMoreScheduledActionsConsideringScheduleTime());
        assertEquals("There should not be any actions in the scheduler", 0, scheduler.getActions().size());
        scheduler.schedule(generalAction);
        assertTrue("The scheduler should have one scheduled action", scheduler.hasMoreScheduledActionsConsideringScheduleTime());
        assertEquals("There should be 1 action in the scheduler", 1, scheduler.getActions().size());
        assertTrue("The action should have been scheduled", scheduler.getActions().contains(generalAction));
        assertFalse("The action should have been executed", generalAction.isActionExecuted());
        scheduler.run();
        assertFalse("The scheduler should not have any scheduled actions", scheduler.hasMoreScheduledActionsConsideringScheduleTime());
        assertEquals("There should not be any actions in the scheduler", 0, scheduler.getActions().size());
        assertFalse("The action should have been scheduled", scheduler.getActions().contains(generalAction));
        assertTrue("The action should have been executed", generalAction.isActionExecuted());
    }

    public void testGetInstance() {
        Scheduler instance = null;
        instance = Scheduler.getInstance();
        assertNotNull(instance);
        assertEquals(instance, scheduler);
    }

    public void testGetCurrentTime() throws SimulationException {
        Action generalAction = new GeneralAction(0);
        scheduler.schedule(generalAction);
        Action action = scheduler.run();
        assertTrue("The action should have been executed", action.isActionExecuted());
        assertEquals(1, scheduler.getCurrentTime());
        Action generalAction2 = new GeneralAction(1);
        assertEquals(1, scheduler.getCurrentTime());
        scheduler.schedule(generalAction2);
        assertEquals(1, scheduler.getCurrentTime());
        action = scheduler.run();
        assertEquals(2, scheduler.getCurrentTime());
        assertTrue("The action should have been executed", action.isActionExecuted());
    }

    public void testRun() throws SimulationException {
        Action generalAction = new GeneralAction(10);
        scheduler.schedule(generalAction);
        assertEquals("The current time for the scheduler should be zero", 0, scheduler.getCurrentTime());
        assertFalse("The action should not have been executed", generalAction.isActionExecuted());
        scheduler.run();
        assertFalse("The action should not have been executed", generalAction.isActionExecuted());
        scheduler.setCurrentTime(10);
        scheduler.run();
        assertTrue("The action should have been executed", generalAction.isActionExecuted());
        assertEquals("The current time should be equal to the action", 11, scheduler.getCurrentTime());
        assertFalse("The scheduler should not have any scheduled actions", scheduler.hasMoreScheduledActionsConsideringScheduleTime());
    }

    public void testRunMoreActions() throws SimulationException {
        Action generalAction = new GeneralAction(0);
        Action generalAction2 = new GeneralAction(0);
        scheduler.schedule(generalAction);
        scheduler.schedule(generalAction2);
        Action action = scheduler.step();
        assertTrue("The action should have been executed", generalAction.isActionExecuted());
        assertEquals("The current time should be equal to the first action", 0, scheduler.getCurrentTime());
        assertTrue("The scheduler should have 1 scheduled action", scheduler.hasMoreScheduledActionsConsideringScheduleTime());
        scheduler.step();
        assertTrue("The action should have been executed", generalAction2.isActionExecuted());
    }

    public void testStepMoreActions2() throws SimulationException {
        Action generalAction = new GeneralAction(0);
        scheduler.schedule(generalAction);
        Action generalAction2 = new GeneralAction(1);
        scheduler.schedule(generalAction2);
        Action generalAction3 = new GeneralAction(2);
        scheduler.schedule(generalAction3);
        Action action = scheduler.step();
        assertTrue("The action should have been executed", action.isActionExecuted());
        assertEquals("The current time should be equal to the first action", 0, scheduler.getCurrentTime());
        assertEquals("The scheduler should have 2 scheduled action", 2, scheduler.getActions().size());
        action = scheduler.step();
        assertTrue("The action should have been executed", action.isActionExecuted());
        assertEquals("The current time should be equal to the second action", 1, scheduler.getCurrentTime());
        assertEquals("The scheduler should have 1 scheduled action", 1, scheduler.getActions().size());
        action = scheduler.step();
        assertTrue("The action should have been executed", action.isActionExecuted());
        assertEquals("The current time should be equal to the third action", 2, scheduler.getCurrentTime());
        assertEquals("The scheduler should have 0 scheduled action", 0, scheduler.getActions().size());
    }

    public void testStepWithoutActions() throws SimulationException {
        try {
            scheduler.step();
        } catch (Throwable e) {
            assertTrue("It was expected to have a simulation exception", e instanceof SimulationException);
        }
    }

    public void testGetMinimumStepLevel() throws SimulationException {
        assertEquals("The minimum expected level should be equal", 0, scheduler.getMinimumStepLevel());
        Action generalAction = new GeneralAction(0);
        generalAction.setStepLevel(4);
        scheduler.setMinimumStepLevel(3);
        scheduler.schedule(generalAction);
        Action action = scheduler.step();
        assertEquals(true, action.isActionExecuted());
        assertEquals("The minimum expected level should be equal", 3, scheduler.getMinimumStepLevel());
        Action generalAction2 = new GeneralAction(0);
        generalAction2.setStepLevel(2);
        scheduler.schedule(generalAction2);
        action = scheduler.step();
        assertEquals(true, action.isActionExecuted());
        scheduler.reset();
        assertEquals("The minimum expected level should be equal", 0, scheduler.getMinimumStepLevel());
    }

    public void testSetMinimumStepLevel() {
        assertEquals("The minimum expected level should be equal", 0, scheduler.getMinimumStepLevel());
        scheduler.setMinimumStepLevel(2);
        assertEquals("The minimum expected level should be equal", 2, scheduler.getMinimumStepLevel());
    }

    public void testGetActionsAlreadyExecuted() throws SimulationException {
        assertEquals("The number of actions executed should be equal", 0, scheduler.getActionsAlreadyExecuted());
        Action generalAction = new GeneralAction(0);
        scheduler.schedule(generalAction);
        Action generalAction2 = new GeneralAction(0);
        scheduler.schedule(generalAction2);
        scheduler.run();
        assertEquals("The number of actions executed should be equal", 2, scheduler.getActionsAlreadyExecuted());
    }

    public void testGetActionsAlreadyExecuted2() throws SimulationException {
        assertEquals("The number of actions executed should be equal", 0, scheduler.getActionsAlreadyExecuted());
        Action generalAction = new GeneralAction(0);
        scheduler.schedule(generalAction);
        Action generalAction2 = new GeneralAction(0);
        scheduler.schedule(generalAction2);
        scheduler.step();
        assertEquals("The number of actions executed should be equal", 1, scheduler.getActionsAlreadyExecuted());
        scheduler.step();
        assertEquals("The number of actions executed should be equal", 2, scheduler.getActionsAlreadyExecuted());
    }

    public void testGetActionsWaitingToBeExecutedViaStep() throws SimulationException {
        assertEquals("The number of actions to be executed should be equal", 0, scheduler.getActionsWaitingToBeExecuted());
        Action generalAction = new GeneralAction(0);
        scheduler.schedule(generalAction);
        assertEquals("The number of actions to be executed should be equal", 1, scheduler.getActionsWaitingToBeExecuted());
        Action generalAction2 = new GeneralAction(0);
        scheduler.schedule(generalAction2);
        assertEquals("The number of actions to be executed should be equal", 2, scheduler.getActionsWaitingToBeExecuted());
        scheduler.step();
        assertEquals("The number of actions to be executed should be equal", 1, scheduler.getActionsWaitingToBeExecuted());
        scheduler.step();
        assertEquals("The number of actions to be executed should be equal", 0, scheduler.getActionsWaitingToBeExecuted());
    }

    public void testGetActionsWaitingToBeExecutedViaRun() throws SimulationException {
        assertEquals("The number of actions to be executed should be equal", 0, scheduler.getActionsWaitingToBeExecuted());
        Action generalAction = new GeneralAction(0);
        scheduler.schedule(generalAction);
        assertEquals("The number of actions to be executed should be equal", 1, scheduler.getActionsWaitingToBeExecuted());
        Action generalAction2 = new GeneralAction(0);
        scheduler.schedule(generalAction2);
        assertEquals("The number of actions to be executed should be equal", 2, scheduler.getActionsWaitingToBeExecuted());
        scheduler.run();
        assertEquals("The number of actions to be executed should be equal", 0, scheduler.getActionsWaitingToBeExecuted());
    }

    public void testGetActions() throws SimulationException {
        Queue<Action> actions = scheduler.getActions();
        assertEquals(0, actions.size());
        Action generalAction = new GeneralAction(0);
        scheduler.schedule(generalAction);
        assertEquals(1, actions.size());
        assertEquals(1, scheduler.getActions().size());
        assertTrue(scheduler.getActions().contains(generalAction));
        scheduler.schedule(generalAction);
        assertEquals(1, scheduler.getActions().size());
        assertTrue(scheduler.getActions().contains(generalAction));
        scheduler.step();
        assertTrue(!scheduler.getActions().contains(generalAction));
        assertEquals(0, scheduler.getActions().size());
    }
}
