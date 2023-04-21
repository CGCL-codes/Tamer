package org.apache.zookeeper.server.quorum;

import java.util.ArrayList;
import java.util.LinkedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.zookeeper.ZooDefs.OpCode;
import org.apache.zookeeper.server.Request;
import org.apache.zookeeper.server.RequestProcessor;

/**
 * This RequestProcessor matches the incoming committed requests with the
 * locally submitted requests. The trick is that locally submitted requests that
 * change the state of the system will come back as incoming committed requests,
 * so we need to match them up.
 */
public class CommitProcessor extends Thread implements RequestProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(CommitProcessor.class);

    /**
     * Requests that we are holding until the commit comes in.
     */
    LinkedList<Request> queuedRequests = new LinkedList<Request>();

    /**
     * Requests that have been committed.
     */
    LinkedList<Request> committedRequests = new LinkedList<Request>();

    RequestProcessor nextProcessor;

    ArrayList<Request> toProcess = new ArrayList<Request>();

    /**
     * This flag indicates whether we need to wait for a response to come back from the
     * leader or we just let the sync operation flow through like a read. The flag will
     * be true if the CommitProcessor is in a Leader pipeline.
     */
    boolean matchSyncs;

    public CommitProcessor(RequestProcessor nextProcessor, String id, boolean matchSyncs) {
        super("CommitProcessor:" + id);
        this.nextProcessor = nextProcessor;
        this.matchSyncs = matchSyncs;
    }

    volatile boolean finished = false;

    @Override
    public void run() {
        try {
            Request nextPending = null;
            while (!finished) {
                int len = toProcess.size();
                for (int i = 0; i < len; i++) {
                    nextProcessor.processRequest(toProcess.get(i));
                }
                toProcess.clear();
                synchronized (this) {
                    if ((queuedRequests.size() == 0 || nextPending != null) && committedRequests.size() == 0) {
                        wait();
                        continue;
                    }
                    if ((queuedRequests.size() == 0 || nextPending != null) && committedRequests.size() > 0) {
                        Request r = committedRequests.remove();
                        if (nextPending != null && nextPending.sessionId == r.sessionId && nextPending.cxid == r.cxid) {
                            nextPending.hdr = r.hdr;
                            nextPending.txn = r.txn;
                            nextPending.zxid = r.zxid;
                            toProcess.add(nextPending);
                            nextPending = null;
                        } else {
                            toProcess.add(r);
                        }
                    }
                }
                if (nextPending != null) {
                    continue;
                }
                synchronized (this) {
                    while (nextPending == null && queuedRequests.size() > 0) {
                        Request request = queuedRequests.remove();
                        switch(request.type) {
                            case OpCode.create:
                            case OpCode.delete:
                            case OpCode.setData:
                            case OpCode.multi:
                            case OpCode.setACL:
                            case OpCode.createSession:
                            case OpCode.closeSession:
                                nextPending = request;
                                break;
                            case OpCode.sync:
                                if (matchSyncs) {
                                    nextPending = request;
                                } else {
                                    toProcess.add(request);
                                }
                                break;
                            default:
                                toProcess.add(request);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            LOG.warn("Interrupted exception while waiting", e);
        } catch (Throwable e) {
            LOG.error("Unexpected exception causing CommitProcessor to exit", e);
        }
        LOG.info("CommitProcessor exited loop!");
    }

    public synchronized void commit(Request request) {
        if (!finished) {
            if (request == null) {
                LOG.warn("Committed a null!", new Exception("committing a null! "));
                return;
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Committing request:: " + request);
            }
            committedRequests.add(request);
            notifyAll();
        }
    }

    public synchronized void processRequest(Request request) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Processing request:: " + request);
        }
        if (!finished) {
            queuedRequests.add(request);
            notifyAll();
        }
    }

    public void shutdown() {
        LOG.info("Shutting down");
        synchronized (this) {
            finished = true;
            queuedRequests.clear();
            notifyAll();
        }
        if (nextProcessor != null) {
            nextProcessor.shutdown();
        }
    }
}
