package org.ensembl.test;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.ensembl.datamodel.GeneSnapShot;
import org.ensembl.datamodel.MappingSession;
import org.ensembl.datamodel.StableIDEvent;
import org.ensembl.driver.StableIDEventAdaptor;
import org.ensembl.util.StringUtil;

/**
 * Test class for StableIDEvents. 
 */
public class StableIDEventTest extends CoreBase {

    private static Logger logger = Logger.getLogger(StableIDEventTest.class.getName());

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public StableIDEventTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(StableIDEventTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        stableIDEventAdaptor = driver.getStableIDEventAdaptor();
    }

    public void testFetchCurrent() throws Exception {
        assertNotNull(stableIDEventAdaptor);
        List stableIDs = stableIDEventAdaptor.fetchCurrent("xxxxxsdfsdf");
        assertTrue(stableIDs.size() == 0);
        stableIDs = stableIDEventAdaptor.fetchCurrent("ENSG00000000003");
        assertTrue(stableIDs.size() > 0);
        stableIDs = stableIDEventAdaptor.fetchCurrent("ENSG00000004469");
        assertTrue(stableIDs.size() > 1);
    }

    public void testVersionSupport() throws Exception {
        List events = stableIDEventAdaptor.fetch("ENSG00000172983");
        assertTrue(events.size() > 0);
        for (Iterator iter = events.iterator(); iter.hasNext(); ) {
            StableIDEvent element = (StableIDEvent) iter.next();
            assertNotNull("type not set", element.getType());
            for (Iterator iterator = element.getRelatedStableIDs().iterator(); iterator.hasNext(); ) {
                String relatedStableID = (String) iterator.next();
                int[] versions = element.getRelatedVersions(relatedStableID);
                assertTrue(versions.length > 0);
                logger.fine(relatedStableID + ": versions = " + StringUtil.toString(versions));
            }
            logger.fine(element.toString());
        }
    }

    public void testFetchGeneSnapshot() throws Exception {
        String id = "ENSG00000172983";
        GeneSnapShot gss = stableIDEventAdaptor.fetchGeneSnapShot(id, 1);
        assertNotNull(gss);
        assertEquals(gss.getArchiveStableID().getStableID(), id);
    }

    private StableIDEventAdaptor stableIDEventAdaptor;
}
