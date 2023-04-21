package org.marre;

import org.marre.sms.GsmOperators;
import junit.framework.TestCase;

/**
 * 
 * @author Markus
 * @version $Id: GsmOperatorsTest.java 379 2005-11-26 17:42:42Z c95men $
 */
public class GsmOperatorsTest extends TestCase {

    public void testGetMCC_MNC() {
        int mccMnc[] = GsmOperators.getMCC_MNC("se", "telia");
        assertNotNull(mccMnc);
        assertEquals(2, mccMnc.length);
        assertEquals(240, mccMnc[0]);
        assertEquals(1, mccMnc[1]);
    }
}
