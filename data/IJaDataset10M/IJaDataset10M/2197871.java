package mapAlgorithms;

import junit.framework.Assert;
import org.junit.Test;
import edu.kit.cm.kitcampusguide.mapAlgorithms.Dijkstra;
import edu.kit.cm.kitcampusguide.mapAlgorithms.RouteCalculator;
import edu.kit.cm.kitcampusguide.model.Graph;
import edu.kit.cm.kitcampusguide.model.Point;
import edu.kit.cm.kitcampusguide.model.Route;

/**
 * 
 * @author Monica
 * 
 *         This class tests the object Dijkstra, which is able to calculate a
 *         short path between 2 points.
 * 
 */
public class DijkstraTest {

    /**
	 * This method tests if the method getSingleton creates a new Dijkstra
	 * object if it isn't created and leaves the old object if an object was
	 * already created.
	 */
    @Test
    public void createSingletonTest() {
        RouteCalculator dijkstra = Dijkstra.getSingleton();
        Assert.assertNotNull(dijkstra);
        RouteCalculator newDijkstra = Dijkstra.getSingleton();
        Assert.assertNotNull(dijkstra);
        Assert.assertEquals(dijkstra, newDijkstra);
    }

    /**
	 * This tests if the method calculateRoute is able to calculate a simple
	 * route between two points.
	 */
    @Test
    public void routeCalculatingTest() {
        Graph mapGraph = new Graph();
        Point[] nodes = { new Point(0.0, 0.0), new Point(1.0, 0.0), new Point(0.0, 1.0), new Point(1.0, 1.0) };
        mapGraph.addNode(nodes[0]);
        mapGraph.addNode(nodes[1]);
        mapGraph.addNode(nodes[2]);
        mapGraph.addNode(nodes[3]);
        mapGraph.addEdge(0, 1, 2);
        mapGraph.addEdge(1, 0, 3);
        mapGraph.addEdge(1, 2, 5);
        mapGraph.addEdge(2, 1, 5);
        mapGraph.addEdge(2, 3, 3);
        mapGraph.addEdge(3, 2, 3);
        mapGraph.addEdge(0, 2, 5);
        mapGraph.addEdge(2, 0, 5);
        RouteCalculator dijkstra = Dijkstra.getSingleton();
        Route route = dijkstra.calculateRoute(nodes[0], nodes[3], mapGraph);
        Assert.assertNotNull(route);
        Assert.assertNotNull(route.getRoute());
        Assert.assertEquals(3, route.getRoute().size());
        Assert.assertEquals(nodes[0], route.getRoute().get(0));
        Assert.assertEquals(nodes[2], route.getRoute().get(1));
        Assert.assertEquals(nodes[3], route.getRoute().get(2));
    }

    /**
	 * This tests if by setting null as parameters of the to and from points
	 * returns an empty list of points.
	 */
    @Test
    public void emptyRouteTest() {
        Graph mapGraph = new Graph();
        RouteCalculator dijkstra = Dijkstra.getSingleton();
        boolean exceptionThrown = false;
        try {
            dijkstra.calculateRoute(null, null, mapGraph);
        } catch (NullPointerException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
    }

    /**
	 * This method checks if the method calculateRoute is able to calculate a
	 * route between the same point. The method should return a route with one
	 * single point.
	 */
    @Test
    public void onePointTest() {
        Graph mapGraph = new Graph();
        RouteCalculator dijkstra = Dijkstra.getSingleton();
        Point point = new Point(0.0, 0.0);
        mapGraph.addNode(point);
        Route route = dijkstra.calculateRoute(point, point, mapGraph);
        Assert.assertNotNull(route);
        Assert.assertNotNull(route.getRoute());
        Assert.assertEquals(1, route.getRoute().size());
        Assert.assertEquals(point, route.getRoute().get(0));
    }

    /**
	 * This test checks if the class does throw an exception if it is getting as
	 * parameters a graph and two points which aren't contained in the graph.
	 */
    @Test
    public void onePointButNotAddedInGraphTest() {
        Graph mapGraph = new Graph();
        RouteCalculator dijkstra = Dijkstra.getSingleton();
        Assert.assertNotNull(dijkstra);
        Point[] nodes = { new Point(0.0, 0.0), new Point(1.0, 0.0), new Point(0.0, 1.0), new Point(1.0, 1.0) };
        mapGraph.addNode(nodes[0]);
        mapGraph.addNode(nodes[1]);
        mapGraph.addNode(nodes[2]);
        mapGraph.addNode(nodes[3]);
        boolean wentInTry = false;
        try {
            dijkstra.calculateRoute(new Point(2.0, 2.0), new Point(2.0, 3.0), mapGraph).getRoute();
        } catch (IllegalArgumentException e) {
            wentInTry = true;
        }
        Assert.assertTrue(wentInTry);
    }

    /**
	 * This method checks if by trying to calculate a route between 2 points
	 * which do not have a possible route between them it returns an empty
	 * route.
	 */
    @Test
    public void noRouteToNode() {
        Graph mapGraph = new Graph();
        RouteCalculator dijkstra = Dijkstra.getSingleton();
        Assert.assertNotNull(dijkstra);
        Point[] nodes = { new Point(0.0, 0.0), new Point(1.0, 0.0), new Point(0.0, 1.0), new Point(1.0, 1.0) };
        mapGraph.addNode(nodes[0]);
        mapGraph.addNode(nodes[1]);
        mapGraph.addNode(nodes[2]);
        mapGraph.addNode(nodes[3]);
        Route route = dijkstra.calculateRoute(nodes[0], nodes[3], mapGraph);
        Assert.assertNotNull(route);
        Assert.assertNotNull(route.getRoute());
        Assert.assertEquals(0, route.getRoute().size());
    }

    /**
	 * This class tests the method calculateRoute when setting null as parameter
	 * of graph
	 */
    @Test
    public void nullRouteTest() {
        RouteCalculator dijkstra = Dijkstra.getSingleton();
        Assert.assertNotNull(dijkstra);
        Point[] nodes = { new Point(0.0, 0.0), new Point(1.0, 0.0) };
        boolean exceptionThrown = false;
        try {
            dijkstra.calculateRoute(nodes[0], nodes[1], null);
        } catch (NullPointerException e) {
            exceptionThrown = true;
        }
        Assert.assertTrue(exceptionThrown);
    }
}
