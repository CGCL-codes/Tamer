package org.hl7.types.impl;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.hl7.types.PN;
import org.hl7.types.PNXP;

/**
 * This class defines unit test code against data type Person Name (PN).
 *
 * @author OWNER: Scott Jiang
 * @author LAST UPDATE $Author: gschadow $
 * @version Since HL7 SDK v1.2
 *          revision    $Revision: 6730 $
 *          date        $Date: 2007-07-22 14:15:47 +0000 (Sun, 22 Jul 2007) $
 */
public class PNImplTest extends TestCase {

    /**
	 * Logging constant used to identify source of log entry, that could be later used to create
	 * logging mechanism to uniquely identify the logged class.
	 */
    private static final String LOGID = "$RCSfile$";

    /**
	 * String that identifies the class version and solves Connection  serial version UID problem.
	 * This String is for informational purposes only and MUST not be made final.
	 *
	 * @see <a href="http://www.visi.com/~gyles19/cgi-bin/fom.cgi?file=63">JBuilder vice javac serial version UID</a>
	 */
    public static String RCSID = "$Header$";

    private PN instance1 = null;

    private PN instance2 = null;

    private PN instance3 = null;

    private PN instance4 = null;

    private PN instance5 = null;

    public void setUp() throws Exception {
        super.setUp();
        instance1 = PNimpl.valueOf(getElementList(false));
        instance2 = PNimpl.valueOf(getElementList(false));
        instance3 = PNimpl.valueOf(getElementList(true));
        instance4 = PNimpl.valueOf((List<PNXP>) null);
        instance5 = PNimpl.valueOf((List<PNXP>) null);
    }

    private List<PNXP> getElementList(boolean allCap) {
        List<PNXP> elementList = new ArrayList<PNXP>();
        String firstStr = "John";
        String secondStr = "William";
        String thirdStr = "Smith";
        if (allCap) {
            elementList.add(PNXPimpl.valueOf(firstStr.toUpperCase()));
            elementList.add(PNXPimpl.valueOf(secondStr.toUpperCase()));
            elementList.add(PNXPimpl.valueOf(thirdStr.toUpperCase()));
        } else {
            elementList.add(PNXPimpl.valueOf(firstStr));
            elementList.add(PNXPimpl.valueOf(secondStr));
            elementList.add(PNXPimpl.valueOf(thirdStr));
        }
        return elementList;
    }

    public void testEquals() {
        assertEquals(instance1.equals(instance2), true);
    }

    public void testNULLEquals() {
        assertEquals(instance4.equals(instance5), true);
    }

    public void testNotEquals() {
        assertEquals(instance1.equals(instance3), false);
    }

    public void tearDown() throws Exception {
        instance1 = null;
        instance2 = null;
        super.tearDown();
    }
}
