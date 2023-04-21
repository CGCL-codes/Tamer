package com.nokia.ats4.appmodel.main.controller.simulator;

import com.nokia.ats4.appmodel.plugin.testengine.TestEngineAdapter;
import com.nokia.ats4.appmodel.model.domain.TestCommand;
import com.nokia.ats4.appmodel.script.TestScriptBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * StepExecutor handles the forwarding of test execution commands from
 * active DomainObject to test engine server. This notifies all the listeners
 * when execution has finished. The notification has <CODE>Boolean</CODE>
 * parameter which indicates wheter the execution was succesfull (passed) or
 * failed.
 *
 * @author Hannu-Pekka Hakam&uml;ki
 * @version $Revision: 16 $
 */
public class StepExecutor extends Observable implements Runnable {

    private final BlockingQueue<StepExecution> executionQueue;

    private final TestScriptBuilder scriptBuilder;

    private final TestEngineAdapter adapter;

    private final Thread executionThread;

    private String dynamicRunID;

    private List<TestCommand> nextUserEvent;

    public StepExecutor(TestEngineAdapter adapter, TestScriptBuilder builder, boolean executeInThread) {
        super();
        this.adapter = adapter;
        this.scriptBuilder = builder;
        this.executionQueue = new LinkedBlockingQueue<StepExecution>();
        if (executeInThread) {
            this.executionThread = new Thread(this);
            this.executionThread.start();
        } else {
            this.executionThread = null;
        }
    }

    public StepExecutor(TestEngineAdapter adapter) {
        this(adapter, new TestScriptBuilder(), true);
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                executeFromQueue();
            }
        } catch (InterruptedException ie) {
        } finally {
            if (this.adapter != null) {
                notify(true);
                this.adapter.stopDynamicRun(dynamicRunID);
            }
        }
    }

    public void notify(boolean passed) {
        setChanged();
        notifyObservers(new Boolean(passed));
        clearChanged();
    }

    /**
     * Execute keywords of a user event.
     * @param commands
     */
    public void executeUserEvent(List<TestCommand> commands) {
        this.nextUserEvent = commands;
    }

    /**
     * Execute keywords of a system event.
     * @param commands
     */
    public void executeSystemEvent(List<TestCommand> commands) {
        if (this.nextUserEvent == null) {
            this.nextUserEvent = new ArrayList<TestCommand>();
        }
        final StepExecution execution = new StepExecution(dynamicRunID, adapter, scriptBuilder, this.nextUserEvent, commands);
        this.executionQueue.add(execution);
        this.nextUserEvent = null;
    }

    /**
     * This ends the execution of this Thread and terminates the connection
     * to test engine.
     * Observers are notified when this process has been terminated
     */
    public void endExecution() {
        if (this.executionThread != null) {
            this.executionThread.interrupt();
        }
    }

    /**
     * Remove commands from execution queue.
     */
    public void clearCommandQueue() {
        this.executionQueue.clear();
        this.nextUserEvent = null;
    }

    /**
     * Run next step from executionQueue. 
     * 
     * Visibility is set to default to provide access to unit tests.
     */
    void executeFromQueue() throws InterruptedException {
        final StepExecution step = this.executionQueue.take();
        notify(step.execute());
    }
}
