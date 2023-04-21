package tests;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import org.dom4j.DocumentException;
import gmof.Coordinates;
import gmof.Customer;
import gmof.Landscape;
import gmof.Pathfinder;
import gmof.Tile;
import gmof.WaypointList;
import junit.framework.TestCase;

/**
 * Test case for the Customer class.
 * 
 * @author max
 * @version 0.0
 * 
 * TODO Make this work!
 */
public class CustomerTest extends TestCase {

    private Landscape simpleLandscape33;

    private Pathfinder way;

    private Customer joe;

    private BufferedInputStream source;

    protected void setUp() throws Exception {
        super.setUp();
        simpleLandscape33 = new Landscape();
        simpleLandscape33.setTile(new Coordinates(0, 0), new Tile(true, true, false, true));
        simpleLandscape33.setTile(new Coordinates(1, 0), new Tile(false, true, false, true));
        simpleLandscape33.setTile(new Coordinates(2, 0), new Tile(false, true, true, false));
        simpleLandscape33.setTile(new Coordinates(0, 1), new Tile(true, true, false, false));
        simpleLandscape33.setTile(new Coordinates(1, 1), new Tile(false, true, false, true));
        simpleLandscape33.setTile(new Coordinates(2, 1), new Tile(false, false, true, true));
        simpleLandscape33.setTile(new Coordinates(0, 2), new Tile(true, false, false, true));
        simpleLandscape33.setTile(new Coordinates(1, 2), new Tile(false, true, false, true));
        simpleLandscape33.setTile(new Coordinates(2, 2), new Tile(false, true, true, true));
        way = new Pathfinder(simpleLandscape33);
        source = new BufferedInputStream(new FileInputStream("bin/tests/customerTest.xml"));
    }

    public void testXml() {
        try {
            joe = new Customer(simpleLandscape33, way, source);
        } catch (DocumentException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(joe.getName(), "Joe Dalton");
        assertEquals(joe.getPosition().getKey(), new Coordinates(0, 0).getKey());
        assertEquals(joe.getPositionInTile().getKey(), new Coordinates(1, 2).getKey());
        assertEquals(joe.getShoppingList().getWaypoint(0).getKey(), new Coordinates(1, 0).getKey());
        assertEquals(joe.getShoppingList().getWaypoint(1).getKey(), new Coordinates(0, 2).getKey());
    }

    public void testRun() {
        WaypointList shoppingList = new WaypointList();
        shoppingList.add(new Coordinates(2, 2));
        shoppingList.add(new Coordinates(0, 0));
        joe = new Customer(simpleLandscape33, new Coordinates(0, 0), new Coordinates(0, 0), way, shoppingList);
        assertEquals(joe.getPosition().getKey(), new Coordinates(0, 0).getKey());
        while (!joe.isOnWaypoint()) {
            joe.act();
        }
        assertEquals(joe.getPosition().getKey(), new Coordinates(2, 2).getKey());
        joe.act();
        while (!joe.isOnWaypoint()) {
            joe.act();
        }
        assertEquals(joe.getPosition().getKey(), new Coordinates(0, 0).getKey());
        joe.act();
        assertEquals(joe.getPosition().getKey(), new Coordinates(0, 0).getKey());
    }
}
