package com.yahoo.zookeeper.server.quorum;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.log4j.Logger;
import com.yahoo.jute.BinaryOutputArchive;
import com.yahoo.zookeeper.server.FinalRequestProcessor;
import com.yahoo.zookeeper.server.Request;
import com.yahoo.zookeeper.server.RequestProcessor;

/**
 * This class has the control logic for the Leader.
 */
public class Leader {

    private static final Logger LOG = Logger.getLogger(Leader.class);

    public static class Proposal {

        public QuorumPacket packet;

        public int ackCount;

        public Request request;

        public String toString() {
            return packet.getType() + ", " + packet.getZxid() + ", " + request;
        }
    }

    LeaderZooKeeperServer zk;

    QuorumPeer self;

    public HashSet<FollowerHandler> followers = new HashSet<FollowerHandler>();

    public HashSet<FollowerHandler> forwardingFollowers = new HashSet<FollowerHandler>();

    public HashMap<Long, Request> pendingSyncs = new HashMap<Long, Request>();

    public HashMap<Long, FollowerHandler> syncHandler = new HashMap<Long, FollowerHandler>();

    /**
     * Adds follower to the leader.
     * 
     * @param follower
     *                instance of follower handle
     */
    void addFollowerHandler(FollowerHandler follower) {
        synchronized (followers) {
            followers.add(follower);
        }
    }

    /**
     * Remove the follower from the followers list
     * 
     * @param follower
     */
    void removeFollowerHandler(FollowerHandler follower) {
        synchronized (forwardingFollowers) {
            forwardingFollowers.remove(follower);
        }
        synchronized (followers) {
            followers.remove(follower);
        }
    }

    boolean isFollowerSynced(FollowerHandler follower) {
        synchronized (forwardingFollowers) {
            return forwardingFollowers.contains(follower);
        }
    }

    ServerSocket ss;

    Leader(QuorumPeer self, LeaderZooKeeperServer zk) throws IOException {
        this.self = self;
        try {
            ss = new ServerSocket(self.getQuorumAddress().getPort());
        } catch (BindException e) {
            LOG.error("Couldn't bind to port " + self.getQuorumAddress().getPort());
            throw e;
        }
        this.zk = zk;
    }

    /**
     * This message is for follower to expect diff
     */
    static final int DIFF = 13;

    /**
     * This is for follower to truncate its logs 
     */
    static final int TRUNC = 14;

    /**
     * This is for follower to download the snapshots
     */
    static final int SNAP = 15;

    /**
     * This message type is sent by the leader to indicate it's zxid and if
     * needed, its database.
     */
    static final int NEWLEADER = 10;

    /**
     * This message type is sent by a follower to indicate the last zxid in its
     * log.
     */
    static final int LASTZXID = 11;

    /**
     * This message type is sent by the leader to indicate that the follower is
     * now uptodate andt can start responding to clients.
     */
    static final int UPTODATE = 12;

    /**
     * This message type is sent to a leader to request and mutation operation.
     * The payload will consist of a request header followed by a request.
     */
    static final int REQUEST = 1;

    /**
     * This message type is sent by a leader to propose a mutation.
     */
    public static final int PROPOSAL = 2;

    /**
     * This message type is sent by a follower after it has synced a proposal.
     */
    static final int ACK = 3;

    /**
     * This message type is sent by a leader to commit a proposal and cause
     * followers to start serving the corresponding data.
     */
    static final int COMMIT = 4;

    /**
     * This message type is enchanged between follower and leader (initiated by
     * follower) to determine liveliness.
     */
    static final int PING = 5;

    /**
     * This message type is to validate a session that should be active.
     */
    static final int REVALIDATE = 6;

    /**
     * This message is a reply to a synchronize command flushing the pipe
     * between the leader and the follower.
     */
    static final int SYNC = 7;

    private ConcurrentLinkedQueue<Proposal> outstandingProposals = new ConcurrentLinkedQueue<Proposal>();

    ConcurrentLinkedQueue<Proposal> toBeApplied = new ConcurrentLinkedQueue<Proposal>();

    Proposal newLeaderProposal = new Proposal();

    /**
     * This method is main function that is called to lead
     * 
     * @throws IOException
     * @throws InterruptedException
     */
    void lead() throws IOException, InterruptedException {
        self.tick = 0;
        zk.loadData();
        zk.startup();
        long epoch = self.getLastLoggedZxid() >> 32L;
        epoch++;
        zk.setZxid(epoch << 32L);
        zk.dataTree.lastProcessedZxid = zk.getZxid();
        lastProposed = zk.getZxid();
        newLeaderProposal.packet = new QuorumPacket(NEWLEADER, zk.getZxid(), null, null);
        if ((newLeaderProposal.packet.getZxid() & 0xffffffffL) != 0) {
            LOG.error("NEWLEADER proposal has Zxid of " + newLeaderProposal.packet.getZxid());
        }
        outstandingProposals.add(newLeaderProposal);
        new Thread() {

            public void run() {
                try {
                    while (true) {
                        Socket s = ss.accept();
                        s.setSoTimeout(self.tickTime * self.syncLimit);
                        s.setTcpNoDelay(true);
                        new FollowerHandler(s, Leader.this);
                    }
                } catch (Exception e) {
                }
            }
        }.start();
        newLeaderProposal.ackCount++;
        while (newLeaderProposal.ackCount <= self.quorumPeers.size() / 2) {
            if (self.tick > self.initLimit) {
                shutdown("Waiting for " + (self.quorumPeers.size() / 2) + " followers, only synced with " + newLeaderProposal.ackCount);
                if (followers.size() >= self.quorumPeers.size() / 2) {
                    LOG.warn("Enough followers present. " + "Perhaps the initTicks need to be increased.");
                }
                return;
            }
            Thread.sleep(self.tickTime);
            self.tick++;
        }
        if (!System.getProperty("zookeeper.leaderServes", "yes").equals("no")) {
            self.cnxnFactory.setZooKeeperServer(zk);
        }
        synchronized (this) {
            notifyAll();
        }
        boolean tickSkip = true;
        while (true) {
            Thread.sleep(self.tickTime / 2);
            if (!tickSkip) {
                self.tick++;
            }
            int syncedCount = 0;
            synchronized (followers) {
                for (FollowerHandler f : followers) {
                    if (f.synced()) {
                        syncedCount++;
                    }
                    f.ping();
                }
            }
            if (!tickSkip && syncedCount < self.quorumPeers.size() / 2) {
                shutdown("Only " + syncedCount + " followers, need " + (self.quorumPeers.size() / 2));
                return;
            }
            tickSkip = !tickSkip;
        }
    }

    boolean isShutdown;

    /**
     * Close down all the FollowerHandlers
     */
    void shutdown(String reason) {
        if (isShutdown) {
            return;
        }
        LOG.error("FIXMSG", new Exception("shutdown Leader! reason: " + reason));
        self.cnxnFactory.setZooKeeperServer(null);
        self.cnxnFactory.clear();
        if (zk != null) {
            zk.shutdown();
        }
        try {
            ss.close();
        } catch (IOException e) {
            LOG.error("FIXMSG", e);
        }
        synchronized (followers) {
            for (Iterator<FollowerHandler> it = followers.iterator(); it.hasNext(); ) {
                FollowerHandler f = it.next();
                it.remove();
                f.shutdown();
            }
        }
        isShutdown = true;
    }

    /**
     * Keep a count of acks that are received by the leader for a particular
     * proposal
     * 
     * @param zxid
     *                the zxid of the proposal sent out
     * @param followerAddr
     */
    public synchronized void processAck(long zxid, SocketAddress followerAddr) {
        boolean first = true;
        if (outstandingProposals.size() == 0) {
            return;
        }
        if (outstandingProposals.peek().packet.getZxid() > zxid) {
            return;
        }
        for (Proposal p : outstandingProposals) {
            long packetZxid = p.packet.getZxid();
            if (packetZxid == zxid) {
                p.ackCount++;
                if (p.ackCount > self.quorumPeers.size() / 2) {
                    if (!first) {
                        LOG.error("Commiting " + Long.toHexString(zxid) + " from " + followerAddr + " not first!");
                        LOG.error("First is " + outstandingProposals.element().packet);
                        System.exit(13);
                    }
                    outstandingProposals.remove();
                    if (p.request != null) {
                        toBeApplied.add(p);
                    }
                    if ((zxid & 0xffffffffL) != 0) {
                        if (p.request == null) {
                            LOG.error("Going to commmit null: " + p);
                        }
                        commit(zxid);
                        zk.commitProcessor.commit(p.request);
                        if (pendingSyncs.containsKey(zxid)) {
                            sendSync(syncHandler.get(pendingSyncs.get(zxid).sessionId), pendingSyncs.get(zxid));
                            syncHandler.remove(pendingSyncs.get(zxid));
                            pendingSyncs.remove(zxid);
                        }
                    }
                }
                return;
            } else {
                first = false;
            }
        }
        LOG.error("Trying to commit future proposal: " + Long.toHexString(zxid) + " from " + followerAddr);
    }

    static class ToBeAppliedRequestProcessor implements RequestProcessor {

        private RequestProcessor next;

        private ConcurrentLinkedQueue<Proposal> toBeApplied;

        /**
         * This request processor simply maintains the toBeApplied list. For
         * this to work next must be a FinalRequestProcessor and
         * FinalRequestProcessor.processRequest MUST process the request
         * synchronously!
         * 
         * @param next
         *                a reference to the FinalRequestProcessor
         */
        ToBeAppliedRequestProcessor(RequestProcessor next, ConcurrentLinkedQueue<Proposal> toBeApplied) {
            if (!(next instanceof FinalRequestProcessor)) {
                throw new RuntimeException(ToBeAppliedRequestProcessor.class.getName() + " must be connected to " + FinalRequestProcessor.class.getName() + " not " + next.getClass().getName());
            }
            this.toBeApplied = toBeApplied;
            this.next = next;
        }

        public void processRequest(Request request) {
            next.processRequest(request);
            Proposal p = toBeApplied.peek();
            if (p != null && p.request != null && p.request.zxid == request.zxid) {
                toBeApplied.remove();
            }
        }

        public void shutdown() {
            next.shutdown();
        }
    }

    /**
     * send a packet to all the followers ready to follow
     * 
     * @param qp
     *                the packet to be sent
     */
    void sendPacket(QuorumPacket qp) {
        synchronized (forwardingFollowers) {
            for (FollowerHandler f : forwardingFollowers) {
                f.queuePacket(qp);
            }
        }
    }

    long lastCommitted;

    /**
     * Create a commit packet and send it to all the members of the quorum
     * 
     * @param zxid
     */
    public void commit(long zxid) {
        lastCommitted = zxid;
        QuorumPacket qp = new QuorumPacket(Leader.COMMIT, zxid, null, null);
        sendPacket(qp);
    }

    long lastProposed;

    /**
     * create a proposal and send it out to all the members
     * 
     * @param request
     * @return the proposal that is queued to send to all the members
     */
    public Proposal propose(Request request) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BinaryOutputArchive boa = BinaryOutputArchive.getArchive(baos);
        try {
            request.hdr.serialize(boa, "hdr");
            if (request.txn != null) {
                request.txn.serialize(boa, "txn");
            }
            baos.close();
        } catch (IOException e) {
            LOG.error("FIXMSG", e);
        }
        QuorumPacket pp = new QuorumPacket(Leader.PROPOSAL, request.zxid, baos.toByteArray(), null);
        Proposal p = new Proposal();
        p.packet = pp;
        p.request = request;
        synchronized (this) {
            outstandingProposals.add(p);
            lastProposed = p.packet.getZxid();
            sendPacket(pp);
        }
        return p;
    }

    /**
     * Process sync requests
     * 
     * @param QuorumPacket p
     * @return void
     */
    public void processSync(Request r) {
        if (outstandingProposals.isEmpty()) {
            LOG.warn("No outstanding proposal");
            sendSync(syncHandler.get(r.sessionId), r);
            syncHandler.remove(r.sessionId);
        } else {
            pendingSyncs.put(lastProposed, r);
        }
    }

    /**
     * Set FollowerHandler for sync.
     * 
     * @param QuorumPacket p
     * @return void
     */
    public synchronized void setSyncHandler(FollowerHandler f, long s) {
        syncHandler.put(s, f);
    }

    /**
     * Sends a sync message to the appropriate server
     * 
     * @param request
     * @return void
     */
    public void sendSync(FollowerHandler f, Request r) {
        if (f != null) {
            QuorumPacket qp = new QuorumPacket(Leader.SYNC, 0, null, null);
            f.queuePacket(qp);
        } else {
            LOG.warn("Committing sync: " + r.cxid);
            zk.commitProcessor.commit(r);
        }
    }

    /**
     * lets the leader know that a follower is capable of following and is done
     * syncing
     * 
     * @param handler
     *                handler of the follower
     * @return last proposed zxid
     */
    public synchronized long startForwarding(FollowerHandler handler, long lastSeenZxid) {
        if (lastProposed > lastSeenZxid) {
            for (Proposal p : toBeApplied) {
                if (p.packet.getZxid() <= lastSeenZxid) {
                    continue;
                }
                handler.queuePacket(p.packet);
                QuorumPacket qp = new QuorumPacket(Leader.COMMIT, p.packet.getZxid(), null, null);
                handler.queuePacket(qp);
            }
            for (Proposal p : outstandingProposals) {
                if (p.packet.getZxid() <= lastSeenZxid) {
                    continue;
                }
                handler.queuePacket(p.packet);
            }
        }
        synchronized (forwardingFollowers) {
            forwardingFollowers.add(handler);
            return lastProposed;
        }
    }
}
