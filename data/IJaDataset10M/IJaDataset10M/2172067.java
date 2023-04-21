package org.hydracache.server.harmony.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import org.hydracache.data.hashing.HashFunction;
import org.hydracache.data.hashing.NativeHashFunction;
import org.hydracache.server.Identity;
import org.hydracache.server.harmony.jgroups.JGroupsNode;
import org.jgroups.stack.IpAddress;
import org.junit.Before;
import org.junit.Test;

/**
 * @author nzhu
 * 
 */
public class NodePartitionSubstanceTest {

    private JGroupsNode nodeA;

    private JGroupsNode nodeB;

    private JGroupsNode nodeC;

    private HashFunction hashFunction = new NativeHashFunction();

    private NodeSet allNodes;

    @Before
    public void setup() throws Exception {
        nodeA = new JGroupsNode(new Identity(80), new IpAddress(800));
        nodeB = new JGroupsNode(new Identity(81), new IpAddress(810));
        nodeC = new JGroupsNode(new Identity(82), new IpAddress(820));
        allNodes = new NodeSet(Arrays.asList(nodeA, nodeB, nodeC));
    }

    @Test
    public void ensureSubstanceGeneratesCorrectSize() {
        int substanceSize = 1;
        Substance substance = new NodePartitionSubstance(nodeA, allNodes, hashFunction, substanceSize);
        assertEquals("Substance owner is incorrect", nodeA, substance.getOwner());
        assertEquals("Substance size is incorrect", substanceSize, substance.getSize());
    }

    @Test
    public void ensureSubstanceNeighbourhoodCalculationIsCorrect() {
        checkNeighbourCalculation(1, nodeA);
        checkNeighbourCalculation(1, nodeB);
        checkNeighbourCalculation(1, nodeC);
        checkNeighbourCalculation(2, nodeA);
        checkNeighbourCalculation(2, nodeB);
        checkNeighbourCalculation(2, nodeC);
    }

    private void checkNeighbourCalculation(int substanceSize, JGroupsNode owner) {
        Substance substance = new NodePartitionSubstance(owner, allNodes, hashFunction, substanceSize);
        NodeSet neighbours = substance.getNeighbours();
        assertNotNull("Neighbour set is null", neighbours);
        assertEquals("Neighbour set size is incorrect", substanceSize, neighbours.size());
        assertFalse("Neighbourhood should not contain the owner", neighbours.contains(owner));
    }

    @Test
    public void ensureOverlyLargeSubstanceSizeIsHandledImplicitly() {
        checkNLargeSubstanceSizeHandling(3, nodeA);
        checkNLargeSubstanceSizeHandling(3, nodeB);
        checkNLargeSubstanceSizeHandling(3, nodeC);
    }

    private void checkNLargeSubstanceSizeHandling(int substanceSize, JGroupsNode owner) {
        Substance substance = new NodePartitionSubstance(owner, allNodes, hashFunction, substanceSize);
        NodeSet neighbours = substance.getNeighbours();
        assertEquals("Neighbour set size is incorrect", 2, neighbours.size());
        assertFalse("Neighbourhood should not contain the owner", neighbours.contains(owner));
    }

    @Test
    public void ensureNeighborDetectionIsCorrect() {
        Substance substance = new NodePartitionSubstance(nodeA, allNodes, hashFunction, 1);
        assertTrue("Should be neighbor", substance.isNeighbor(nodeB.getId()));
        assertFalse("Should not be neighbor", substance.isNeighbor(nodeC.getId()));
    }
}
