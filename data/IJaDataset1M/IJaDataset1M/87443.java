package net.sourceforge.combean.test.graph.alg.spath;

import java.util.Iterator;
import net.sourceforge.combean.interfaces.graph.alg.spath.SingleSourceShortestPathAlg;
import net.sourceforge.combean.interfaces.graph.containers.FixedNodeMap;
import net.sourceforge.combean.interfaces.graph.containers.FixedPath;

/**
 * @author schickin
 *
 */
public class AbstractGraphWithShortestPathFixture extends AbstractGraphWithEdgeWeightsFixture {

    private int[] predNum = null;

    private int[] distanceToNode = null;

    private int[] spath = null;

    /**
     * constructor
     */
    public AbstractGraphWithShortestPathFixture(int[][] edgesWithWeights, int[] predNum, int[] distanceToNode, int[] spath) {
        super(predNum.length, edgesWithWeights);
        this.predNum = predNum;
        this.distanceToNode = distanceToNode;
        this.spath = spath;
    }

    public final void checkDistanceAndPred(SingleSourceShortestPathAlg<Object, Object, Double> alg) {
        runShortestPathAlg(alg);
        FixedNodeMap<Object, Object> predMap = alg.getPredecessorMap();
        for (int i = 0; i < this.globNodes.getNumNodes(); i++) {
            Object currNode = this.globNodes.getNode(i);
            Number distance = alg.getDistanceTo(currNode);
            assertEquals("checking distance to node number " + i, this.distanceToNode[i], distance.doubleValue(), 0);
            Object predEdge = predMap.get(currNode);
            if (i == 0) {
                assertNull("the start node does not have a predecessor edge", predEdge);
                continue;
            }
            assertNotNull("node " + i + " must have a predecessor edge", predEdge);
            Object predNode = this.neigh.getOtherNode(predEdge, currNode);
            assertEquals("predecessor of " + i + " shall be " + this.predNum[i], this.predNum[i], this.globNodes.getNodeNumber(predNode));
        }
    }

    public final void checkShortestPathFromZeroToSix(SingleSourceShortestPathAlg<Object, Object, Double> alg) {
        runShortestPathAlg(alg);
        FixedPath<Object, Object> p = alg.getShortestPathTo(this.targetNode);
        assertEquals(this.spath.length, p.getNumNodes());
        assertEquals(this.startNode, p.getFirstNode());
        assertEquals(this.targetNode, p.getLastNode());
        Iterator<Object> itPath = p.getNodeIterator();
        int nodeCount = 0;
        while (itPath.hasNext()) {
            Object currNode = itPath.next();
            assertEquals("check of node number " + nodeCount + " in the path is correct", this.spath[nodeCount], this.globNodes.getNodeNumber(currNode));
            nodeCount++;
        }
    }
}
