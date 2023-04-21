package com.yahoo.zookeeper.server.quorum;

import com.yahoo.zookeeper.ZooDefs;
import com.yahoo.zookeeper.server.Request;
import com.yahoo.zookeeper.server.RequestProcessor;
import com.yahoo.zookeeper.server.SyncRequestProcessor;

/**
 * This RequestProcessor simply forwards requests to an AckRequestProcessor and
 * SyncRequestProcessor.
 */
public class ProposalRequestProcessor implements RequestProcessor {

    LeaderZooKeeperServer zks;

    RequestProcessor nextProcessor;

    SyncRequestProcessor syncProcessor;

    public ProposalRequestProcessor(LeaderZooKeeperServer zks, RequestProcessor nextProcessor) {
        this.zks = zks;
        this.nextProcessor = nextProcessor;
        AckRequestProcessor ackProcessor = new AckRequestProcessor(zks.getLeader());
        syncProcessor = new SyncRequestProcessor(zks, ackProcessor);
    }

    public void processRequest(Request request) {
        if (request.type == ZooDefs.OpCode.sync) {
            zks.getLeader().processSync(request);
            if (!zks.getLeader().syncHandler.containsKey(request.sessionId)) {
                zks.getLeader().syncHandler.put(request.sessionId, null);
                nextProcessor.processRequest(request);
            }
        } else {
            nextProcessor.processRequest(request);
            if (request.hdr != null) {
                zks.getLeader().propose(request);
                syncProcessor.processRequest(request);
            }
        }
    }

    public void shutdown() {
        nextProcessor.shutdown();
        syncProcessor.shutdown();
    }
}
