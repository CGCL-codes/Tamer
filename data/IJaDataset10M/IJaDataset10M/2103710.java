package org.xactor.tm.recovery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.transaction.Status;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import org.jboss.logging.Logger;
import org.xactor.tm.TxManager;
import org.xactor.tm.TxUtils;
import org.xactor.tm.XidFactoryBase;

/**
 * A <code>RecoveryManager</code> object manages the crash recovery process. 
 * At recovery time, the <code>RecoveryManagerService</code> creates a 
 * <code>RecoveryManager</code> instance that interacts with the 
 * <code>RecoveryLogger</code> to read the transaction logs and identify
 * the transactions that were active at the time of the crash. The 
 * <code>RecoveryManager</code> knows how to perform crash recovery for 
 * transactions involving <code>XAResource</code>s that correspond to
 * <code>Recoverable</code> objects known in advance.
 *
 * @author <a href="mailto:bill@jboss.org">Bill Burke</a>
 * @author <a href="mailto:reverbel@ime.usp.br">Francisco Reverbel</a>
 * @version $Revision: 37634 $
 */
public class RecoveryManager {

    /**
    * Class <code>Logger</code> for trace messages.
    */
    private static Logger log = Logger.getLogger(RecoveryManager.class.getName());

    /**
    * A <code>XAResourceAccess</code> implementation that calls 
    * <code>cleanUpResource</code> on a <code>Recoverable</code> instance 
    * if the <code>Recoverable</code> will not be used anymore. The
    * last <code>release</code> call on an <code>XAResourceAccess</code>
    * instance closes the underlying <code>XAConnection</code>. 
    */
    public static class XAResourceAccessImpl implements XAResourceAccess {

        private Recoverable recoverable;

        private int refCount;

        XAResourceAccessImpl(Recoverable recoverable) {
            this.recoverable = recoverable;
            refCount = 1;
        }

        synchronized XAResourceAccess duplicate() {
            refCount++;
            return this;
        }

        public synchronized void release() {
            if (refCount <= 0) log.warn("release called, but refCount=" + refCount + ", this=" + this, new Throwable("[Stack trace]"));
            if (--refCount == 0) recoverable.cleanupResource();
        }
    }

    /**
    * "Struct class" that groups together a <code>Recoverable</code> object,  
    * the <code>XAResource</code> represented by the <code>Recoverable</code>,    
    * and a set of <code>Xids</code> that still need to be processed. 
    */
    private static class XAResourceXids {

        public Recoverable recoverable;

        public XAResource resource;

        public XAResourceAccessImpl resourceAccess;

        public Set xids;

        XAResourceXids(Recoverable recoverable) {
            this.recoverable = recoverable;
            this.resource = recoverable.getResource();
            this.resourceAccess = new XAResourceAccessImpl(recoverable);
            this.xids = new HashSet();
        }
    }

    /**
    * This implementation of <code>TxCompletionHandler</code> handles  
    * transaction completion for transactions recreated by the recovery
    * process.
    */
    private static class CompletionHandler implements TxCompletionHandler {

        /** 
       * The log reader associated with the transaction whose completion
       * is handled by this <code>CompletionHandler</code>.
       */
        private RecoveryLogReader reader;

        /** 
       * Number of pending transactions associated with the log reader. 
       */
        private int pendingTransactions;

        /**
       * Creates a new <code>CompletionHandler</code>.
       * 
       * @param reader log reader associated with the transaction whose 
       *               completion will be handled by the new 
       *               <code>CompletionHandler</code> 
       * @param pendingTransactions number of pending transactions 
       *               associated with the log reader above.  
       */
        CompletionHandler(RecoveryLogReader reader, int pendingTransactions) {
            this.reader = reader;
            this.pendingTransactions = pendingTransactions;
        }

        /**
       * Signals the end of the two-phase commit protocol for a committed 
       * transaction. This method should be invoked when the second phase of the 
       * two-phase commit protocol completes successfully.
       *
       * @param localTransactionId the local id of the completed transaction.
       */
        public void handleTxCompletion(long localTransactionId) {
            if (--pendingTransactions == 0) reader.finishRecovery();
        }
    }

    /**
    * The Xid factory used by this <code>RecoveryManager</code>.
    */
    private XidFactoryBase xidFactory;

    /**
    * The transaction manager used by this <code>RecoveryManager</code>.
    */
    private TxManager txManager;

    /**
    * The recovery logger used by this <code>RecoveryManager</code>.
    */
    private RecoveryLogger recoveryLogger;

    /**
    * Constructs a new <code>RecoveryManager</code>.
    *
    * @param xidFactory the Xid factory that will be used by the new 
    *                   <code>RecoveryManager</code>
    * @param txManager  the transaction manager that will be used by the new 
    *                   <code>RecoveryManager</code>
    * @param recoveryLogger the recovery logger that will be used by the new 
    *                   <code>RecoveryManager</code>
    */
    public RecoveryManager(XidFactoryBase xidFactory, TxManager txManager, RecoveryLogger recoveryLogger) {
        this.xidFactory = xidFactory;
        this.txManager = txManager;
        this.recoveryLogger = recoveryLogger;
    }

    /**
    * Performs crash recovery. This method reads the transaction logs, 
    * identifies the transactions that were active at the time of the crash,
    * and performs crash recovery actions for each such transaction, which
    * may involve any subset of the <code>XAResource</code>s associated with 
    * a given list of <code>Recoverable</code> objects 
    * 
    * @param rcoverables a list of <code>Recoverable</code> objects whose
    *                    <code>XAResource</code>s may be involved in active
    *                    transactions.
    */
    public void recover(ArrayList recoverables) {
        Map heuristicallyCompletedTransactions = new HashMap();
        HeuristicStatusLogReader[] heurStatusLogReaders = recoveryLogger.getHeuristicStatusLogs();
        if (heurStatusLogReaders != null) {
            for (int i = 0; i < heurStatusLogReaders.length; i++) heurStatusLogReaders[i].recover(heuristicallyCompletedTransactions);
            Iterator heurIt = heuristicallyCompletedTransactions.keySet().iterator();
            while (heurIt.hasNext()) {
                Long localId = (Long) heurIt.next();
                LogRecord.HeurData heurData = (LogRecord.HeurData) heuristicallyCompletedTransactions.get(localId);
                recoveryLogger.saveHeuristicStatus(heurData.localTransactionId, heurData.foreignTx, heurData.formatId, heurData.globalTransactionId, heurData.inboundBranchQualifier, heurData.transactionStatus, heurData.heuristicStatusCode, heurData.locallyDetectedHeuristicHazard, heurData.xaResourceHeuristics, heurData.remoteResourceHeuristics);
            }
            for (int i = 0; i < heurStatusLogReaders.length; i++) heurStatusLogReaders[i].finishRecovery();
        }
        RecoveryLogReader[] readers = recoveryLogger.getRecoveryLogs();
        if (readers == null || readers.length == 0) return;
        Set readerBranchQualifiers = new HashSet();
        for (int i = 0; i < readers.length; i++) {
            String branchQualifier = null;
            try {
                branchQualifier = readers[i].getBranchQualifier();
            } catch (Exception e) {
                log.error("logfile corrupted: " + readers[i].getLogFileName(), e);
            }
            readerBranchQualifiers.add(branchQualifier);
            log.info("will recover transactions with branch qualifier " + branchQualifier + " (logFile: " + readers[i].getLogFileName() + ")");
        }
        Map toRecoverMap = new HashMap();
        try {
            for (int i = 0; i < recoverables.size(); i++) {
                Recoverable rec = (Recoverable) recoverables.get(i);
                XAResourceXids xaResXids;
                try {
                    xaResXids = new XAResourceXids(rec);
                } catch (Throwable t) {
                    throw new RuntimeException("Unable to getResource: " + rec.getId() + " aborting recovery.", t);
                }
                toRecoverMap.put(rec.getId(), xaResXids);
                try {
                    xaResXids.xids.addAll(pruneXidList(rec.scan(), readerBranchQualifiers, rec.getId()));
                } catch (XAException e) {
                    throw new RuntimeException("Unable to scan: " + rec.getId(), e);
                }
            }
            recover(readers, heuristicallyCompletedTransactions, toRecoverMap);
        } finally {
            cleanupRecoverables(toRecoverMap.values().iterator());
        }
    }

    /**
    * Performs crash recovery using a given array of log readers and a map 
    * with information on the active XA transaction branches for which 
    * recovery actions must be taken.
    *
    * @param readers      an array of transaction log readers
    * @param heuristicallyCompletedTransactions
    * @param toRecoverMap a map whose keys are <code>Recoverable</code> ids 
    *                     and whose values are <code>XAResourceXids<code> 
    *                     objects, each of which contains the set of 
    *                     <code>Xids</code> for the active XA transaction 
    *                     branches that involve a given <code>XAResource<code>
    *                     and that require recovery actions.
    */
    private void recover(RecoveryLogReader[] readers, Map heuristicallyCompletedTransactions, Map toRecoverMap) {
        boolean presumeRollback = true;
        CorruptedLogRecordException corruptedLogRecordException = null;
        for (int i = 0; i < readers.length; i++) {
            log.info("recovering log file " + readers[i].getLogFileName());
            List committedSingleTmTransactions = new ArrayList();
            List committedMultiTmTransactions = new ArrayList();
            List inDoubtTransactions = new ArrayList();
            List inDoubtJcaTransactions = new ArrayList();
            try {
                readers[i].recover(committedSingleTmTransactions, committedMultiTmTransactions, inDoubtTransactions, inDoubtJcaTransactions);
            } catch (CorruptedLogRecordException e) {
                log.trace("reader threw CorruptedLogRecordException with " + "disablePresumedRollback=" + e.disablePresumedRollback);
                corruptedLogRecordException = e;
                if (corruptedLogRecordException.disablePresumedRollback) presumeRollback = false;
            }
            int pendingTransactions = committedSingleTmTransactions.size() + committedMultiTmTransactions.size() + inDoubtTransactions.size() + inDoubtJcaTransactions.size();
            if (pendingTransactions == 0) readers[i].finishRecovery(); else {
                CompletionHandler completionHandler = new CompletionHandler(readers[i], pendingTransactions);
                resumePendingTransactions(heuristicallyCompletedTransactions, committedSingleTmTransactions, committedMultiTmTransactions, inDoubtTransactions, inDoubtJcaTransactions, toRecoverMap, completionHandler);
            }
        }
        Iterator heurIt = heuristicallyCompletedTransactions.keySet().iterator();
        while (heurIt.hasNext()) {
            Long localId = (Long) heurIt.next();
            LogRecord.HeurData heurData = (LogRecord.HeurData) heuristicallyCompletedTransactions.get(localId);
            heurIt.remove();
            byte[] globalId = heurData.globalTransactionId;
            List xaResourcesWithHeuristics = getXAWork(globalId, toRecoverMap);
            txManager.recreateTransaction(heurData, xaResourcesWithHeuristics, null);
        }
        if (!presumeRollback) {
            log.info("PRESUMED ROLLBACK IS DISABLED DUE TO LOG FILE CORRUPTION.");
        }
        Iterator rit = toRecoverMap.values().iterator();
        while (rit.hasNext()) {
            XAResourceXids xaResXids = (XAResourceXids) rit.next();
            Iterator it = xaResXids.xids.iterator();
            while (it.hasNext()) {
                Xid xid = (Xid) it.next();
                if (!presumeRollback) {
                    log.info("WOULD ROLLBACK " + xidFactory.toString(xid) + " ON RECOVERABLE XAResource " + xaResXids.recoverable.getId() + ", BUT PRESUMED ROLLBACK IS DISABLED");
                } else {
                    try {
                        xaResXids.resource.rollback(xid);
                        log.info("rolledback " + xidFactory.toString(xid) + " on recoverable XAResource " + xaResXids.recoverable.getId());
                    } catch (XAException e) {
                        log.warn("XAException in recover (when rolling back " + "res " + xaResXids.recoverable.getId() + ", xid=" + xidFactory.toString(xid) + "): errorCode=" + TxUtils.getXAErrorCodeAsString(e.errorCode), e);
                    }
                }
            }
        }
        if (corruptedLogRecordException != null) throw corruptedLogRecordException;
    }

    /**
    * Resumes the pending transactions specified by the <code>List</code>
    * arguments.
    * 
    * @param committedSingleTmTransactions a list of 
    *                                      <code>LogRecord.Data</code> objects
    *                                      for the committed single-TM 
    *                                      transactions
    * @param committedMultiTmTransactions a list of <code>LogRecord.Data</code>
    *                                     objects for the committed multi-TM 
    *                                     transactions
    * @param inDoubtTransactions a list of <code>LogRecord.Data</code> objects
    *                            for the in-doubt transactions that entered
    *                            this virtual machine in transaction contexts 
    *                            propagated along with remote method invocations
    * @param inDoubtJcaTransactions a list of <code>LogRecord.Data</code> objects
    *                               for the in-doubt transactions that entered
    *                               this virtual machine through JCA transaction 
    *                               inflow
    * @param toRecoverMap a map whose keys are <code>Recoverable</code> ids and 
    *                     whose values are <code>XAResourceXids<code> objects,
    *                     each of which contains the set of <code>Xids</code>
    *                     for all active XA transaction branches that involve 
    *                     a given <code>XAResource<code> and that require 
    *                     recovery actions           
    * @param completionHandler a <code>TxCompletionHandler</code> that handles 
    *                          the completion of all transactions specified by 
    *                          the preceding arguments.
    */
    private void resumePendingTransactions(Map heuristicallyCompletedTransactions, List committedSingleTmTransactions, List committedMultiTmTransactions, List inDoubtTransactions, List inDoubtJcaTransactions, Map toRecoverMap, TxCompletionHandler completionHandler) {
        Iterator it;
        LogRecord.Data data;
        LogRecord.HeurData heurData;
        it = committedSingleTmTransactions.iterator();
        while (it.hasNext()) {
            data = (LogRecord.Data) it.next();
            byte[] globalId = data.globalTransactionId;
            Long localId = new Long(data.localTransactionId);
            heurData = (LogRecord.HeurData) heuristicallyCompletedTransactions.get(localId);
            if (heurData != null) heuristicallyCompletedTransactions.remove(localId);
            if (heurData != null && !heurData.locallyDetectedHeuristicHazard) {
                List xaResourcesWithHeuristics = getXAWork(globalId, toRecoverMap);
                txManager.recreateTransaction(heurData, xaResourcesWithHeuristics, completionHandler);
            } else {
                List pendingXAWorkList = commitXAWork(globalId, toRecoverMap);
                if (pendingXAWorkList.isEmpty()) {
                    if (heurData == null) {
                        completionHandler.handleTxCompletion(data.localTransactionId);
                    } else {
                        txManager.recreateTransaction(heurData, pendingXAWorkList, completionHandler);
                    }
                } else {
                    txManager.recreateTransaction(data.localTransactionId, pendingXAWorkList, completionHandler, heurData);
                }
            }
        }
        it = committedMultiTmTransactions.iterator();
        while (it.hasNext()) {
            data = (LogRecord.Data) it.next();
            byte[] globalId = data.globalTransactionId;
            Long localId = new Long(data.localTransactionId);
            heurData = (LogRecord.HeurData) heuristicallyCompletedTransactions.get(localId);
            if (heurData != null) heuristicallyCompletedTransactions.remove(localId);
            if (heurData != null && !heurData.locallyDetectedHeuristicHazard) {
                List xaResourcesWithHeuristics = getXAWork(globalId, toRecoverMap);
                txManager.recreateTransaction(heurData, xaResourcesWithHeuristics, completionHandler);
            } else {
                List pendingXAWorkList = commitXAWork(globalId, toRecoverMap);
                txManager.recreateTransaction(data.localTransactionId, pendingXAWorkList, data.resTrmiMechIds, data.resources, completionHandler, heurData);
            }
        }
        it = inDoubtTransactions.iterator();
        while (it.hasNext()) {
            data = (LogRecord.Data) it.next();
            byte[] globalId = data.globalTransactionId;
            Long localId = new Long(data.localTransactionId);
            heurData = (LogRecord.HeurData) heuristicallyCompletedTransactions.get(localId);
            if (heurData != null) heuristicallyCompletedTransactions.remove(localId);
            if (heurData != null && !heurData.locallyDetectedHeuristicHazard) {
                heuristicallyCompletedTransactions.remove(localId);
                List xaResourcesWithHeuristics = getXAWork(globalId, toRecoverMap);
                txManager.recreateTransaction(heurData, xaResourcesWithHeuristics, completionHandler);
            } else {
                if (heurData == null) {
                    List preparedXAWorkList = getXAWork(globalId, toRecoverMap);
                    txManager.recreateTransaction(data.localTransactionId, data.inboundFormatId, data.globalTransactionId, data.recCoorTrmiMechId, data.recoveryCoordinator, preparedXAWorkList, data.resTrmiMechIds, data.resources, completionHandler, null);
                } else {
                    if (heurData.transactionStatus == Status.STATUS_COMMITTING) {
                        List pendingXAWorkList = commitXAWork(globalId, toRecoverMap);
                        txManager.recreateTransaction(data.localTransactionId, data.inboundFormatId, data.globalTransactionId, data.recCoorTrmiMechId, data.recoveryCoordinator, pendingXAWorkList, data.resTrmiMechIds, data.resources, completionHandler, heurData);
                    } else if (heurData.transactionStatus == Status.STATUS_ROLLING_BACK) {
                        List pendingXAWorkList = rollbackXAWork(globalId, toRecoverMap);
                        txManager.recreateTransaction(data.localTransactionId, data.inboundFormatId, data.globalTransactionId, data.recCoorTrmiMechId, data.recoveryCoordinator, pendingXAWorkList, data.resTrmiMechIds, data.resources, completionHandler, heurData);
                    } else {
                        log.warn("Cannot recover tx=" + toString() + "\nInconsistent state", new Throwable("[Stack trace]"));
                    }
                }
            }
        }
        it = inDoubtJcaTransactions.iterator();
        while (it.hasNext()) {
            data = (LogRecord.Data) it.next();
            byte[] globalId = data.globalTransactionId;
            Long localId = new Long(data.localTransactionId);
            heurData = (LogRecord.HeurData) heuristicallyCompletedTransactions.get(localId);
            if (heurData != null) heuristicallyCompletedTransactions.remove(localId);
            if (heurData != null && !heurData.locallyDetectedHeuristicHazard) {
                List xaResourcesWithHeuristics = getXAWork(globalId, toRecoverMap);
                txManager.recreateTransaction(heurData, xaResourcesWithHeuristics, completionHandler);
            } else {
                List preparedXAWorkList = getXAWork(globalId, toRecoverMap);
                if (heurData == null) {
                    txManager.recreateTransaction(data.localTransactionId, data.inboundFormatId, data.globalTransactionId, data.inboundBranchQualifier, preparedXAWorkList, data.resTrmiMechIds, data.resources, completionHandler, null);
                } else {
                    if (heurData.transactionStatus == Status.STATUS_COMMITTING) {
                        List pendingXAWorkList = commitXAWork(globalId, toRecoverMap);
                        txManager.recreateTransaction(data.localTransactionId, data.inboundFormatId, data.globalTransactionId, data.inboundBranchQualifier, pendingXAWorkList, data.resTrmiMechIds, data.resources, completionHandler, heurData);
                    } else if (heurData.transactionStatus == Status.STATUS_ROLLING_BACK) {
                        List pendingXAWorkList = rollbackXAWork(globalId, toRecoverMap);
                        txManager.recreateTransaction(data.localTransactionId, data.inboundFormatId, data.globalTransactionId, data.inboundBranchQualifier, pendingXAWorkList, data.resTrmiMechIds, data.resources, completionHandler, heurData);
                    } else {
                        log.warn("Cannot recover tx=" + toString() + "\nInconsistent state", new Throwable("[Stack trace]"));
                    }
                }
            }
        }
    }

    /**
    * Commits the XA work associated with a given global transaction id. This
    * method receives a <code>toRecoverMap</code> whose values are 
    * <code>XAResourceXids<code> objects that contain the <code>Xids</code>
    * of all active XA transaction branches that require recovery actions. 
    * It removes from the <code>toRecoverMap</code> all <code>Xids</code> 
    * that correspond to the XA work committed (those associated with the
    * specified <code>globalId</code>).
    * 
    * @param globalId the global transaction id associated with the work to 
    *                 commit
    * @param toRecoverMap a map whose keys are <code>Recoverable</code> ids and
    *                     whose values are <code>XAResourceXids<code> objects,
    *                     each of which contains the set of <code>Xids</code>
    *                     for all active XA transaction branches that involve 
    *                     a given <code>XAResource<code> and that require 
    *                     recovery actions
    * @return a "pending work" list containing <code>XAWork</code> instances 
    *         describing the work that should have been committed, but could 
    *         not be committed due to transient problems. The caller should 
    *         try to commit the pending work again, at a later time.
    */
    private List commitXAWork(byte[] globalId, Map toRecoverMap) {
        log.info("*** trying to complete XA work with globalId " + new String(globalId).trim());
        globalId = pad(globalId);
        List pendingXAWorkList = new ArrayList();
        Iterator rit = toRecoverMap.values().iterator();
        while (rit.hasNext()) {
            XAResourceXids toRecover = (XAResourceXids) rit.next();
            log.info("    looking at resource " + toRecover.recoverable.getId());
            Iterator resXidIt = toRecover.xids.iterator();
            while (resXidIt.hasNext()) {
                Xid resXid = (Xid) resXidIt.next();
                byte[] resGlobalId = pad(resXid.getGlobalTransactionId());
                if (!Arrays.equals(globalId, resGlobalId)) continue;
                try {
                    toRecover.resource.commit(resXid, false);
                    log.info("        committed: " + resXid);
                } catch (XAException e) {
                    switch(e.errorCode) {
                        case XAException.XA_HEURCOM:
                            log.trace("commitXAWork ignored XAException.XA_HEURCOM", e);
                            try {
                                toRecover.resource.forget(resXid);
                            } catch (XAException xae) {
                                log.warn("XAException in commitXAWork (when forgetting " + "XA_HEURCOM): errorCode=" + TxUtils.getXAErrorCodeAsString(xae.errorCode), xae);
                            }
                            break;
                        case XAException.XA_HEURRB:
                        case XAException.XA_HEURMIX:
                        case XAException.XA_HEURHAZ:
                            log.warn("Heuristic XAException in commitXAWork: errorCode=" + TxUtils.getXAErrorCodeAsString(e.errorCode) + "\nWill deal with the heuristic later", e);
                            XAWork postponedWork = new XAWork(toRecover.resource, resXid, toRecover.resourceAccess);
                            pendingXAWorkList.add(postponedWork);
                            break;
                        case XAException.XAER_RMERR:
                            log.warn("Unexpected XAException in commitXAWork: errorCode=" + TxUtils.getXAErrorCodeAsString(e.errorCode), e);
                            break;
                        case XAException.XAER_RMFAIL:
                        case XAException.XA_RETRY:
                            log.warn("XAException in commitXAWork: errorCode=" + TxUtils.getXAErrorCodeAsString(e.errorCode) + "\nWill attempt to commit the XAResource later", e);
                            XAWork pendingXAWork = new XAWork(toRecover.resource, resXid, toRecover.resourceAccess);
                            pendingXAWorkList.add(pendingXAWork);
                            break;
                        case XAException.XAER_NOTA:
                        case XAException.XAER_INVAL:
                        case XAException.XAER_PROTO:
                        default:
                            log.warn("Could not recover from unexpected XAException: " + " errorCode=" + TxUtils.getXAErrorCodeAsString(e.errorCode), e);
                            break;
                    }
                } finally {
                    resXidIt.remove();
                }
            }
            if (toRecover.xids.isEmpty()) rit.remove();
        }
        return pendingXAWorkList;
    }

    private List rollbackXAWork(byte[] globalId, Map toRecoverMap) {
        log.info("*** trying to rollback XA work with globalId " + new String(globalId).trim());
        globalId = pad(globalId);
        List pendingXAWorkList = new ArrayList();
        Iterator rit = toRecoverMap.values().iterator();
        while (rit.hasNext()) {
            XAResourceXids toRecover = (XAResourceXids) rit.next();
            log.info("    looking at resource " + toRecover.recoverable.getId());
            Iterator resXidIt = toRecover.xids.iterator();
            while (resXidIt.hasNext()) {
                Xid resXid = (Xid) resXidIt.next();
                byte[] resGlobalId = pad(resXid.getGlobalTransactionId());
                if (!Arrays.equals(globalId, resGlobalId)) continue;
                try {
                    toRecover.resource.rollback(resXid);
                    log.info("        rolledback: " + resXid);
                } catch (XAException e) {
                    switch(e.errorCode) {
                        case XAException.XA_HEURRB:
                            log.trace("rollbackXAWork ignored XAException.XA_HEURRB", e);
                            try {
                                toRecover.resource.forget(resXid);
                            } catch (XAException xae) {
                                log.warn("XAException in rollbackXAWork (when forgetting " + "XA_HEURRB): errorCode=" + TxUtils.getXAErrorCodeAsString(xae.errorCode), xae);
                            }
                            break;
                        case XAException.XA_HEURCOM:
                        case XAException.XA_HEURMIX:
                        case XAException.XA_HEURHAZ:
                            log.warn("Heuristic XAException in rollbackXAWork: errorCode=" + TxUtils.getXAErrorCodeAsString(e.errorCode) + "\nWill deal with the heuristic later", e);
                            XAWork postponedWork = new XAWork(toRecover.resource, resXid, toRecover.resourceAccess);
                            pendingXAWorkList.add(postponedWork);
                            break;
                        case XAException.XAER_RMERR:
                            log.warn("Unexpected XAException in rollbackXAWork: " + "errorCode=" + TxUtils.getXAErrorCodeAsString(e.errorCode), e);
                            break;
                        case XAException.XAER_RMFAIL:
                        case XAException.XA_RETRY:
                            log.warn("XAException in rollbackXAWork: errorCode=" + TxUtils.getXAErrorCodeAsString(e.errorCode) + "\nWill attempt to rollback the XAResource later", e);
                            XAWork pendingXAWork = new XAWork(toRecover.resource, resXid, toRecover.resourceAccess);
                            pendingXAWorkList.add(pendingXAWork);
                            break;
                        case XAException.XAER_NOTA:
                        case XAException.XAER_INVAL:
                        case XAException.XAER_PROTO:
                        default:
                            log.warn("Could not recover from unexpected XAException: " + " errorCode=" + TxUtils.getXAErrorCodeAsString(e.errorCode), e);
                            break;
                    }
                } finally {
                    resXidIt.remove();
                }
            }
            if (toRecover.xids.isEmpty()) rit.remove();
        }
        return pendingXAWorkList;
    }

    /**
    * Extracts from a <code>toRecoverMap</code> all the XA work that is
    * associated with a given global transaction id. This method scans
    * the <code>toRecoverMap</code> and builds a list of <code>XAWork</code>
    * instances whose <code>Xids</code> contain the specified global id.
    * It removes all those <code>Xids</code> from the the 
    * <code>toRecoverMap</code>. 
    *  
    * @param globalId the global transaction id
    * @param toRecoverMap a map whose keys are <code>Recoverable</code> ids 
    *                     and whose values are <code>XAResourceXids<code> 
    *                     objects, each of which contains the set of 
    *                     <code>Xids</code> for the active XA transaction 
    *                     branches that involve a given <code>XAResource<code>
    *                     and that require recovery actions.
    * @return a <code>List</code> of <code>XAWork</code> instances with 
    *         <code>Xid</code> fields that were taken from the 
    *         <code>toRecoverMap</code> and that contain the specified global 
    *         transaction id.
    */
    private List getXAWork(byte[] globalId, Map toRecoverMap) {
        log.info("*** getting XA work with globalId " + new String(globalId).trim());
        globalId = pad(globalId);
        List xaWorkList = new ArrayList();
        Iterator rit = toRecoverMap.values().iterator();
        while (rit.hasNext()) {
            XAResourceXids toRecover = (XAResourceXids) rit.next();
            log.info("    looking at resource " + toRecover.recoverable.getId());
            Iterator resXidIt = toRecover.xids.iterator();
            while (resXidIt.hasNext()) {
                Xid resXid = (Xid) resXidIt.next();
                byte[] resGlobalId = pad(resXid.getGlobalTransactionId());
                if (!Arrays.equals(globalId, resGlobalId)) continue;
                XAWork preparedXAWork = new XAWork(toRecover.resource, resXid, toRecover.resourceAccess);
                xaWorkList.add(preparedXAWork);
                resXidIt.remove();
            }
        }
        return xaWorkList;
    }

    /**
    * Takes an iterator for a collection of <code>XAResourceXids</code> 
    * instances and cleans up every <code>Recoverable</code> in the 
    * <code>recoverable</code> field of an element of the collection.
    */
    private void cleanupRecoverables(Iterator it) {
        while (it.hasNext()) {
            XAResourceXids xaResXids = (XAResourceXids) it.next();
            try {
                xaResXids.resourceAccess.release();
            } catch (Exception ignored) {
            }
        }
    }

    /**
    * Filters out every xid whose branch qualifier field was not generated by
    * transaction manager. This is to avoid rolling back transaction branches
    * that are not ours.
    */
    private List pruneXidList(Xid[] xids, Set branchQualifiers, String resourceName) {
        ArrayList list = new ArrayList();
        for (int i = 0; i < xids.length; i++) {
            byte[] branchQual = xids[i].getBranchQualifier();
            String baseBranchQual = xidFactory.getBaseBranchQualifier(branchQual);
            if (branchQualifiers.contains(baseBranchQual)) {
                list.add(xids[i]);
                log.info("Adding xid " + xidFactory.toString(xids[i]) + " to pruned Xid list for " + resourceName);
            }
        }
        return list;
    }

    /**
    * Pads a byte array with null bytes so that the length of the padded
    * array is <code>Xid.MAXGTRIDSIZE</code>. Called before comparing 
    * global transaction ids.
    * 
    */
    private byte[] pad(byte[] globalId) {
        if (globalId.length < Xid.MAXGTRIDSIZE) {
            byte[] bytes = new byte[Xid.MAXGTRIDSIZE];
            System.arraycopy(globalId, 0, bytes, 0, globalId.length);
            globalId = bytes;
        }
        return globalId;
    }
}
