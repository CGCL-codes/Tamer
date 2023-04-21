package gov.sns.apps.energymaster.test;

import gov.sns.apps.energymaster.CaUpdateCounter;
import junit.framework.TestCase;

public class CaUpdateCounterTest extends TestCase {

    public CaUpdateCounterTest(String name) {
        super(name);
    }

    public void testCaUpdateCounter(String ch) {
        double timeRange = 10;
        CaUpdateCounter counter = new CaUpdateCounter(ch, timeRange);
        int nUpdate = counter.getUpdateNumber();
        assertTrue(nUpdate >= 0);
    }
}
