package org.tripcom.distribution.oprocessor;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import net.jini.core.lease.Lease;
import org.apache.log4j.Logger;
import org.tripcom.distribution.client.OutVirtualClient;
import org.tripcom.distribution.controller.Constants;
import org.tripcom.distribution.controller.Controller;
import org.tripcom.distribution.distributedspaces.KernelFinder;
import org.tripcom.distribution.distributedspaces.KernelFinder.CouldntFindKernelException;
import org.tripcom.integration.entry.KernelAddress;
import org.tripcom.integration.entry.OutDMEntry;
import org.tripcom.integration.entry.OutMetaMEntry;
import org.tripcom.integration.entry.transaction.EndTransaction;
import org.tripcom.integration.entry.transaction.EndTransactionResult;

/**
 * 
 * LindaOutThread takes the write-op entries out of the system bus and starts a
 * new thread for every taken tuple to operate it
 * 
 * @author Kia Teymourian
 * 
 */
public class OutThread extends Thread {

    private static Logger log = Logger.getLogger(OutThread.class);

    public static final Queue<OutDMEntry> indexingCache = new LinkedList<OutDMEntry>();

    private static final OutThread SHARED_INSTANCE = new OutThread();

    public static OutThread sharedInstance() {
        return SHARED_INSTANCE;
    }

    /**
	 * The constructor must be protected to ensure that only subclasses can call
	 * it and that only one instance can ever get created. A client that tries
	 * to instantiate PGridWrapper directly will get an error at compile-time.
	 */
    private OutThread() {
    }

    public void run() {
        while (true) {
            OutDMEntry myTakenEntry = takeEntry();
            KernelAddress remoteKernel;
            try {
                remoteKernel = KernelFinder.findManagingKernel(myTakenEntry.space, myTakenEntry.operationID);
            } catch (CouldntFindKernelException e1) {
                return;
            }
            try {
                if (log.isDebugEnabled()) log.debug("Out Entry, lookup operation refer that Space is hosted on: " + remoteKernel);
                if (Controller.localkernel.equals(remoteKernel)) {
                    writeForMetadata(myTakenEntry);
                    if (Boolean.parseBoolean(Constants.INDEXING)) {
                        if (log.isInfoEnabled()) log.info("DM: Indexing is enabled");
                        new IndexingThread().insertORDelete(myTakenEntry.getTripleSet(), myTakenEntry.getSpace().toString(), true);
                        if (log.isInfoEnabled()) log.info("Out Indexing Thread started");
                    } else {
                        if (log.isDebugEnabled()) log.debug("Indexing is disabled");
                    }
                } else {
                    OutVirtualClient mClient = new OutVirtualClient(myTakenEntry, remoteKernel);
                    mClient.start();
                }
            } catch (Exception e) {
                log.error("Exception in OperationProcessor", e);
            }
        }
    }

    /**
	 * 
	 * @return
	 */
    public static OutDMEntry takeEntry() {
        synchronized (indexingCache) {
            while (indexingCache.isEmpty()) {
                try {
                    indexingCache.wait();
                } catch (InterruptedException e) {
                    log.error("DM, Indexing Thread", e);
                }
            }
            return indexingCache.remove();
        }
    }

    @SuppressWarnings("unchecked")
    public static int numOfTripelsInCache() {
        synchronized (indexingCache) {
            int num = 0;
            Iterator i = indexingCache.iterator();
            while (i.hasNext()) {
                OutDMEntry outEntry = (OutDMEntry) i.next();
                num = num + outEntry.data.size();
            }
            indexingCache.notifyAll();
            return num;
        }
    }

    /**
	 * 
	 * @param outDM
	 */
    private void writeForMetadata(OutDMEntry outDM) {
        OutMetaMEntry outMM = new OutMetaMEntry();
        outMM.clientInfo = outDM.clientInfo;
        outMM.data = outDM.data;
        outMM.operationID = outDM.operationID;
        outMM.space = outDM.space;
        outMM.timestamp = outDM.timestamp;
        outMM.transactionID = outDM.transactionID;
        try {
            Controller.systemBus.write(outMM, null, Lease.FOREVER);
        } catch (Exception e) {
            log.error("LindaOutThread: Could not wrrite to Systen BUS " + e.toString(), e);
        }
    }

    /**
	 * 
	 * @param myTakenEntry
	 * @param mytrans
	 */
    public void writeTransactionResult(EndTransaction mytrans) {
        EndTransactionResult mResult = new EndTransactionResult();
        mResult.operationID = mytrans.operationID;
        try {
            Controller.systemBus.write(mResult, null, Lease.FOREVER);
        } catch (Exception e) {
            log.error("LindaOutThread: Could not wrrite to Systen BUS " + e.toString(), e);
        }
    }
}
