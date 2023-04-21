package algs.model.tests.network;

import java.util.ArrayList;
import org.junit.Test;
import algs.model.network.BFS_SearchArray;
import algs.model.network.EdgeInfo;
import algs.model.network.FlowNetworkArray;
import algs.model.network.FordFulkerson;
import algs.model.network.ShortestPathArray;
import junit.framework.TestCase;

/**
 * http://www.me.utexas.edu/~jensen/ORMM/models/unit/network/subunits/distribution/index.html
 * 
 * @author George
 *
 */
public class FinalCaseTest extends TestCase {

    @Test
    public void testDistributionExample() {
        ArrayList<EdgeInfo> edges = new ArrayList<EdgeInfo>();
        edges.add(new EdgeInfo(0, 1, 700));
        edges.add(new EdgeInfo(0, 7, 200));
        edges.add(new EdgeInfo(0, 8, 200));
        edges.add(new EdgeInfo(2, 9, 200));
        edges.add(new EdgeInfo(3, 9, 200));
        edges.add(new EdgeInfo(6, 9, 250));
        edges.add(new EdgeInfo(4, 9, 300));
        edges.add(new EdgeInfo(5, 9, 150));
        edges.add(new EdgeInfo(1, 2, 200, 6));
        edges.add(new EdgeInfo(1, 3, 200, 3));
        edges.add(new EdgeInfo(1, 4, 200, 3));
        edges.add(new EdgeInfo(1, 5, 200, 7));
        edges.add(new EdgeInfo(7, 3, 200, 7));
        edges.add(new EdgeInfo(7, 4, 200, 2));
        edges.add(new EdgeInfo(7, 5, 200, 5));
        edges.add(new EdgeInfo(8, 4, 200, 6));
        edges.add(new EdgeInfo(8, 5, 200, 4));
        edges.add(new EdgeInfo(8, 6, 200, 7));
        edges.add(new EdgeInfo(4, 3, 200, 5));
        edges.add(new EdgeInfo(4, 2, 200, 4));
        edges.add(new EdgeInfo(4, 6, 200, 6));
        edges.add(new EdgeInfo(5, 6, 200, 5));
        edges.add(new EdgeInfo(5, 2, 200, 4));
        edges.add(new EdgeInfo(4, 5, 200, 2));
        edges.add(new EdgeInfo(5, 4, 200, 2));
        FlowNetworkArray network = new FlowNetworkArray(12, 0, 9, edges.iterator());
        FordFulkerson ff = new FordFulkerson(network, new BFS_SearchArray(network));
        ff.compute();
        assertEquals(5350, network.getCost());
        assertEquals(1100, network.getFlow());
        network = new FlowNetworkArray(12, 0, 9, edges.iterator());
        ff = new FordFulkerson(network, new ShortestPathArray(network));
        ff.compute();
        assertEquals(5300, network.getCost());
        assertEquals(1100, network.getFlow());
        System.out.println(network);
    }
}
