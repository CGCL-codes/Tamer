package symore.conflictsolving;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import symore.events.Event;
import symore.sentinel.ISentinelManager;
import symore.sentinel.SentinelNode;

/**
 * This conflict solving algorithm extends the PriorityConflictSolving with 
 * support for commutative transactions.
 * 
 * Idea: 
 * 		- solve conflicts with the default priority conflict solving algorithm
 * 		- after this we merge the tree, so that we check if some deactivated transaction
 * 		  can be attached to the activated transactions 
 * 
 * 
 * @author Sebastian Martens, Manuel Scholz
 */
public class PriorityCommutativeConflictSolvingAlgorithm extends CommutativeConflictSolving {

    /**
	 * Returns the list of sentinel nodes in the order, they should be observed.
	 */
    public ArrayList getSentinelNodeList(ISentinelManager oSentinelManager, Event from, Event to, boolean bWithReadSet) {
        SortedSet sentinelSet = oSentinelManager.getSentinelNodesInTimestampOrder().subSet(from, to);
        ArrayList nodeList = new ArrayList(sentinelSet.size());
        nodeList.addAll(sentinelSet);
        Collections.sort(nodeList, new Comparator() {

            /**
			 * Compare two SentinelNodes
			 * The node with the smaller priority is smaller than the node with the bigger priority
			 */
            public int compare(Object o1, Object o2) {
                if (!(o1 instanceof SentinelNode) || !(o2 instanceof SentinelNode)) throw new ClassCastException();
                SentinelNode s1 = (SentinelNode) o1;
                SentinelNode s2 = (SentinelNode) o2;
                if (s1.getPriority() == s2.getPriority()) return s1.compareTo(s2); else return (s1.getPriority() - s2.getPriority());
            }
        });
        return nodeList;
    }
}
