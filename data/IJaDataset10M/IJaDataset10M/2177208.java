package org.apache.bailey.ddb;

import java.io.IOException;
import org.apache.bailey.Document;
import org.apache.bailey.Query;
import org.apache.bailey.Range;
import org.apache.bailey.util.Pair;

/**
 * A host manages a number of nodes.
 */
public class Host implements HostProtocol {

    public boolean addDoc(NodeID nodeID, Document d) throws IOException {
        return false;
    }

    public boolean removeDoc(NodeID nodeID, Document d) throws IOException {
        return false;
    }

    public boolean isNewer(NodeID nodeID, Document d) throws IOException {
        return false;
    }

    public boolean[] isNewer(NodeID nodeID, Document[] ds) throws IOException {
        return null;
    }

    public Document getDoc(NodeID nodeID, String id) throws IOException {
        return null;
    }

    public Document getDoc(NodeID nodeID, String id, int position) throws IOException {
        return null;
    }

    public Document[] getDocs(NodeID nodeID, String[] ids) throws IOException {
        return null;
    }

    public Document[] getDocs(NodeID nodeID, String[] ids, int[] positions) throws IOException {
        return null;
    }

    public Document[] getDocs(NodeID nodeID, Range range) throws IOException {
        return null;
    }

    public RangeResults[] search(NodeID[] nodeIDs, Range[] ranges, Query q, int maxHits) throws IOException {
        return null;
    }

    public RangeResults search(NodeID nodeID, Range range, Query q, int maxHits) throws IOException {
        return null;
    }

    public Pair<Long, LogEntry[]> getEntries(NodeID nodeID, Range range, long startEntry, int maxEntries) throws IOException {
        return null;
    }

    public NodeState getNodeState(NodeID nodeID) throws IOException {
        return null;
    }

    public NodeState[] getNeighborStates(NodeID nodeID, Range range) throws IOException {
        return null;
    }

    public long getOldestMapperVersion() {
        return -1;
    }
}
