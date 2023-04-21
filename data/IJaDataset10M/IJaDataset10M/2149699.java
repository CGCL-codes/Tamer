package net.sf.katta.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.katta.operation.OperationId;
import net.sf.katta.operation.master.MasterOperation;
import net.sf.katta.operation.node.OperationResult;
import net.sf.katta.protocol.ConnectedComponent;
import net.sf.katta.protocol.IAddRemoveListener;
import net.sf.katta.protocol.InteractionProtocol;
import net.sf.katta.util.ZkConfiguration.PathDef;
import org.I0Itec.zkclient.IZkDataListener;
import org.apache.log4j.Logger;

/**
 * When watchdog for a list of {@link NodeOperation}s. The watchdog is finished
 * if all operations are done or the nodes of the incomplete nodes went down.
 */
public class OperationWatchdog implements ConnectedComponent, Serializable {

    private static final long serialVersionUID = 1L;

    protected static final Logger LOG = Logger.getLogger(OperationWatchdog.class);

    private final String _queueElementId;

    private final List<OperationId> _openOperationIds;

    private final List<OperationId> _operationIds;

    private MasterContext _context;

    private final MasterOperation _masterOperation;

    public OperationWatchdog(String queueElementId, MasterOperation masterOperation, List<OperationId> operationIds) {
        _queueElementId = queueElementId;
        _operationIds = operationIds;
        _masterOperation = masterOperation;
        _openOperationIds = new ArrayList<OperationId>(operationIds);
    }

    public void start(MasterContext context) {
        _context = context;
        subscribeNotifications();
    }

    private final synchronized void subscribeNotifications() {
        checkDeploymentForCompletion();
        if (isDone()) {
            return;
        }
        InteractionProtocol protocol = _context.getProtocol();
        protocol.registerChildListener(this, PathDef.NODES_LIVE, new IAddRemoveListener() {

            @Override
            public void removed(String name) {
                checkDeploymentForCompletion();
            }

            @Override
            public void added(String name) {
            }
        });
        IZkDataListener dataListener = new IZkDataListener() {

            @Override
            public void handleDataDeleted(String arg0) throws Exception {
                checkDeploymentForCompletion();
            }

            @Override
            public void handleDataChange(String arg0, Object arg1) throws Exception {
            }
        };
        for (OperationId operationId : _openOperationIds) {
            protocol.registerNodeOperationListener(this, operationId, dataListener);
        }
        checkDeploymentForCompletion();
    }

    protected final synchronized void checkDeploymentForCompletion() {
        if (isDone()) {
            return;
        }
        List<String> liveNodes = _context.getProtocol().getLiveNodes();
        for (Iterator<OperationId> iter = _openOperationIds.iterator(); iter.hasNext(); ) {
            OperationId operationId = iter.next();
            if (!_context.getProtocol().isNodeOperationQueued(operationId) || !liveNodes.contains(operationId.getNodeName())) {
                iter.remove();
            }
        }
        if (isDone()) {
            finishWatchdog();
        } else {
            LOG.debug("still " + getOpenOperationCount() + " open deploy operations");
        }
    }

    public synchronized void cancel() {
        _context.getProtocol().unregisterComponent(this);
        this.notifyAll();
    }

    private synchronized void finishWatchdog() {
        InteractionProtocol protocol = _context.getProtocol();
        protocol.unregisterComponent(this);
        try {
            List<OperationResult> operationResults = new ArrayList<OperationResult>(_openOperationIds.size());
            for (OperationId operationId : _operationIds) {
                OperationResult operationResult = protocol.getNodeOperationResult(operationId, true);
                if (operationResult != null && operationResult.getUnhandledException() != null) {
                    LOG.error("received unhandlde exception from node " + operationId.getNodeName(), operationResult.getUnhandledException());
                }
                operationResults.add(operationResult);
            }
            _masterOperation.nodeOperationsComplete(_context, operationResults);
        } catch (Exception e) {
            LOG.info("operation complete action of " + _masterOperation + " failed", e);
        }
        LOG.info("watch for " + _masterOperation + " finished");
        this.notifyAll();
        _context.getMasterQueue().removeWatchdog(this);
    }

    public String getQueueElementId() {
        return _queueElementId;
    }

    public MasterOperation getOperation() {
        return _masterOperation;
    }

    public List<OperationId> getOperationIds() {
        return _operationIds;
    }

    public final int getOpenOperationCount() {
        return _openOperationIds.size();
    }

    public boolean isDone() {
        return _openOperationIds.isEmpty();
    }

    public final synchronized void join() throws InterruptedException {
        join(0);
    }

    public final synchronized void join(long timeout) throws InterruptedException {
        if (!isDone()) {
            this.wait(timeout);
        }
    }

    @Override
    public final void disconnect() {
    }

    @Override
    public final void reconnect() {
    }
}
