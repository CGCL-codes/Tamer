package net.sourceforge.basher.internal.impl;

import junit.framework.TestCase;
import org.easymock.MockControl;
import org.apache.commons.logging.Log;
import net.sourceforge.basher.internal.Randomizer;
import net.sourceforge.basher.internal.TaskInvoker;
import net.sourceforge.basher.TaskManager;
import net.sourceforge.basher.internal.impl.TaskRunnerFactoryImpl;
import net.sourceforge.basher.internal.impl.TaskRunnerImpl;

/**
 * @author Johan Lindquist
 * @version 1.0
 */
public class TestTaskRunnerFactoryImpl extends TestCase {

    public void testCreate() {
        TaskRunnerFactoryImpl taskRunnerFactory = new TaskRunnerFactoryImpl();
        MockControl logControl = MockControl.createControl(Log.class);
        Log log = (Log) logControl.getMock();
        taskRunnerFactory.setLog(log);
        taskRunnerFactory.setMaxDelay(100);
        taskRunnerFactory.setMinTime(100);
        MockControl randomizerControl = MockControl.createControl(Randomizer.class);
        final Randomizer randomizer = (Randomizer) randomizerControl.getMock();
        taskRunnerFactory.setRandomizer(randomizer);
        MockControl taskInvokerControl = MockControl.createControl(TaskInvoker.class);
        final TaskInvoker taskInvoker = (TaskInvoker) taskInvokerControl.getMock();
        taskRunnerFactory.setTaskInvoker(taskInvoker);
        MockControl taskManagerControl = MockControl.createControl(TaskManager.class);
        final TaskManager taskManager = (TaskManager) taskManagerControl.getMock();
        taskRunnerFactory.setTaskManager(taskManager);
        log.info("Creating Task Runner");
        log.info("Task Runner Created");
        logControl.replay();
        randomizerControl.replay();
        taskInvokerControl.replay();
        taskManagerControl.replay();
        TaskRunnerImpl taskRunner = (TaskRunnerImpl) taskRunnerFactory.createTaskRunner();
        assertNotNull("Null task runner", taskRunner);
        assertNotNull("Null task runner name", taskRunner.getName());
        logControl.verify();
        randomizerControl.verify();
        taskInvokerControl.verify();
        taskManagerControl.verify();
    }
}
