package org.apache.bailey.ddb;

import java.io.IOException;
import org.apache.bailey.Document;
import org.apache.bailey.Query;
import org.apache.bailey.Range;

/**
 * This protocol mirrors Database functionalities. However, it may need more
 * parameters. Therefore, we make it a different interface from Database.
 */
public interface ClientToHostProtocol {

    boolean addDoc(NodeID nodeID, Document d) throws IOException;

    boolean removeDoc(NodeID nodeID, Document d) throws IOException;

    boolean isNewer(NodeID nodeID, Document d) throws IOException;

    boolean[] isNewer(NodeID nodeID, Document[] ds) throws IOException;

    Document getDoc(NodeID nodeID, String id) throws IOException;

    Document getDoc(NodeID nodeID, String id, int position) throws IOException;

    /**
   * For each node n in nodeIDs and the corresponding range r in ranges,
   * execute query q on node n with range r. The range r should be a subset of
   * the range that node n serves. In most case, the range r is the same as the
   * range that node n serves. If range r is not a subset of the range that
   * node n serves, execute the query on the overlap range and return the
   * results with the overlap range. Return an array of results, one for each
   * node.
   * 
   * @param nodeIDs
   * @param ranges
   * @param q
   * @param maxHits
   * @return for each node the set of documents that match the query on a range
   * @throws IOException
   */
    RangeResults[] search(NodeID[] nodeIDs, Range[] ranges, Query q, int maxHits) throws IOException;

    RangeResults search(NodeID nodeID, Range range, Query q, int maxHits) throws IOException;
}
