package de.suse.swamp.test;

import java.util.*;
import de.suse.swamp.core.container.*;
import de.suse.swamp.util.*;
import junit.framework.*;

/**
 * @author tschmidt
 * This Class is called by <i>ant junit</i> and calls  
 */
public class SWAMPTestSuite extends TestCase {

    public static Test suite() {
        Logger.log.setLevel(org.apache.log4j.Level.INFO);
        Date start = new Date();
        Logger.LOG("Starting SWAMP Init");
        SWAMP swamp = SWAMP.getInstance();
        long diff = System.currentTimeMillis() - start.getTime();
        Logger.LOG("SWAMP-init took " + (diff / 1000l) + " seconds");
        TestSuite suite = new TestSuite("All SWAMP JUnit Tests");
        suite.addTest(new TestSuite(TestWorkflowRemove.class));
        return suite;
    }
}
