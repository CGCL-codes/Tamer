package org.tripcom.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Iterator;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Statement;

/**
 * Test cases for rdmultiple.
 * 
 * @author Christian Schreiber
 * 
 */
public class RdMultipleTest extends AbstractTest {

    /**
	 * Write the statements into the triplespace.
	 * 
	 * @throws Exception
	 */
    @Before
    public void setUp() throws Exception {
        for (Statement s : statements) {
            coreAPI.out(s, rootSpace);
        }
        Thread.sleep(5000);
    }

    @Test
    public void testRdMultiple01() throws Exception {
        Set<Set<Statement>> result = extendedAPI.rdmultiple(DEFAULT_QUERY, rootSpace, TIMEOUT);
        System.out.println(result.size());
        for (Iterator<Set<Statement>> i = result.iterator(); i.hasNext(); ) {
            for (Iterator<Statement> j = i.next().iterator(); j.hasNext(); ) System.out.println(j.next());
        }
        assertNotNull(result);
        int i = 0;
        for (Set<Statement> s : result) {
            assertNotNull(s);
            for (Statement st : s) {
                assertNotNull(st);
                System.err.println(st);
                i++;
            }
        }
        assertTrue("only " + i + " triples found", i >= 10);
    }
}
