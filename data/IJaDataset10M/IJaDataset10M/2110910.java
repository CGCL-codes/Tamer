package net.assimilator.monitor;

import com.sun.jini.reliableLog.LogHandler;
import net.assimilator.core.OperationalString;
import net.assimilator.core.OperationalStringException;
import net.assimilator.resources.persistence.SnapshotHandler;
import java.io.*;
import java.rmi.MarshalledObject;
import java.util.*;
import java.util.logging.Level;

/**
 * Class that manages the persistence details behind saving and restoring
 * OperationalStrings
 */
class OpStringLogHandler extends LogHandler implements SnapshotHandler {

    /**
     * Collection of of recovered Operational Strings to add
     */
    List<OperationalString> recoveredOpstrings = new ArrayList<OperationalString>();

    /**
     * Collection of of recovered Operational Strings to add
     */
    List<RecordHolder> updatedOpstrings = new ArrayList<RecordHolder>();

    /**
     * flag to indicate whether OperationalStrings have been recovered
     */
    boolean opStringsRecovered = false;

    private ProvisionMonitorImpl provisionMonitor;

    public OpStringLogHandler(ProvisionMonitorImpl provisionMonitor) {
        this.provisionMonitor = provisionMonitor;
    }

    public void snapshot(OutputStream out) throws IOException {
        ObjectOutputStream oostream = new ObjectOutputStream(out);
        oostream.writeUTF(ProvisionMonitorImpl.class.getName());
        oostream.writeInt(ProvisionMonitorImpl.LOG_VERSION);
        ArrayList<OperationalString> list = new ArrayList<OperationalString>();
        OperationalString[] opStrings = provisionMonitor.getOperationalStrings();
        list.addAll(Arrays.asList(opStrings));
        oostream.writeObject(new MarshalledObject(list));
        oostream.flush();
    }

    /**
     * Required method implementing the abstract recover() defined in
     * ReliableLog's associated LogHandler class. This callback is invoked
     * from the recover method of ReliableLog.
     */
    public void recover(InputStream in) throws Exception {
        provisionMonitor.inRecovery = true;
        ObjectInputStream oistream = new ObjectInputStream(in);
        if (!ProvisionMonitorImpl.class.getName().equals(oistream.readUTF())) {
            throw new IOException("Log from wrong implementation");
        }
        if (oistream.readInt() != ProvisionMonitorImpl.LOG_VERSION) {
            throw new IOException("Wrong log format version");
        }
        MarshalledObject mo = (MarshalledObject) oistream.readObject();
        ArrayList list = (ArrayList) mo.get();
        for (Object aList : list) {
            OperationalString opString = (OperationalString) aList;
            if (ProvisionMonitorImpl.logger.isLoggable(Level.FINER)) {
                ProvisionMonitorImpl.logger.finer("Recovered : " + opString.getName());
            }
            recoveredOpstrings.add(opString);
            opStringsRecovered = true;
        }
    }

    /**
     * Required method implementing the abstract applyUpdate() defined in
     * ReliableLog's associated LogHandler class.
     * <p/>
     * During state recovery, the recover() method defined in the
     * ReliableLog class is invoked. That method invokes the method
     * recoverUpdates() which invokes the method readUpdates(). Both of
     * those methods are defined in ReliableLog. The method readUpdates()
     * retrieves a record from the log file and then invokes this method.
     */
    public void applyUpdate(Object update) throws Exception {
        if (update instanceof MarshalledObject) {
            RecordHolder holder = (RecordHolder) ((MarshalledObject) update).get();
            updatedOpstrings.add(holder);
            opStringsRecovered = true;
        }
    }

    /**
     * Called by <code>PersistentStore</code> after every update to give
     * server a chance to trigger a snapshot <br>
     *
     * @param updateCount Number of updates since last snapshot
     */
    public void updatePerformed(int updateCount) {
        if (updateCount >= provisionMonitor.logToSnapshotThresh) {
            provisionMonitor.getSnapshotter().takeSnapshot();
        }
    }

    /**
     * Delegate snapshot request to PersistentStore
     */
    public void takeSnapshot() {
        provisionMonitor.getSnapshotter().takeSnapshot();
    }

    /**
     * Determine if OperationalString objects have been recovered or updated
     *
     * @return boolean <code/true</code> if OperationalString objects have
     *         been recovered or updated, otherwise <code>false</code>
     */
    boolean opStringsRecovered() {
        return (opStringsRecovered);
    }

    /**
     * Process recovered OperationalString objects
     */
    void processRecoveredOpStrings() {
        for (OperationalString opString : recoveredOpstrings) {
            try {
                if (!provisionMonitor.opStringExists(opString.getName())) {
                    HashMap map = new HashMap();
                    provisionMonitor.addOperationalString(opString, map, null, true, null);
                    provisionMonitor.dumpOpStringError(map);
                } else {
                    OpStringManager opMgr = provisionMonitor.getOpStringManager(opString.getName());
                    Map map = opMgr.updateOperationalString(opString);
                    provisionMonitor.dumpOpStringError(map);
                }
            } catch (Exception ex) {
                ProvisionMonitorImpl.logger.log(Level.SEVERE, "Processing recovered OperationalStrings", ex);
            }
        }
        recoveredOpstrings.clear();
    }

    /**
     * Process updated OperationalString objects
     *
     * @throws net.assimilator.core.OperationalStringException
     *          if we are unable to update the op strings
     */
    void processUpdatedOpStrings() throws OperationalStringException {
        for (RecordHolder holder : updatedOpstrings) {
            OperationalString opString = holder.getOperationalString();
            try {
                if (holder.getAction() == RecordHolder.MODIFIED) {
                    if (!provisionMonitor.opStringExists(opString.getName())) {
                        HashMap map = new HashMap();
                        provisionMonitor.addOperationalString(opString, map, null, true, null);
                        provisionMonitor.dumpOpStringError(map);
                    } else {
                        OpStringManager opMgr = provisionMonitor.getOpStringManager(opString.getName());
                        Map map = opMgr.updateOperationalString(opString);
                        provisionMonitor.dumpOpStringError(map);
                    }
                } else {
                    provisionMonitor.undeploy(opString.getName(), false);
                }
            } catch (Exception ex) {
                ProvisionMonitorImpl.logger.log(Level.SEVERE, "Processing updated OperationalStrings", ex);
            }
        }
        updatedOpstrings.clear();
    }
}
