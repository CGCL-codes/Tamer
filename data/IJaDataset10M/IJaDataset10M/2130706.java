package com.nokia.ats4.appmodel.adapter;

import com.nokia.ats4.appmodel.adapter.event.TestResultEvent;
import com.nokia.ats4.appmodel.script.TestScript;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 * TestEngineAdapter is the abstract base class for test engines adapters.
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public abstract class TestEngineAdapter {

    /** Logging */
    private static final Logger log = Logger.getLogger(TestEngineAdapter.class);

    /** Listeners registered to adapter */
    private Set<TestEngineAdapterListener> listeners;

    /**
     * Creates a new instance of TestEngineAdapter
     */
    public TestEngineAdapter() {
        this.listeners = new HashSet<TestEngineAdapterListener>();
    }

    /**
     * Establish connection to the test engine.
     * @return true if initialized and connected successfully, otherwise false.
     */
    public abstract boolean connect();

    /**
     * Disconnect adapter from the test engine.
     */
    public abstract void disconnect();

    /**
     * Sets the adapter ready to execute tests dynamically.
     * NOTE! the target to be used when executing tests should be added before
     * calling this method.
     * @return true if dynamic test execution is ok to start
     */
    public abstract boolean startDynamicTestExecution();

    /**
     * Resets the adapter's dynamic mode.
     */
    public abstract void stopDynamicTestExecution();

    /**
     * <p>Execute a single keyword/parameter pair. The method will block while
     * the step is being executed in the remote test engine, and returns the
     * passed/failed status of the step as a boolean value.</p>
     * NOTE! you have to call startDynamicTestExecution method before you can use this
     * method.
     *
     * @param keyword Keyword to be executed
     * @param parameter Parameter of the keyword
     * @return true if step was executed and got passed status, otherwise false
     */
    public abstract boolean executeStep(String keyword, String parameter);

    /**
     * Execute the given TestScript in test engine. If server has a script in
     * execution currently, the script is added in the execution queue.
     * 
     * @param script TestScript to be executed (test case file)
     * @return Test set ID or <code>null</code> if failed.
     * @throws IllegalStateException If not connected to server yet
     */
    public abstract String executeTestScript(TestScript script);

    /**
     * Execute the given TestScript in test engine as many times as specified by
     * the repeats parameter. If server has a script in execution currently, the
     * script is added in the execution queue.
     * 
     * @param script TestScript to be executed (test case file)
     * @param repeats Number of test script repeats
     * @return Test set ID or <code>null</code> if failed.
     * @throws IllegalStateException If not connected to server yet
     */
    public abstract String executeTestScript(TestScript script, int repeats);

    /**
     * Stops execution of given test set
     * @param testSetItemID id of test set
     */
    public abstract void stopTestScriptExecution(String testSetItemID);

    /**
     * List the names of all available targets the tests can be run on.
     * 
     * @return List of target names
     */
    public abstract List<String> getTargetNames();

    /**
     * Add a target to be used in the tests. Should be one of the names returned
     * by <code>getTargetNames()</code>.
     * 
     * @param name Target name.
     * @return true if adding target was successful, false otherwise.
     * @throws IllegalStateException If disconnected from test engine
     */
    public abstract boolean addTarget(String name);

    /**
     * Add a targets to be used in the tests. Should contain subset of the names returned
     * by <code>getTargetNames()</code>.
     * 
     * @param targets a map of target id's in script and their corresponding target names.
     * <BR/>"Default" id means master target.
     * @return true if adding targets was successful, false otherwise.
     * @throws IllegalStateException If disconnected from test engine
     */
    public abstract boolean addSlaveTargets(Map<String, String> targets);

    /**
     * This clears target list of adapter
     */
    public abstract void clearTargets();

    /**
     * Register a listener for receiving <code>TestEngineEvents</code>. Events
     * are dispatched when test set or test case status is updated, i.e. when 
     * the test in execution is progressing.
     * 
     * @param listener TestEngineAdapterListener to register.
     */
    public void registerTestEngineAdapterListener(TestEngineAdapterListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Remove the specified <code>TestEngineAdapterListener</code> from the
     * adapter.
     * 
     * @param listener TestEngineAdapterListener to remove
     */
    public void removeTestEngineAdapterListener(TestEngineAdapterListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Notify all listeners with the specified TestResultEvent.
     * 
     * @param event TestResultEvent to dispatch to all listeners.
     */
    protected void statusUpdated(TestResultEvent event) {
        log.debug("Dispatching TestResultEvent; " + event.toString());
        for (TestEngineAdapterListener listener : this.listeners) {
            listener.processTestResultEvent(event);
        }
    }
}
