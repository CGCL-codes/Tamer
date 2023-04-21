package org.apache.bailey.ddb.simple;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.bailey.Database;
import org.apache.bailey.Document;
import org.apache.bailey.Query;
import org.apache.bailey.Range;
import org.apache.bailey.Results;
import org.apache.bailey.ddb.ClientToMasterProtocol;
import org.apache.bailey.ddb.HostID;
import org.apache.bailey.ddb.HostProtocol;
import org.apache.bailey.ddb.Mapper;
import org.apache.bailey.ddb.NodeID;
import org.apache.bailey.ddb.RangeMismatchException;
import org.apache.bailey.ddb.RangeResults;
import org.apache.bailey.util.Pair;
import org.apache.bailey.util.SetValuedMap;

public class SimpleClient extends Database {

    private final ClientToMasterProtocol master;

    private final Object mapperLock;

    private Mapper mapper;

    public SimpleClient(ClientToMasterProtocol master) {
        this.master = master;
        this.mapperLock = new Object();
        this.mapper = this.master.getMapper();
    }

    protected Mapper obtainMapper() {
        synchronized (mapperLock) {
            return mapper;
        }
    }

    protected void updateMapper() {
        synchronized (mapperLock) {
            mapper = master.getMapper();
        }
    }

    public boolean addDoc(Document d) throws IOException {
        Mapper mapper = obtainMapper();
        try {
            List<NodeID> nodeList = mapper.getNodes(d.getPosition());
            for (NodeID nodeID : nodeList) {
                HostID hostID = mapper.getHost(nodeID);
                HostProtocol host = HostInstanceRegistry.getInstance(hostID);
                host.addDoc(nodeID, d);
            }
            return true;
        } catch (RangeMismatchException e) {
            updateMapper();
            if (obtainMapper().getVersion() > mapper.getVersion()) {
                return addDoc(d);
            } else {
                throw e;
            }
        }
    }

    public Document getDoc(String id) throws IOException {
        Mapper mapper = obtainMapper();
        try {
            int position = Document.getDefaultPosition(id);
            NodeID nodeID = mapper.getNode(position);
            HostID hostID = mapper.getHost(nodeID);
            HostProtocol host = HostInstanceRegistry.getInstance(hostID);
            return host.getDoc(nodeID, id);
        } catch (RangeMismatchException e) {
            updateMapper();
            if (obtainMapper().getVersion() > mapper.getVersion()) {
                return getDoc(id);
            } else {
                throw e;
            }
        }
    }

    public Document getDoc(String id, int position) throws IOException {
        Mapper mapper = obtainMapper();
        try {
            NodeID nodeID = mapper.getNode(position);
            HostID hostID = mapper.getHost(nodeID);
            HostProtocol host = HostInstanceRegistry.getInstance(hostID);
            return host.getDoc(nodeID, id, position);
        } catch (RangeMismatchException e) {
            updateMapper();
            if (obtainMapper().getVersion() > mapper.getVersion()) {
                return getDoc(id, position);
            } else {
                throw e;
            }
        }
    }

    public boolean removeDoc(Document d) throws IOException {
        Mapper mapper = obtainMapper();
        try {
            List<NodeID> nodeList = mapper.getNodes(d.getPosition());
            for (NodeID nodeID : nodeList) {
                HostID hostID = mapper.getHost(nodeID);
                HostProtocol host = HostInstanceRegistry.getInstance(hostID);
                host.removeDoc(nodeID, d);
            }
            return true;
        } catch (RangeMismatchException e) {
            updateMapper();
            if (obtainMapper().getVersion() > mapper.getVersion()) {
                return removeDoc(d);
            } else {
                throw e;
            }
        }
    }

    public Results search(Query q, int maxHits) throws IOException {
        return search(Range.ALL, q, maxHits);
    }

    public Results search(Range range, Query q, int maxHits) throws IOException {
        Mapper mapper = obtainMapper();
        try {
            long totalHitCount = 0;
            Collection<Document> totalHits = new ArrayList<Document>();
            SetValuedMap<HostID, Pair<NodeID, Range>> map = new SetValuedMap<HostID, Pair<NodeID, Range>>();
            List<Pair<NodeID, Range>> list = mapper.getCoverage(range);
            for (Pair<NodeID, Range> pair : list) {
                HostID hostID = mapper.getHost(pair.getKey());
                map.add(hostID, pair);
            }
            for (HostID hostID : map.keySet()) {
                Set<Pair<NodeID, Range>> nodeSet = map.get(hostID);
                NodeID[] nodes = new NodeID[nodeSet.size()];
                Range[] ranges = new Range[nodeSet.size()];
                int i = 0;
                for (Pair<NodeID, Range> pair : nodeSet) {
                    nodes[i] = pair.getKey();
                    ranges[i] = pair.getValue();
                    i++;
                }
                HostProtocol host = HostInstanceRegistry.getInstance(hostID);
                RangeResults[] rangeResults = host.search(nodes, ranges, q, maxHits);
                assert (rangeResults.length == ranges.length);
                for (i = 0; i < rangeResults.length; i++) {
                    if (!rangeResults[i].getRange().equals(ranges[i])) {
                        throw new RangeMismatchException(nodes[i], ranges[i], rangeResults[i].getRange());
                    }
                    totalHitCount += rangeResults[i].getHitCount();
                    totalHits.addAll(rangeResults[i].getHits());
                }
            }
            return new Results(totalHitCount, totalHits);
        } catch (RangeMismatchException e) {
            updateMapper();
            if (obtainMapper().getVersion() > mapper.getVersion()) {
                return search(range, q, maxHits);
            } else {
                throw e;
            }
        }
    }
}
