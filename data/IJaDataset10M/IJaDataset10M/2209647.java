package analysis.dataCompression;

import analysis.BinnedPeakList;
import analysis.DistanceMetric;
import analysis.Normalizer;
import junit.framework.TestCase;
import java.util.ArrayList;

public class CFNodeTest extends TestCase {

    private DistanceMetric dMetric;

    private BinnedPeakList bp1, bp2, bp3;

    private ClusterFeature parentCF, childCF, orphanCF, farOffCF;

    private CFNode parentNode, childNode, orphanNode, farOffNode;

    protected void setUp() throws Exception {
        super.setUp();
        dMetric = DistanceMetric.EUCLIDEAN_SQUARED;
        parentNode = new CFNode(null, dMetric);
        parentCF = new ClusterFeature(parentNode, dMetric);
        parentNode.addCF(parentCF);
        childNode = new CFNode(parentCF, dMetric);
        parentCF.updatePointers(childNode, parentNode);
        childCF = new ClusterFeature(childNode, dMetric);
        childNode.addCF(childCF);
        bp1 = new BinnedPeakList(new Normalizer());
        bp1.add(-210, (float) 1);
        bp1.add(-160, (float) 1);
        bp1.add(-100, (float) 1);
        bp1.add(-30, (float) 1);
        bp1.add(20, (float) 2);
        bp1.add(90, (float) 2);
        bp1.add(120, (float) 2);
        bp1.posNegNormalize(dMetric);
        childCF.updateCF(bp1, 1, true);
        orphanNode = new CFNode(null, dMetric);
        orphanCF = new ClusterFeature(orphanNode, dMetric);
        orphanNode.addCF(orphanCF);
        bp2 = new BinnedPeakList(new Normalizer());
        bp2.add(-210, (float) 1);
        bp2.add(-160, (float) 1);
        bp2.add(-100, (float) 1);
        bp2.add(-30, (float) 1);
        bp2.posNegNormalize(dMetric);
        orphanCF.updateCF(bp2, 2, true);
        farOffNode = new CFNode(null, dMetric);
        farOffCF = new ClusterFeature(farOffNode, dMetric);
        bp3 = new BinnedPeakList(new Normalizer());
        bp3.add(500, 1);
        bp3.add(501, 1);
        bp3.add(502, 1);
        bp3.posNegNormalize(dMetric);
        farOffCF.updateCF(bp3, 3, true);
    }

    public void testSameContents() {
        assertFalse(childNode.sameContents(orphanNode));
        assertFalse(orphanNode.sameContents(childNode));
        orphanCF.getSums().printPeakList();
        orphanCF.setSums(bp1);
        orphanCF.getSums().printPeakList();
        childCF.getSums().printPeakList();
        assertTrue(childNode.sameContents(orphanNode));
        assertTrue(orphanNode.sameContents(childNode));
    }

    public void testAddCF() {
        ArrayList<ClusterFeature> array = childNode.getCFs();
        assertEquals(array.size(), 1);
        assertEquals(array.get(0), childCF);
        childNode.addCF(childCF);
        assertEquals(array.size(), 2);
        childNode.addCF(orphanCF);
        assertEquals(array.size(), 3);
        assertEquals(array.get(2), orphanCF);
        assertEquals(parentCF.getCount(), 0);
    }

    public void testRemoveCF() {
        ArrayList<ClusterFeature> array = childNode.getCFs();
        assertEquals(array.size(), 1);
        childNode.removeCF(childCF);
        assertEquals(array.size(), 0);
    }

    public void testGetTwoClosest() {
        assertNull(orphanNode.getTwoClosest());
        childNode.addCF(orphanCF);
        childNode.addCF(farOffCF);
        Pair<ClusterFeature[], Float> pair = childNode.getTwoClosest();
        ClusterFeature[] array = pair.first;
        assert (array[0] == orphanCF || array[0] == childCF);
        assert (array[1] == orphanCF || array[1] == childCF);
    }

    public void testGetTwoFarthest() {
        assertNull(orphanNode.getTwoFarthest());
        childNode.addCF(orphanCF);
        childNode.addCF(farOffCF);
        ClusterFeature[] array = childNode.getTwoFarthest();
        assertTrue(array[0] == childCF || array[0] == farOffCF);
        assertTrue(array[1] == childCF || array[1] == farOffCF);
    }

    public void testGetClosest() {
        assertEquals(childNode.getClosest(childCF.getSums()).first, childCF);
        assertEquals(childNode.getClosest(farOffCF.getSums()).first, childCF);
        assertEquals(childNode.getClosest(orphanCF.getSums()).first, childCF);
        childNode.addCF(orphanCF);
        assertEquals(childNode.getClosest(childCF.getSums()).first, childCF);
        assertEquals(childNode.getClosest(farOffCF.getSums()).first, childCF);
        assertEquals(childNode.getClosest(orphanCF.getSums()).first, orphanCF);
        childNode.addCF(farOffCF);
        assertEquals(childNode.getClosest(childCF.getSums()).first, childCF);
        assertEquals(childNode.getClosest(farOffCF.getSums()).first, farOffCF);
        assertEquals(childNode.getClosest(orphanCF.getSums()).first, orphanCF);
    }

    public void testUpdateParent() {
        childNode.updateParent(orphanCF);
        assertEquals(childCF.curNode, childNode);
        assertEquals(childCF.curNode.parentCF, orphanCF);
        assertEquals(childCF.curNode.parentNode, orphanNode);
        assertEquals(childNode.parentCF, orphanCF);
        assertEquals(childNode.parentNode, orphanNode);
        assertEquals(orphanCF.child, childNode);
        assertEquals(orphanCF.curNode, orphanNode);
    }

    public void testIsLeaf() {
        assertTrue(childNode.isLeaf());
        assertTrue(orphanNode.isLeaf());
        assertTrue(farOffNode.isLeaf());
        assertFalse(parentNode.isLeaf());
    }
}
