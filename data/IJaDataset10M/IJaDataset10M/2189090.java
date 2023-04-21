package net.grinder.tools.tcpsniffer;

import junit.framework.TestCase;
import junit.swingui.TestRunner;
import net.grinder.common.GrinderException;

/**
 * Unit test case for <code>ConnectionDetails</code>.
 *
 * @author Philip Aston
 * @version $Revision: 940 $
 */
public class TestConnectionDetails extends TestCase {

    public static void main(String[] args) {
        TestRunner.run(TestConnectionDetails.class);
    }

    public TestConnectionDetails(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
    }

    public void testGetDescription() throws Exception {
        final ConnectionDetails connectionDetails = new ConnectionDetails("one", 55, "two", 121, true);
        assertEquals("one:55->two:121", connectionDetails.getDescription());
    }

    public void testGetURLBase() throws Exception {
        final ConnectionDetails connectionDetails = new ConnectionDetails("one", 55, "two", 121, true);
        assertEquals("https://two:121", connectionDetails.getURLBase("http"));
    }

    public void testEquality() throws Exception {
        final ConnectionDetails[] connectionDetails = { new ConnectionDetails("A", 55, "B", 80, false), new ConnectionDetails("a", 55, "B", 80, false), new ConnectionDetails("c", 55, "B", 80, false), new ConnectionDetails("a", 55, "B", 80, true), new ConnectionDetails("a", 56, "B", 80, false) };
        assertEquals(connectionDetails[0], connectionDetails[0]);
        assertEquals(connectionDetails[0], connectionDetails[1]);
        assertEquals(connectionDetails[1], connectionDetails[0]);
        assertTrue(!connectionDetails[0].equals(connectionDetails[2]));
        assertTrue(!connectionDetails[1].equals(connectionDetails[3]));
        assertTrue(!connectionDetails[1].equals(connectionDetails[4]));
    }
}
