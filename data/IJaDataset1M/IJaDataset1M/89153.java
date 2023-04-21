package drcl.net.graph;

import java.util.List;
import java.util.Iterator;
import drcl.util.queue.*;

/**
This class implements the Dijkstra algorithm and
calculates the minimum shortest path tree for a graph.
*/
public class ShortestPathTree {

    protected boolean debug = false;

    double[] cost;

    public void setDebugEnabled(boolean enabled_) {
        debug = enabled_;
    }

    public boolean isDebugEnabled() {
        return debug;
    }

    /**
   * Runs the Dijkstra algorithm.  
   * The implementation excludes "marked" links
   * (see {@link Link.setMarked(boolean)}). 
   * @param sourceID_ index of the source node, in the node array. 
   * @param destIDs_ indices of the destination node, in the node array. 
   * @param nodes_ the node array. 
   * @param trim_ set to trim the branches that do not contain destinations.
   * @return three integer arrays.
   *     1st array: parent indices of nodes.  An index can be -1
   *     if the node has no parent (i.e., the node is either the source or
   *     is not intree).
   *     2nd array: parent node interface Id.
   *     3rd array:  interface Id of interface to the parent node.
   */
    public int[][] run(int sourceID_, int[] destIDs_, Node[] nodes_, boolean trim_) {
        int nnodes = nodes_.length;
        int[] originalIDs_ = new int[nnodes];
        for (int i = 0; i < nnodes; i++) {
            originalIDs_[i] = nodes_[i].getID();
            nodes_[i].setID(i);
        }
        int[][] neighborIndices_ = new int[nnodes][];
        for (int i = 0; i < nnodes; i++) {
            Node[] neighbors_ = nodes_[i].neighbors();
            if (neighbors_ == null) neighborIndices_[i] = new int[0]; else {
                int[] tmp_ = new int[neighbors_.length];
                for (int j = 0; j < neighbors_.length; j++) tmp_[j] = neighbors_[j].getID();
                neighborIndices_[i] = tmp_;
            }
        }
        cost = new double[nnodes];
        int[] parentIndices = new int[nnodes];
        int[] parentIfs = new int[nnodes];
        int[] ifs = new int[nnodes];
        boolean[] intree = new boolean[nnodes];
        for (int i = 0; i < nnodes; i++) {
            parentIndices[i] = parentIfs[i] = ifs[i] = -1;
            cost[i] = Double.POSITIVE_INFINITY;
        }
        cost[sourceID_] = 0.0;
        boolean[] destArray_ = new boolean[nnodes];
        for (int i = 0; i < destIDs_.length; i++) destArray_[destIDs_[i]] = true;
        int ndests_ = destIDs_.length;
        Queue candidates_ = QueueAssistant.getBest();
        candidates_.enqueue(0.0, nodes_[sourceID_]);
        while (!candidates_.isEmpty()) {
            Node minCostNode_ = (Node) candidates_.dequeue();
            if (debug) System.out.println("check " + minCostNode_.getID());
            int from_ = minCostNode_.getID();
            if (intree[from_]) continue;
            intree[from_] = true;
            if (destArray_[from_]) {
                destArray_[from_] = false;
                ndests_--;
                if (ndests_ == 0) break;
            }
            List links_ = minCostNode_.linksInList();
            for (Iterator it = links_.iterator(); it.hasNext(); ) {
                Link l = (Link) it.next();
                if (l.isMarked()) continue;
                Node neighbor_ = l.neighbor(minCostNode_);
                int to_ = neighbor_.getID();
                if (intree[to_]) continue;
                int fromIf_ = l.getInterfaceId(minCostNode_);
                double lcost_ = l.getCost();
                if (cost[to_] > cost[from_] + lcost_) {
                    cost[to_] = cost[from_] + lcost_;
                    parentIndices[to_] = from_;
                    parentIfs[to_] = fromIf_;
                    ifs[to_] = l.getInterfaceId(neighbor_);
                    candidates_.enqueue(cost[to_], neighbor_);
                }
            }
        }
        if (trim_) {
            for (int i = 0; i < nnodes; i++) intree[i] = false;
            for (int i = 0; i < destIDs_.length; i++) {
                int index_ = destIDs_[i];
                while (!intree[index_]) {
                    intree[index_] = true;
                    if (index_ == sourceID_) break; else if (parentIndices[index_] < 0) {
                        System.err.println("-- warning -- cannot find path to " + nodes_[index_]);
                        break;
                    }
                    index_ = parentIndices[index_];
                }
            }
            for (int i = 0; i < nnodes; i++) if (!intree[i]) parentIndices[i] = parentIfs[i] = ifs[i] = -1;
        }
        for (int i = 0; i < nnodes; i++) nodes_[i].setID(originalIDs_[i]);
        originalIDs_ = null;
        destArray_ = null;
        intree = null;
        neighborIndices_ = null;
        candidates_ = null;
        return new int[][] { parentIndices, parentIfs, ifs };
    }

    /** Returns the result cost from source from the previous run.
   * The indices match the node array in the previous run. */
    public double[] getCost() {
        return cost;
    }
}
