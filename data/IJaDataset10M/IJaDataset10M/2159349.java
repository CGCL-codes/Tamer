package org.tripcom.kerneltests.api2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.openrdf.model.Statement;
import org.tripcom.integration.entry.SpaceURI;

public class ReadRecursiveTest extends AbstractTest {

    public ReadRecursiveTest(String localhostname, String lport, String remotehostname, String rport) {
        super(localhostname, lport, remotehostname, rport);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        SpaceURI currentSpace = rootSpace;
        for (int i = 0; i < 10; i++) {
            currentSpace = new SpaceURI(currentSpace.toString() + "/" + i);
            tsclient.create(currentSpace);
            System.err.println(currentSpace);
        }
        tsclient.outSynchrone(statements, currentSpace);
        System.err.println("Wrote data into " + currentSpace);
    }

    @Test
    public void testRecursiveReadFromDirectSubspace() throws Exception {
        Set<Statement> result = tsclient.rd(DEFAULT_QUERY, new SpaceURI(rootSpace.toString() + "/0/1/2/3/4/5/6/7/8"), TIMEOUT);
        System.err.println(result);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testRecursiveRead() throws Exception {
        Set<Statement> result = tsclient.rd(DEFAULT_QUERY, new SpaceURI(rootSpace.toString() + "/0"), TIMEOUT);
        System.err.println(result);
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
