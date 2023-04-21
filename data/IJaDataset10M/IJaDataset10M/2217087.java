package com.tirsen.hanoi.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.DynaProperty;
import java.util.*;
import com.tirsen.hanoi.beans.MixedDynaClass;
import com.tirsen.hanoi.beans.MixedDynaBean;
import com.tirsen.hanoi.event.*;

/**
 *
 *
 * <!-- $Id: ProcessInstance.java,v 1.13 2002/09/09 14:52:13 tirsen Exp $ -->
 * <!-- $Author: tirsen $ -->
 *
 * @author Jon Tirs&eacute;n (tirsen@users.sourceforge.net)
 * @version $Revision: 1.13 $
 */
public class ProcessInstance {

    private static final Log logger = LogFactory.getLog(ProcessInstance.class);

    private ProcessDefinition definition;

    private Activity currentActivity;

    private Datasheet datasheet;

    private Queue outgoing = new DefaultQueue();

    private Queue incoming = new DefaultQueue();

    /**
     * Ready to run!
     */
    public static final int READY = 1;

    public static final String READY_STRING = "READY";

    /**
     * Currently running.
     */
    public static final int RUN = READY + 1;

    public static final String RUN_STRING = "RUN";

    /**
     * Paused while waiting to be started again.
     */
    public static final int PAUSE = RUN + 1;

    public static final String PAUSE_STRING = "PAUSE";

    /**
     * Waiting for a request to a connector.
     */
    public static final int WAIT = PAUSE + 1;

    public static final String WAIT_STRING = "WAIT";

    /**
     * Done execution of this workflow.
     */
    public static final int DONE = WAIT + 1;

    public static final String DONE_STRING = "DONE";

    private static final String[] STATE_NAMES = new String[] { READY_STRING, RUN_STRING, PAUSE_STRING, WAIT_STRING, DONE_STRING };

    private Vector processInstanceListeners = new Vector();

    protected ProcessInstance(ProcessDefinition definition, Datasheet datasheet) {
        this.definition = definition;
        this.datasheet = datasheet;
        if (datasheet.getState() == 0) switchState(READY);
        if (datasheet.getCurrentStepID() != null) {
            currentActivity = definition.getActivity(datasheet.getCurrentStepID());
        }
        if (currentActivity == null && isNotState(DONE)) {
            setCurrentActivity(definition.getFirstActivity());
        }
        for (Iterator iterator = definition.getProcessorsList().iterator(); iterator.hasNext(); ) {
            Processor processor = (Processor) iterator.next();
            processor.init(this);
        }
        Object[] resources = definition.getResources();
        for (int i = 0; i < resources.length; i++) {
            Object resource = resources[i];
            if (resource instanceof ResourceBindingListener) {
                ResourceBindingListener resourceBindingListener = (ResourceBindingListener) resource;
                resourceBindingListener.bindToInstance(this);
            }
        }
    }

    private void setCurrentActivity(Activity activity) {
        currentActivity = activity;
        if (isNotState(DONE)) datasheet.setCurrentStepID(currentActivity.getId()); else datasheet.setCurrentStepID(null);
    }

    /**
     * Used by the engine to set the queue from the persistence.
     */
    void setOutgoingQueue(Queue outgoing) {
        this.outgoing = outgoing;
    }

    /**
     * Used by the engine to set the queue from the persistence.
     */
    void setIncomingQueue(Queue incoming) {
        this.incoming = incoming;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    private void gotoNextActivity() {
        Transition transition = null;
        List transitions = definition.getTransitionList();
        Activity from = getCurrentActivity();
        Activity resultingActivity = null;
        for (Iterator iterator = transitions.iterator(); iterator.hasNext() && resultingActivity == null; ) {
            transition = (Transition) iterator.next();
            if (transition.getFrom() == from) {
                if (transition.evaluate(datasheet)) resultingActivity = transition.getTo();
            }
        }
        if (resultingActivity == null) switchState(DONE);
        setCurrentActivity(resultingActivity);
    }

    public String getId() {
        return getDatasheet().getInstanceID();
    }

    public Queue getOutgoingQueue() {
        return outgoing;
    }

    public Queue getIncomingQueue() {
        return incoming;
    }

    public Datasheet getDatasheet() {
        return datasheet;
    }

    private static class Runner implements Processor.Next {

        private Iterator processors;

        private Activity activity;

        public Runner(Iterator processors, Activity activity) {
            this.processors = processors;
            this.activity = activity;
        }

        public int runNext() {
            if (processors.hasNext()) {
                Processor current = (Processor) processors.next();
                return current.run(this, activity);
            } else {
                return activity.run();
            }
        }
    }

    /**
     * Execute all tasks until we are done or until we are waiting for an connector.
     * When you suspect a connector has processed it's requests run this again and
     * the currently running step will check if it has what it needs to go on.
     */
    public void execute() {
        if (isNotState(DONE)) {
            switchState(RUN);
            while (isState(RUN)) {
                executeActivity();
            }
            logger.debug("finished execution with state " + getStateString());
        }
    }

    public void executeOne() {
        if (isNotState(DONE)) {
            switchState(RUN);
            executeActivity();
            if (isState(RUN)) switchState(PAUSE);
        }
    }

    private void executeActivity() {
        logger.info("executing " + currentActivity);
        java.util.Iterator listenerIterator = processInstanceListeners.iterator();
        while (listenerIterator.hasNext()) {
            ProcessInstanceListener listener = (ProcessInstanceListener) listenerIterator.next();
            listener.instanceChanged(new ProcessInstanceEvent(currentActivity));
        }
        Runner runner = new Runner(definition.getProcessorsList().iterator(), currentActivity);
        int result = runner.runNext();
        if (result == Step.WAIT) {
            switchState(WAIT);
        } else {
            gotoNextActivity();
        }
        logger.info("execute result " + result);
    }

    public static void logDatasheet(Datasheet datasheet) {
        MixedDynaBean bean = MixedDynaBean.asDynaBean(datasheet);
        MixedDynaClass mixedDynaClass = bean.getMixedDynaClass();
        DynaProperty[] properties = mixedDynaClass.getDynaProperties();
        for (int i = 0; i < properties.length; i++) {
            String property = properties[i].getName();
            if (mixedDynaClass.isReadable(property)) {
                logger.debug(property + "\t=\t" + bean.get(property));
            } else {
                logger.debug(property + "\t=\t<not readable>");
            }
        }
    }

    private void switchState(int state) {
        datasheet.setState(state);
        logger.debug("switched state to " + getStateString());
    }

    private boolean isState(int state) {
        return getState() == state;
    }

    private boolean isNotState(int state) {
        return getState() != state;
    }

    public int getState() {
        return datasheet.getState();
    }

    public String getStateString() {
        return getStateString(Locale.getDefault());
    }

    public String getStateString(Locale locale) {
        int state = getState();
        return getStateString(locale, state);
    }

    public static String getStateString(Locale locale, int state) {
        if (state - 1 >= STATE_NAMES.length) {
            return "Unknown<" + state + ">";
        }
        return STATE_NAMES[state - 1];
    }

    public ProcessDefinition getDefinition() {
        return definition;
    }

    public void addProcessInstanceListener(com.tirsen.hanoi.event.ProcessInstanceListener listener) {
        processInstanceListeners.add(listener);
    }

    public String toString() {
        return "ProcessInstance[" + getId() + "]";
    }
}
