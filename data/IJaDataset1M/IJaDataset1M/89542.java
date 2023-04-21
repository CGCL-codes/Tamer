package nz.ac.massey.softwarec.group3.game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Natalie
 */
public class MoveTest {

    private int destinationNode, originNode;

    private Move test;

    public MoveTest() {
    }

    @Before
    public void setUp() {
        test = new Move("BUS", 1, 2);
    }

    @After
    public void tearDown() {
        test = null;
    }

    /**
     * Test of getDestinationNode method, of class Move.
     */
    @Test
    public void testGetDestinationNode() {
        System.out.println("getDestinationNode");
        int expResult = 2;
        int result = test.getDestinationNode();
        assertEquals(expResult, result);
    }

    /**
     * Test of setDestinationNode method, of class Move.
     */
    @Test
    public void testSetDestinationNode() {
        System.out.println("setDestinationNode");
        destinationNode = 3;
        test.setDestinationNode(destinationNode);
        int result = test.getDestinationNode();
        int expResult = 3;
        assertEquals(expResult, result);
    }

    /**
     * Test of getOriginNode method, of class Move.
     */
    @Test
    public void testGetOriginNode() {
        System.out.println("getOriginNode");
        int expResult = 1;
        int result = test.getOriginNode();
        assertEquals(expResult, result);
    }

    /**
     * Test of setOriginNode method, of class Move.
     */
    @Test
    public void testSetOriginNode() {
        System.out.println("setOriginNode");
        originNode = 2;
        test.setOriginNode(originNode);
        int result = test.getOriginNode();
        int expResult = originNode;
        assertEquals(expResult, result);
    }

    /**
     * Test of getTicketType method, of class Move.
     */
    @Test
    public void testGetTicketType() {
        System.out.println("getTicketType");
        String expResult = "BUS";
        String result = test.getTicketType();
        assertEquals(expResult, result);
    }

    /**
     * Test of setTicketType method, of class Move.
     */
    @Test
    public void testSetTicketType() {
        System.out.println("setTicketType");
        String ticketType = "UNDERGROUND";
        test.setTicketType(ticketType);
        String expResult = ticketType;
        String result = test.getTicketType();
        assertEquals(expResult, result);
    }
}
