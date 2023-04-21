package net.redlightning.dht.kad.tasks;

import java.util.*;
import net.redlightning.dht.kad.*;
import net.redlightning.dht.kad.DHT.DHTtype;
import net.redlightning.dht.kad.messages.*;
import net.redlightning.dht.kad.messages.MessageBase.Method;
import net.redlightning.dht.kad.utils.AddressUtils;
import net.redlightning.dht.kad.utils.PackUtil;
import android.util.Log;

/**
 * @author Damokles
 */
public class PeerLookupTask extends Task {

    private static final String TAG = PeerLookupTask.class.getSimpleName();

    private boolean scrapeOnly;

    private boolean noSeeds;

    private boolean fastLookup;

    private List<KBucketEntryAndToken> announceCanidates;

    private ScrapeResponseHandler scrapeHandler;

    private Set<PeerAddressDBItem> returnedItems;

    private SortedSet<KBucketEntryAndToken> closestSet;

    private int validReponsesSinceLastClosestSetModification;

    AnnounceNodeCache cache;

    public PeerLookupTask(RPCServerBase rpc, Node node, Key info_hash) {
        super(info_hash, rpc, node);
        announceCanidates = new ArrayList<KBucketEntryAndToken>(20);
        returnedItems = new HashSet<PeerAddressDBItem>();
        this.closestSet = new TreeSet<KBucketEntryAndToken>(new KBucketEntry.DistanceOrder(targetKey));
        cache = rpc.getDHT().getCache();
        cache.register(targetKey);
        Log.d(TAG, "PeerLookupTask started: " + getTaskID());
    }

    public void setScrapeHandler(ScrapeResponseHandler scrapeHandler) {
        this.scrapeHandler = scrapeHandler;
    }

    public void setNoSeeds(boolean avoidSeeds) {
        noSeeds = avoidSeeds;
    }

    public void setFastLookup(boolean isFastLookup) {
        if (!isQueued()) throw new IllegalStateException("cannot change lookup mode after startup");
        fastLookup = isFastLookup;
    }

    public void setScrapeOnly(boolean scrapeOnly) {
        this.scrapeOnly = scrapeOnly;
    }

    public boolean isScrapeOnly() {
        return scrapeOnly;
    }

    @Override
    void callFinished(RPCCallBase c, MessageBase rsp) {
        if (c.getMessageMethod() != Method.GET_PEERS) {
            return;
        }
        GetPeersResponse gpr;
        if (rsp instanceof GetPeersResponse) {
            gpr = (GetPeersResponse) rsp;
        } else {
            return;
        }
        for (DHTtype type : DHTtype.values()) {
            byte[] nodes = gpr.getNodes(type);
            if (nodes == null) continue;
            int nval = nodes.length / type.NODES_ENTRY_LENGTH;
            if (type == rpc.getDHT().getType()) {
                synchronized (todo) {
                    for (int i = 0; i < nval; i++) {
                        KBucketEntry e = PackUtil.UnpackBucketEntry(nodes, i * type.NODES_ENTRY_LENGTH, type);
                        if (!AddressUtils.isBogon(e.getAddress()) && !node.allLocalIDs().contains(e.getID()) && !visited.contains(e)) todo.add(e);
                    }
                }
            } else {
                for (int i = 0; i < nval; i++) {
                    KBucketEntry e = PackUtil.UnpackBucketEntry(nodes, i * type.NODES_ENTRY_LENGTH, type);
                    DHT.getDHT(type).addDHTNode(e.getAddress().getAddress().getHostAddress(), e.getAddress().getPort());
                }
            }
        }
        List<DBItem> items = gpr.getPeerItems();
        for (DBItem item : items) {
            if (!(item instanceof PeerAddressDBItem)) continue;
            PeerAddressDBItem it = (PeerAddressDBItem) item;
            if (!AddressUtils.isBogon(it)) returnedItems.add(it);
        }
        KBucketEntry entry = new KBucketEntry(rsp.getOrigin(), rsp.getID());
        cache.add(entry);
        KBucketEntryAndToken toAdd = new KBucketEntryAndToken(entry, gpr.getToken());
        if (!items.isEmpty()) {
            if (scrapeHandler != null) scrapeHandler.addGetPeersRespone(gpr);
        }
        if (gpr.getToken() != null) {
            synchronized (announceCanidates) {
                announceCanidates.add(toAdd);
            }
        }
        if (scrapeOnly || gpr.getToken() != null) {
            synchronized (closestSet) {
                closestSet.add(toAdd);
                if (closestSet.size() > DHTConstants.MAX_ENTRIES_PER_BUCKET) {
                    KBucketEntryAndToken last = closestSet.last();
                    closestSet.remove(last);
                    if (toAdd == last) {
                        validReponsesSinceLastClosestSetModification++;
                    } else {
                        validReponsesSinceLastClosestSetModification = 0;
                    }
                }
            }
        }
    }

    @Override
    void callTimeout(RPCCallBase c) {
        cache.removeEntry(c.getExpectedID());
    }

    @Override
    boolean canDoRequest() {
        if (scrapeOnly) return getNumOutstandingRequestsExcludingStalled() < DHTConstants.MAX_CONCURRENT_REQUESTS_LOWPRIO;
        return super.canDoRequest();
    }

    void update() {
        synchronized (todo) {
            todo.addAll(cache.get(targetKey, 3, visited));
            while (!todo.isEmpty() && canDoRequest() && validReponsesSinceLastClosestSetModification < DHTConstants.MAX_CONCURRENT_REQUESTS) {
                KBucketEntry e = todo.first();
                todo.remove(e);
                if (!visited.contains(e)) {
                    GetPeersRequest gpr = new GetPeersRequest(targetKey);
                    gpr.setWant4(rpc.getDHT().getType() == DHTtype.IPV4_DHT || DHT.getDHT(DHTtype.IPV4_DHT).getNode().getNumEntriesInRoutingTable() < DHTConstants.BOOTSTRAP_IF_LESS_THAN_X_PEERS);
                    gpr.setWant6(rpc.getDHT().getType() == DHTtype.IPV6_DHT || DHT.getDHT(DHTtype.IPV6_DHT).getNode().getNumEntriesInRoutingTable() < DHTConstants.BOOTSTRAP_IF_LESS_THAN_X_PEERS);
                    gpr.setDestination(e.getAddress());
                    gpr.setScrape(true);
                    gpr.setNoSeeds(noSeeds);
                    rpcCall(gpr, e.getID());
                    visited.add(e);
                }
            }
        }
        int waitingFor = fastLookup ? getNumOutstandingRequestsExcludingStalled() : getNumOutstandingRequests();
        if (todo.isEmpty() && waitingFor == 0 && !isFinished()) {
            done();
        } else if (waitingFor == 0 && validReponsesSinceLastClosestSetModification >= DHTConstants.MAX_CONCURRENT_REQUESTS) {
            done();
        }
    }

    @Override
    protected void done() {
        super.done();
        if (validReponsesSinceLastClosestSetModification >= DHTConstants.MAX_CONCURRENT_REQUESTS) synchronized (closestSet) {
            SortedSet<Key> toEstimate = new TreeSet<Key>();
            for (KBucketEntryAndToken e : closestSet) toEstimate.add(e.getID());
            rpc.getDHT().getEstimator().update(toEstimate);
        }
    }

    public List<KBucketEntryAndToken> getAnnounceCanidates() {
        if (fastLookup) throw new IllegalStateException("cannot use fast lookups for announces");
        return announceCanidates;
    }

    /**
	 * @return the returned_items
	 */
    public Set<PeerAddressDBItem> getReturnedItems() {
        return Collections.unmodifiableSet(returnedItems);
    }

    /**
	 * @return the info_hash
	 */
    public Key getInfoHash() {
        return targetKey;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void start() {
        KClosestNodesSearch kns = new KClosestNodesSearch(targetKey, DHTConstants.MAX_ENTRIES_PER_BUCKET * 4, rpc.getDHT());
        kns.fill();
        todo.addAll(kns.getEntries());
        cache.register(targetKey);
        todo.addAll(cache.get(targetKey, DHTConstants.MAX_CONCURRENT_REQUESTS * 2, Collections.EMPTY_SET));
        super.start();
    }
}
