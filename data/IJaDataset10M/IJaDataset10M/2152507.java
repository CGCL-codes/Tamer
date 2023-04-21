package org.tripcom.kerneltests.api2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Statement;

/**
 * Test cases for blocking rd.
 * 
 * @author Christian Schreiber
 * 
 */
public class BlockingRdTest extends AbstractTest {

    public BlockingRdTest(String localhostname, String lport, String remotehostname, String rport) {
        super(localhostname, lport, remotehostname, rport);
    }

    /**
	 * Called before the test case is executed.<br>
	 * Sets the variable <code>returned</code> to <code>false</code>
	 */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.returned = false;
    }

    /**
	 * Indicates if the blocking rd has returned.
	 */
    private boolean returned = false;

    @Test
    public void testBlockingRdWithSpace01() throws Exception {
        this.testBlockingRd01(true);
    }

    @Test
    public void testBlockingRdWithoutSpace01() throws Exception {
        this.testBlockingRd01(false);
    }

    public void testBlockingRd01(boolean withSpace) throws Exception {
        long start = System.currentTimeMillis();
        Set<Statement> st;
        if (withSpace) {
            st = tsclient.rd(DEFAULT_QUERY, rootSpace, TIMEOUT);
        } else {
            st = tsclient.rd(DEFAULT_QUERY, TIMEOUT);
        }
        assertEquals(0, st.size());
        assertTrue("The operation did not block", System.currentTimeMillis() >= start + TIMEOUT);
    }

    /**
	 * Performs a blocking rd on the space and checks wether the rd returns
	 * after a matching entry has been written.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testBlockingRdWithSpace02() throws Exception {
        this.testBlockingRd02(true);
    }

    @Test
    public void testBlockingRdWithoutSpace02() throws Exception {
        this.testBlockingRd02(false);
    }

    public void testBlockingRd02(final boolean withSpace) throws Exception {
        Thread t = new Thread() {

            public void run() {
                try {
                    Set<Statement> result;
                    if (withSpace) {
                        result = tsclient.rd(DEFAULT_QUERY, rootSpace, TIMEOUT * 4);
                    } else {
                        result = tsclient.rd(DEFAULT_QUERY, TIMEOUT * 4);
                    }
                    System.err.println(result);
                    assertNotNull(result);
                    assertEquals(1, result.size());
                    assertNotNull(result.iterator().next());
                    returned = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        };
        t.start();
        Thread.sleep(TIMEOUT);
        tsclient.out(statements.iterator().next(), rootSpace);
        Thread.sleep(TIMEOUT);
        if (t.isAlive()) {
            t.interrupt();
        }
        assertTrue("The blocking read operation did not return", this.returned);
    }
}
