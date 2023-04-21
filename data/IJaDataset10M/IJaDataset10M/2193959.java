package org.dbe.composer.wfengine.bpel.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.SdlException;
import org.dbe.composer.wfengine.bpel.ISdlBusinessProcess;
import org.dbe.composer.wfengine.bpel.ISdlEngineEvent;
import org.dbe.composer.wfengine.bpel.ISdlEngineListener;
import org.dbe.composer.wfengine.bpel.ISdlFault;
import org.dbe.composer.wfengine.bpel.ISdlPlanManager;
import org.dbe.composer.wfengine.bpel.ISdlProcessEvent;
import org.dbe.composer.wfengine.bpel.ISdlProcessInfoEvent;
import org.dbe.composer.wfengine.bpel.ISdlProcessListener;
import org.dbe.composer.wfengine.bpel.SdlBusinessProcessException;
import org.dbe.composer.wfengine.bpel.WSDLDefHelper;
import org.dbe.composer.wfengine.bpel.config.ISdlEngineConfiguration;
import org.dbe.composer.wfengine.bpel.impl.list.ProcessInstanceDetail;
import org.dbe.composer.wfengine.bpel.impl.queue.SdlInboundReceive;
import org.dbe.composer.wfengine.bpel.impl.queue.SdlInvoke;
import org.dbe.composer.wfengine.bpel.impl.queue.SdlMessageReceiver;
import org.dbe.composer.wfengine.bpel.impl.queue.SdlReply;
import org.dbe.composer.wfengine.bpel.message.ISdlMessageData;
import org.dbe.composer.wfengine.bpel.xpath.SdlXPathHelper;
import org.dbe.composer.wfengine.util.SdlSafelyViewableCollection;
import org.dbe.composer.wfengine.wsdl.def.IWsdlPropertyAlias;
import org.dbe.composer.wfengine.wsio.receive.ISdlMessageContext;
import org.w3c.dom.Document;

/** The class implementing the business process execution engine. */
public class SdlBusinessProcessEngine implements ISdlBusinessProcessEngineInternal {

    private static final Logger logger = Logger.getLogger(SdlBusinessProcessEngine.class.getName());

    /** Engine listeners */
    protected Collection mEngineListeners = new SdlSafelyViewableCollection(new LinkedHashSet());

    /** Global process listeners */
    protected Collection mGlobalProcessListeners = new SdlSafelyViewableCollection(new LinkedHashSet());

    /** Map of listeners, keyed by the process ID */
    protected HashMap mProcessListeners = new HashMap();

    /** The queue for incoming data that gets dispatched to processes */
    protected ISdlQueueManager mQueueManager;

    /** The alarm manager for handling waits and alarm. */
    protected ISdlAlarmManager mAlarmManager;

    /** The lock manager for handling acquiring and releasing locks. */
    protected ISdlLockManager mLockManager;

    /** Maps process QName to its process plan */
    protected ISdlPlanManager mPlanManager;

    /** The date the engine started */
    protected Date mStarted;

    /** Process manager */
    private ISdlProcessManager mProcessManager;

    /** The type mapping to use when converting from schema to java and back. */
    private SdlTypeMapping mTypeMapping = new SdlTypeMapping();

    /** The engine configuration supplied during engine construction. */
    protected ISdlEngineConfiguration mEngineConfiguration;

    /**
     * Constructs a new engine with the passed configuration, queue manager,
     * process manager, and alarm manager.
     *
     * @param aEngineConfiguration The engine configuration to use for this engine.
     * @param aQueueManager The queue manager to be associated with this engine.
     * @param aProcessManager The process manager to be associated with this engine.
     * @param aAlarmManager The alarm manager to be associated with this engine.
     * @param aLockManager The lock manager to be associated with this engine.
     */
    public SdlBusinessProcessEngine(ISdlEngineConfiguration aEngineConfiguration, ISdlQueueManager aQueueManager, ISdlProcessManager aProcessManager, ISdlAlarmManager aAlarmManager, ISdlLockManager aLockManager) {
        logger.debug("SdlBusinessProcessEngine()");
        mEngineConfiguration = aEngineConfiguration;
        mQueueManager = aQueueManager;
        mProcessManager = aProcessManager;
        mAlarmManager = aAlarmManager;
        mLockManager = aLockManager;
        if (mQueueManager != null) {
            mQueueManager.setEngine(this);
        }
        if (mProcessManager != null) {
            mProcessManager.setEngine(this);
        }
        if (mAlarmManager != null) {
            mAlarmManager.setEngine(this);
        }
        if (mLockManager != null) {
            mLockManager.setEngine(this);
        }
        mStarted = new Date();
    }

    /**
     * Returns the business process with the specified process id, locking the
     * process into memory. <em>Each call to <code>getProcessById</code> must be
     * followed eventually by a matching call to
     * <code>releaseProcess</code></em>.
     *
     * @param aId
     */
    protected ISdlBusinessProcess getSdlProcessById(long aId) throws SdlBusinessProcessException {
        ISdlBusinessProcess process = (ISdlBusinessProcess) getProcessManager().getProcess(aId);
        if (process == null) {
            logger.error("Error: Unknown process:" + aId);
            throw new SdlBusinessProcessException("Unknown process: " + aId);
        }
        return process;
    }

    /**
     * Creates the process with the data received.
     *
     * @param aInboundReceive The inbound receive object.
     * @param aReply The message receiver reply object.
     */
    protected void createProcessWithServiceMessage(SdlInboundReceive aInboundReceive, ISdlMessageReceiver aReply) throws SdlBusinessProcessException {
        logger.debug("createProcessWithMessage()");
        ISdlBusinessProcess process = (ISdlBusinessProcess) getProcessManager().createBusinessProcess((ISdlProcessPlan) aInboundReceive.getProcessPlan());
        long processId = process.getProcessId();
        try {
            fireEngineEvent(new SdlEngineEvent(processId, ISdlEngineEvent.PROCESS_CREATED, aInboundReceive.getProcessName()));
            if (aReply != null) {
                getQueueManager().addReply(new SdlReply(process.getProcessId(), aReply, aInboundReceive));
            }
            process.setCreateMessage(aInboundReceive);
            initPartnerLinks(process, aInboundReceive.getProcessPlan());
            updatePartnerLink(aInboundReceive, process);
            process.setContextSDLProvider((ISdlProcessPlan) aInboundReceive.getProcessPlan());
            process.setContextWSDLProvider((ISdlProcessPlan) aInboundReceive.getProcessPlan());
            executeProcess(processId, aReply != null);
        } finally {
            releaseProcess(process);
        }
    }

    /**
     * Updates the partner link object with the data from the inbound receive.
     * @param aInboundReceive
     * @param aProcess
     */
    protected void updatePartnerLink(SdlInboundReceive aInboundReceive, ISdlBusinessProcess aProcess) throws SdlBusinessProcessException {
    }

    /**
     * Initialize the partner links for the business process
     * @param aProcess the process to initialize partner links for
     * @param aDesc the plan describing the partner link initialization
     * @throws SdlBusinessProcessException
     */
    protected void initPartnerLinks(ISdlBusinessProcess aProcess, ISdlProcessPlan aDesc) throws SdlBusinessProcessException {
    }

    public ISdlEngineConfiguration getEngineConfiguration() {
        return mEngineConfiguration;
    }

    public ISdlBusinessProcess createProcess(long aPid, ISdlProcessPlan aPlan) {
        SdlBusinessProcess process = new SdlBusinessProcess(aPid, this, aPlan);
        return process;
    }

    public ISdlProcessManager getProcessManager() {
        return mProcessManager;
    }

    /**
     * Executes a business process
     * @param aPid - the pid for the process to execute
     * @param aExecuteImmediatelyFlag - a hint regarding whether a reply is waiting for the process to execute or not.
     */
    protected void executeProcess(long aPid, boolean aExecuteImmediatelyFlag) throws SdlBusinessProcessException {
        logger.debug("executeProcess() pid=" + aPid + ", immediately=" + aExecuteImmediatelyFlag);
        ISdlBusinessProcess process = getProcessById(aPid);
        try {
            if (process == null) {
                logger.error("Error: Unknown process: " + aPid);
                throw new SdlBusinessProcessException("Unknown process: " + aPid);
            }
            process.queueObjectToExecute(process);
        } finally {
            releaseProcess(process);
        }
    }

    /**
     * Returns the business process with the specified process id, locking the
     * process into memory. <em>Each call to <code>getProcessById</code> must be
     * followed eventually by a matching call to
     * <code>releaseProcess</code></em>.
     *
     * @param aId
     */
    protected ISdlBusinessProcess getProcessById(long aId) throws SdlBusinessProcessException {
        ISdlBusinessProcess process = (ISdlBusinessProcess) getProcessManager().getProcess(aId);
        if (process == null) {
            logger.error("Error: Unknown process: " + aId);
            throw new SdlBusinessProcessException("Unknown process: " + aId);
        }
        return process;
    }

    /**
     * Releases a process locked by <code>getProcessById</code>.
     */
    protected void releaseProcess(ISdlBusinessProcess aProcess) {
        getProcessManager().releaseProcess(aProcess);
    }

    public void suspendProcess(long aPid) throws SdlBusinessProcessException {
        ISdlBusinessProcess process = getProcessById(aPid);
        try {
            if (process.getProcessState() == ISdlBusinessProcess.PROCESS_RUNNING) {
                process.suspend();
            }
        } finally {
            releaseProcess(process);
        }
    }

    public void resumeProcess(long aPid) throws SdlBusinessProcessException {
        ISdlBusinessProcess process = getProcessById(aPid);
        try {
            if (process.getProcessState() == ISdlBusinessProcess.PROCESS_SUSPENDED) {
                process.resume(true);
            }
        } finally {
            releaseProcess(process);
        }
    }

    public void terminateProcess(long aPid) throws SdlBusinessProcessException {
        ISdlBusinessProcess process = getProcessById(aPid);
        try {
            if (process.getProcessState() != ISdlBusinessProcess.PROCESS_COMPLETE && process.getProcessState() != ISdlBusinessProcess.PROCESS_FAULTED) {
                process.terminate();
            }
        } finally {
            releaseProcess(process);
        }
    }

    public void resumeProcessObject(long aPid, String aLocation) throws SdlBusinessProcessException {
        ISdlBusinessProcess process = getProcessById(aPid);
        try {
            if (process.getProcessState() == ISdlBusinessProcess.PROCESS_SUSPENDED) {
                process.resume(aLocation);
            }
        } finally {
            releaseProcess(process);
        }
    }

    public void queueReceiveSdlData(ISdlProcessPlan aProcessPlan, ISdlMessageData aMessageData, ISdlMessageReceiver aReply, ISdlMessageContext aContext) throws SdlBusinessProcessException {
        logger.info("queueReceiveSdlData()");
        QName portType = aProcessPlan.getMyRolePortType(aContext.getPartnerLinkName());
        boolean canCreateInstance = aProcessPlan.isCreateInstance(aContext.getPartnerLinkName(), portType, aContext.getOperation());
        Map correlationMap = createSdlCorrelationMap(aProcessPlan, aMessageData, aContext, portType);
        SdlInboundReceive inboundReceive = new SdlInboundReceive(correlationMap, aProcessPlan, aMessageData, aContext);
        SdlMessageReceiver found = getQueueManager().matchInboundReceive(inboundReceive, aReply, !canCreateInstance);
        if (found == null && canCreateInstance) {
            createProcessWithServiceMessage(inboundReceive, aReply);
        } else if (found != null) {
            handleMatchedReceive(inboundReceive, found, aReply != null);
        }
    }

    public void queueReceiveWsdlData(ISdlProcessPlan aProcessPlan, ISdlMessageData aMessageData, ISdlMessageReceiver aReply, ISdlMessageContext aContext) throws SdlBusinessProcessException {
        logger.debug("queueReceiveWsdlData()");
        QName portType = aProcessPlan.getMyRolePortType(aContext.getPartnerLinkName());
        boolean canCreateInstance = aProcessPlan.isCreateInstance(aContext.getPartnerLinkName(), portType, aContext.getOperation());
        Map correlationMap = createWsdlCorrelationMap(aProcessPlan, aMessageData, aContext, portType);
        SdlInboundReceive inboundReceive = new SdlInboundReceive(correlationMap, aProcessPlan, aMessageData, aContext);
        SdlMessageReceiver found = getQueueManager().matchInboundReceive(inboundReceive, aReply, !canCreateInstance);
        if (found == null && canCreateInstance) {
            createProcessWithServiceMessage(inboundReceive, aReply);
        } else if (found != null) {
            handleMatchedReceive(inboundReceive, found, aReply != null);
        }
    }

    public void handleMatchedReceive(SdlInboundReceive aInboundReceive, SdlMessageReceiver aFound, boolean aReplyWaitingFlag) throws SdlBusinessProcessException {
        dispatchReceiveData(aFound, aInboundReceive, aReplyWaitingFlag);
    }

    /**
     * Dispatches the message data to the process's message receiver.
     * @param aMessageReceiver
     * @param aInboundReceive
     * @param aReplyWaitingFlag Serves as a hint to how the dispatch might be handled.
     */
    protected void dispatchReceiveData(SdlMessageReceiver aMessageReceiver, SdlInboundReceive aInboundReceive, boolean aReplyWaitingFlag) throws SdlBusinessProcessException {
        long processId = aMessageReceiver.getProcessId();
        int locationPathId = aMessageReceiver.getMessageReceiverPathId();
        getProcessManager().saveReceivedMessage(processId, locationPathId, aInboundReceive.getMessageData());
        ISdlBusinessProcess process = getProcessById(processId);
        try {
            updatePartnerLink(aInboundReceive, process);
            process.dispatchReceiveData(locationPathId, aInboundReceive.getMessageData());
        } finally {
            releaseProcess(process);
        }
    }

    /**
     * Creates the correlation map for the incoming receive.
     * @param aDesc
     * @param aData
     * @param aContext
     * @param aPortType
     */
    private Map createSdlCorrelationMap(ISdlProcessPlan aDesc, ISdlMessageData aData, ISdlMessageContext aContext, QName aPortType) throws SdlBusinessProcessException {
        Map map = new HashMap();
        return map;
    }

    /**
     * Creates the correlation map for the incoming receive.
     * @param aDesc
     * @param aData
     * @param aContext
     * @param aPortType
     */
    private Map createWsdlCorrelationMap(ISdlProcessPlan aDesc, ISdlMessageData aData, ISdlMessageContext aContext, QName aPortType) throws SdlBusinessProcessException {
        Map map = new HashMap();
        for (Iterator iter = aDesc.getCorrelatedPropertyNames(aContext.getPartnerLinkName(), aPortType, aContext.getOperation()).iterator(); iter.hasNext(); ) {
            QName propName = (QName) iter.next();
            IWsdlPropertyAlias propAlias = aDesc.getSdlProcessDef().getPropertyAlias(aData.getMessageType(), propName);
            QName propType = WSDLDefHelper.getProperty(aDesc, propName).getTypeName();
            map.put(propName, SdlXPathHelper.evaluatePropertyToSimpleType(propAlias, aData, getTypeMapping(), propType));
        }
        return map;
    }

    /**
     * Updates the partner link object with the data from the inbound receive.
     * @param aInboundReceive
     * @param aProcess
     */
    protected void updatePartnerLink(SdlInboundReceive aInboundReceive, SdlBusinessProcess aProcess) throws SdlBusinessProcessException {
    }

    public void queueInvokeData(long aProcessId, String aLocationPath, ISdlMessageData aMessageData) throws SdlBusinessProcessException {
        logger.debug("queueInvokeData() pid=" + aProcessId + ", location=" + aLocationPath);
        SdlInvoke invokeQueueObject = new SdlInvoke(aProcessId, aLocationPath, aMessageData);
        SdlInvoke found = getQueueManager().removeInvoke(invokeQueueObject);
        if (found != null && found.getMessageReceiverPath() != null) {
            ISdlBusinessProcess process = getProcessById(aProcessId);
            try {
                process.dispatchInvokeData(aLocationPath, aMessageData);
            } finally {
                releaseProcess(process);
            }
        } else {
            unmatchedInvoke(invokeQueueObject);
        }
    }

    /**
     * Called when we got some invoke data that we weren't expecting. This should
     * never happen.
     * @param aInvokeQueueObject
     * @throws SdlBusinessProcessException
     */
    protected void unmatchedInvoke(SdlInvoke aInvokeQueueObject) throws SdlBusinessProcessException {
        logger.error("unexpected invoke");
        throw new SdlBusinessProcessException("unexpected invoke");
    }

    public void queueInvokeFault(long aProcessId, String aLocationPath, ISdlFault aFault) throws SdlBusinessProcessException {
        SdlInvoke invokeQueueObject = new SdlInvoke(aProcessId, aLocationPath, aFault);
        SdlInvoke found = getQueueManager().removeInvoke(invokeQueueObject);
        if (found != null && found.getMessageReceiverPath() != null) {
            ISdlBusinessProcess process = getProcessById(aProcessId);
            try {
                process.dispatchInvokeFault(aLocationPath, aFault);
            } finally {
                releaseProcess(process);
            }
        } else {
            unmatchedInvoke(invokeQueueObject);
        }
    }

    /**
     * TODO change to accept the var path instead of the name plus activity context
     */
    public Object getVariableData(long aPid, String aActivityLoc, String aVarName, String aPart) {
        try {
            ISdlBusinessProcess process = getProcessById(aPid);
            try {
                return process.getVariableData(aActivityLoc, aVarName, aPart);
            } finally {
                releaseProcess(process);
            }
        } catch (SdlBusinessProcessException e) {
            logger.error("Error getting process " + aPid);
            SdlException.logError(e, "Error getting process " + aPid);
            return null;
        }
    }

    public ISdlQueueManager getQueueManager() {
        return mQueueManager;
    }

    public ISdlAlarmManager getAlarmManager() {
        return mAlarmManager;
    }

    public ISdlLockManager getLockManager() {
        return mLockManager;
    }

    public void setPlanManager(ISdlPlanManager aPlanManager) {
        mPlanManager = aPlanManager;
    }

    /**
     * Getter for the plan manager.
     */
    protected ISdlPlanManager getPlanManager() {
        return mPlanManager;
    }

    /**
     * Sets the configruation to use for this engine, note this is usually set during construction.
     * @param aConfiguration The configuration to use for this engine.
     */
    public void setEngineConfiguration(ISdlEngineConfiguration aConfiguration) {
        mEngineConfiguration = aConfiguration;
    }

    /**
     * Removes the process from the process manager.
     */
    public void processEnded(long aProcessId) throws SdlBusinessProcessException {
        SdlBusinessProcess process = (SdlBusinessProcess) getProcessById(aProcessId);
        try {
            if (process.getState() == SdlBpelState.FAULTED) {
                ISdlFault fault = process.getFault();
                if (process.getOutstandingReplyCount() > 0) {
                    getQueueManager().faultOutstandingReplies(aProcessId, fault);
                }
            }
            getProcessManager().processEnded(aProcessId);
        } finally {
            releaseProcess(process);
        }
    }

    public Date getStartDate() {
        return mStarted;
    }

    public Document getProcessState(long aProcessId) throws SdlBusinessProcessException {
        ISdlBusinessProcess process = getProcessById(aProcessId);
        try {
            return process.serializeState(false);
        } finally {
            releaseProcess(process);
        }
    }

    public Document getProcessVariable(long aProcessId, String aLocationPath) throws SdlBusinessProcessException {
        ISdlBusinessProcess process = getProcessById(aProcessId);
        try {
            return process.serializeVariable(aLocationPath);
        } finally {
            releaseProcess(process);
        }
    }

    /**
     * Dispatches alarm to a process by the location path of the alarm receiver.
     *
     * @param aProcessId The process id of the owner process.
     * @param aLocationPathId The location path id of the alarm receiver.
     */
    public void dispatchAlarm(long aProcessId, int aLocationPathId) throws SdlBusinessProcessException {
        if (transferAlarmToProcessManager(aProcessId, aLocationPathId)) {
            SdlBusinessProcess process = (SdlBusinessProcess) getProcessById(aProcessId);
            try {
                process.dispatchAlarm(aLocationPathId);
            } finally {
                releaseProcess(process);
            }
        }
    }

    /**
     * Transfers alarm from alarm manager to process manager. Returns
     * <code>true</code> if and only if the alarm was successfully removed from
     * the alarm manager. If the remove fails, then the alarm was already
     * cancelled by some other activity.
     *
     * @param aProcessId
     * @param aLocationPathId
     * @throws SdlBusinessProcessException
     */
    protected boolean transferAlarmToProcessManager(long aProcessId, int aLocationPathId) throws SdlBusinessProcessException {
        boolean removed = getAlarmManager().removeAlarm(aProcessId, aLocationPathId);
        if (removed) {
            getProcessManager().saveReceivedAlarm(aProcessId, aLocationPathId);
        }
        return removed;
    }

    /**
     * Returns the process instance details for the process with the specified process id.
     *
     * @param aProcessId
     * @return ProcessInstanceDetail
     */
    public ProcessInstanceDetail getProcessInstanceDetails(long aProcessId) {
        return getProcessManager().getProcessInstanceDetails(aProcessId);
    }

    public void addEngineListener(ISdlEngineListener aListener) {
        mEngineListeners.add(aListener);
    }

    public void removeEngineListener(ISdlEngineListener aListener) {
        mEngineListeners.remove(aListener);
    }

    public void fireEngineEvent(ISdlEngineEvent aEvent) {
        logger.info("fireEngineEvent() pid=" + aEvent.getPID() + ", event=" + aEvent);
        for (Iterator iter = mEngineListeners.iterator(); iter.hasNext(); ) {
            ((ISdlEngineListener) iter.next()).handleEngineEvent(aEvent);
        }
    }

    public void addProcessListener(ISdlProcessListener aListener) {
        mGlobalProcessListeners.add(aListener);
    }

    public void removeProcessListener(ISdlProcessListener aListener) {
        mGlobalProcessListeners.remove(aListener);
    }

    public void addProcessListener(ISdlProcessListener aListener, long aPid) {
        String key = Long.toString(aPid);
        synchronized (mProcessListeners) {
            Collection listeners = (Collection) mProcessListeners.get(key);
            if (listeners == null) mProcessListeners.put(key, (listeners = new SdlSafelyViewableCollection(new LinkedHashSet())));
            listeners.add(aListener);
        }
    }

    public void removeProcessListener(ISdlProcessListener aListener, long aPid) {
        String key = Long.toString(aPid);
        synchronized (mProcessListeners) {
            Collection listeners = (Collection) mProcessListeners.get(key);
            if (listeners != null) {
                listeners.remove(aListener);
                if (listeners.size() == 0) mProcessListeners.put(key, null);
            }
        }
    }

    public void fireInfoEvent(ISdlProcessInfoEvent aInfoEvent) {
        Collection listeners;
        String key = Long.toString(aInfoEvent.getPID());
        synchronized (mProcessListeners) {
            listeners = (Collection) mProcessListeners.get(key);
        }
        if (listeners != null) {
            for (Iterator iter = listeners.iterator(); iter.hasNext(); ) ((ISdlProcessListener) iter.next()).handleProcessInfoEvent(aInfoEvent);
        }
        for (Iterator iter = mGlobalProcessListeners.iterator(); iter.hasNext(); ) ((ISdlProcessListener) iter.next()).handleProcessInfoEvent(aInfoEvent);
    }

    public void fireEvaluationEvent(long aPID, String aExpression, SdlAbstractBpelObject aBPELObject, int aEventID, String aNodePath, String aResult) {
        fireInfoEvent(new SdlProcessInfoEvent(aPID, aNodePath, aEventID, "", aResult));
    }

    public void fireEvent(ISdlProcessEvent aEvent) {
        logger.info("fireEvent() event=" + aEvent.getQName() + ", pid=" + aEvent.getPID());
        Collection listeners;
        String key = Long.toString(aEvent.getPID());
        boolean suspend = false;
        synchronized (mProcessListeners) {
            listeners = (Collection) mProcessListeners.get(key);
        }
        if (listeners != null) {
            for (Iterator iter = listeners.iterator(); iter.hasNext(); ) suspend |= ((ISdlProcessListener) iter.next()).handleProcessEvent(aEvent);
        }
        for (Iterator iter = mGlobalProcessListeners.iterator(); iter.hasNext(); ) suspend |= ((ISdlProcessListener) iter.next()).handleProcessEvent(aEvent);
        if (suspend) {
            try {
                suspendProcess(aEvent.getPID());
            } catch (SdlBusinessProcessException e) {
                logger.error("SdlBusinessProcessException: " + e);
                e.logError();
            }
        }
    }

    public void start() throws SdlBusinessProcessException {
        mStarted = new Date();
    }

    public void stop() throws SdlBusinessProcessException {
        mStarted = null;
    }

    public void shutDown() throws SdlBusinessProcessException {
    }

    public SdlTypeMapping getTypeMapping() {
        return mTypeMapping;
    }

    public void saveReceivedAlarm(long aProcessId, int aLocationId) throws SdlBusinessProcessException {
        getProcessManager().saveReceivedAlarm(aProcessId, aLocationId);
    }

    public void saveReceivedMessage(long aProcessId, int aLocationId, ISdlMessageData aMessageData) throws SdlBusinessProcessException {
        logger.debug("saveReceivedMessage() pid=" + aProcessId + ", locationId=" + aLocationId);
        getProcessManager().saveReceivedMessage(aProcessId, aLocationId, aMessageData);
    }
}
