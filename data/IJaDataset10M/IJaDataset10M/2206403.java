package org.dbe.composer.wfengine.bpel.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.SdlException;
import org.dbe.composer.wfengine.bpel.ISdlBusinessProcess;
import org.dbe.composer.wfengine.bpel.ISdlFault;
import org.dbe.composer.wfengine.bpel.SdlBusinessProcessException;
import org.dbe.composer.wfengine.bpel.message.ISdlMessageData;
import org.dbe.composer.wfengine.util.SdlUtil;

/**
 * Used in conjunction with the process to ensure that we're only executing a
 * single object at a time. Simply add objects to the queue to have them get
 * executed.
 */
public class SdlExecutionQueue {

    private static final Logger logger = Logger.getLogger(SdlExecutionQueue.class.getName());

    /** Holds the objects that we want to execute. */
    private final LinkedList mExecutionQueue;

    /** flag for whether execution is suspended */
    private boolean mSuspended = false;

    /** Number of pending calls to <code>execute</code>. */
    private int mExecuteDepth = 0;

    /** Number of pending calls to <code>executeObject</code>. */
    private int mExecuteObjectDepth = 0;

    /** The owner process. */
    private ISdlBusinessProcess mProcess;

    /** Location paths to be resumed. */
    private final Set mPendingResumePaths = Collections.synchronizedSet(new HashSet());

    /**
     * Constructs empty queue.
     *
     * @param aProcess
     */
    public SdlExecutionQueue(ISdlBusinessProcess aProcess) {
        this(aProcess, false, Collections.EMPTY_LIST);
    }

    /**
     * Construct execution queue from a specific list of objects.
     *
     * @param aProcess the owner process
     * @param aSuspended flag for whether execution is suspended
     * @param aQueue initial queue of objects
     */
    public SdlExecutionQueue(ISdlBusinessProcess aProcess, boolean aSuspended, List aQueue) {
        logger.debug("SdlExecutionQueue() loc=" + aProcess.getLocationPath() + ", process=" + aProcess.getName());
        mProcess = aProcess;
        mSuspended = aSuspended;
        mExecutionQueue = new LinkedList(aQueue);
    }

    /**
     * Adds the object to the execution queue. If the queue is already executing
     * then the object will be executed when its turn comes. If the queue
     * isn't being executed, then adding an object for execution will immediately
     * start the execution process.
     * @param aObject
     */
    protected void add(ISdlExecutableQueueItem aObject) throws SdlBusinessProcessException {
        logger.debug("add() " + aObject.getLocationPath());
        if (isSuspended() && mPendingResumePaths.contains(aObject.getLocationPath())) {
            mPendingResumePaths.remove(aObject.getLocationPath());
            executeObject(aObject);
        } else {
            mExecutionQueue.add(aObject);
            if (!isExecuting()) {
                execute();
            }
        }
    }

    /**
     * Returns true if the queue is currently being executed. Called from the add
     * method to see if the queue is being executed. If not, then the add process
     * will start the queue executing.
     */
    private boolean isExecuting() {
        return mExecuteDepth > 0;
    }

    /**
     * Pops an object off the queue and executes it. Once called, this method will
     * run until there are no more objects to execute.
     */
    protected void execute() throws SdlBusinessProcessException {
        logger.debug("execute()");
        ++mExecuteDepth;
        try {
            while (!isSuspended() && hasObjectsToExecute()) {
                executeObject(getNextObjectToExecute());
            }
        } finally {
            --mExecuteDepth;
        }
    }

    /**
     * Returns true if there are objects in the queue to execute
     */
    private boolean hasObjectsToExecute() {
        return !mExecutionQueue.isEmpty();
    }

    /**
     * Gets the next object from the queue to execute.
     */
    private ISdlExecutableQueueItem getNextObjectToExecute() {
        return (ISdlExecutableQueueItem) mExecutionQueue.removeFirst();
    }

    /**
     * Calls the object's execute method.
     * @param aExecutable
     */
    private synchronized void executeObject(ISdlExecutableQueueItem aExecutable) throws SdlBusinessProcessException {
        logger.debug("executeObject()");
        ++mExecuteObjectDepth;
        try {
            if (aExecutable.getState() == SdlBpelState.READY_TO_EXECUTE) {
                aExecutable.setState(SdlBpelState.EXECUTING);
                try {
                    aExecutable.execute();
                } catch (Throwable ex) {
                    logger.error("Throwable: Exception during execution of bpel object: " + ex);
                    SdlException.logError(ex, "Exception during execution of bpel object.");
                    ISdlFault fault = null;
                    if (ex instanceof SdlBpelException) {
                        fault = ((SdlBpelException) ex).getFault();
                    }
                    if (fault == null) {
                        fault = SdlFaultFactory.getFault(SdlFaultFactory.SYSTEM_ERROR);
                    }
                    if (fault != null) {
                        logger.error("fault=" + fault);
                        fault.setInfo(SdlUtil.isNullOrEmpty(ex.getLocalizedMessage()) ? " no additional info " : ex.getLocalizedMessage());
                    }
                    aExecutable.objectCompletedWithFault(fault);
                }
            }
        } finally {
            --mExecuteObjectDepth;
        }
    }

    /**
     * Returns <code>true</code> if and only if this execution queue is quiescent.
     */
    private boolean isQuiescent() {
        return (mExecuteObjectDepth == 0) && (mExecutionQueue.isEmpty() || isSuspended());
    }

    /**
     * Returns <code>true</code> if and only if this execution queue is suspended.
     */
    private boolean isSuspended() {
        return mSuspended;
    }

    /**
     * Returns the location paths of the objects in this queue in the same order as the queue.
     */
    public List getLocationPaths() {
        List locationPaths = new LinkedList();
        for (Iterator i = mExecutionQueue.iterator(); i.hasNext(); ) {
            ISdlExecutableBpelObject object = (ISdlExecutableBpelObject) i.next();
            String locationPath = object.getLocationPath();
            locationPaths.add(locationPath);
        }
        return locationPaths;
    }

    /**
     * Resumes execution of objects in the queue.
     *
     * @param aExecute <code>true</code> to execute immediately or
     * <code>false</code> to put the queue in a running state without executing
     * immediately.
     */
    public synchronized void resume(boolean aExecute) throws SdlBusinessProcessException {
        logger.debug("resume() execute=" + aExecute);
        mSuspended = false;
        mPendingResumePaths.clear();
        if (aExecute) {
            execute();
        }
    }

    /**
     * Checks the suspended execution queue and resumes the specified object
     * if it is in the queue.
     * Note this is synchronized in case there is a callback to resume an object
     * just being scheduled by the execution of a parent (executeObject call),
     * in which case we need to ensure the object is in our queue.
     */
    public synchronized void resume(String aLocationPath) throws SdlBusinessProcessException {
        logger.debug("resume() location=" + aLocationPath);
        if (aLocationPath != null) {
            boolean found = false;
            ISdlExecutableBpelObject object = null;
            for (Iterator i = mExecutionQueue.iterator(); i.hasNext() && !found; ) {
                object = (ISdlExecutableBpelObject) i.next();
                found = aLocationPath.equals(object.getLocationPath());
            }
            if (found) {
                mExecutionQueue.remove(object);
                executeObject(object);
            } else {
                mPendingResumePaths.add(aLocationPath);
            }
        }
    }

    /**
     * Marks this execution queue as suspended.
     */
    public void suspend() {
        mSuspended = true;
    }

    /**
     * Dispatches an alarm to its receiver.
     *
     * @param aReceiver
     * @throws SdlBusinessProcessException
     */
    public void dispatchAlarm(final ISdlAlarmReceiver aReceiver) throws SdlBusinessProcessException {
        logger.debug("dispatchAlarm()");
        executeObject(new SdlExecutableObjectStub() {

            public void execute() throws SdlBusinessProcessException {
                aReceiver.onAlarm();
            }

            protected ISdlBpelObject getFaultObject() {
                return (ISdlBpelObject) aReceiver;
            }
        });
    }

    /**
     * Dispatches message data to a queued invoke.
     *
     * @param aReceiver
     * @param aData
     * @throws SdlBusinessProcessException
     */
    public void dispatchInvokeData(final ISdlMessageReceiver aReceiver, final ISdlMessageData aData) throws SdlBusinessProcessException {
        logger.debug("dispatchInvokeData()");
        executeObject(new SdlExecutableObjectStub() {

            public void execute() throws SdlBusinessProcessException {
                aReceiver.onMessage(aData);
            }

            protected ISdlBpelObject getFaultObject() {
                return (ISdlBpelObject) aReceiver;
            }
        });
    }

    /**
     * Dispatches a fault to a queued invoke.
     *
     * @param aReceiver
     * @param aFault
     * @throws SdlBusinessProcessException
     */
    public void dispatchInvokeFault(final ISdlMessageReceiver aReceiver, final ISdlFault aFault) throws SdlBusinessProcessException {
        logger.debug("dispatcInvokeFault()");
        executeObject(new SdlExecutableObjectStub() {

            public void execute() throws SdlBusinessProcessException {
                aReceiver.onFault(aFault);
            }

            protected ISdlBpelObject getFaultObject() {
                return (ISdlBpelObject) aReceiver;
            }
        });
    }

    /**
     * Dispatches message data to a message receiver.
     *
     * @param aReceiver
     * @param aData
     * @throws SdlBusinessProcessException
     */
    public void dispatchReceiveData(final ISdlMessageReceiver aReceiver, final ISdlMessageData aData) throws SdlBusinessProcessException {
        logger.debug("dispatchReceiveData()");
        executeObject(new SdlExecutableObjectStub() {

            public void execute() throws SdlBusinessProcessException {
                aReceiver.onMessage(aData);
            }

            protected ISdlBpelObject getFaultObject() {
                return (ISdlBpelObject) aReceiver;
            }
        });
    }

    /**
     * Returns owner process.
     */
    protected ISdlBusinessProcess getProcess() {
        return mProcess;
    }

    /**
     * Restores the queue's state.
     *
     * @param aSuspended the suspended state to set.
     * @param aQueue the list of executable objects to set for the queue.
     * @throws SdlBusinessProcessException
     */
    public void setQueueData(boolean aSuspended, List aQueue) throws SdlBusinessProcessException {
        if (!isQuiescent()) {
            logger.error("Cannot set execution queue data when not quiescent.");
            throw new SdlBusinessProcessException("Cannot set execution queue data when not quiescent.");
        }
        mSuspended = aSuspended;
        mExecutionQueue.clear();
        mExecutionQueue.addAll(aQueue);
    }

    /**
     * Base class for stub objects that can be executed by <code>executeObject</code>.
     */
    private abstract class SdlExecutableObjectStub implements ISdlExecutableQueueItem {

        protected abstract ISdlBpelObject getFaultObject();

        public String getLocationPath() {
            logger.error("No location path available");
            throw new UnsupportedOperationException("No location path available");
        }

        public SdlBpelState getState() {
            return SdlBpelState.READY_TO_EXECUTE;
        }

        public void objectCompletedWithFault(ISdlFault fault) throws SdlBusinessProcessException {
            getProcess().objectCompletedWithFault(getFaultObject(), fault);
        }

        public void setState(SdlBpelState aState) {
        }
    }
}
