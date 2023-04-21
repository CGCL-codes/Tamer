package org.hl7.types.impl;

import junit.framework.TestCase;
import org.hl7.types.CD;

/**
 * This class defines unit test code against data type Concept Descriptor (CD).
 *
 * @author OWNER: Scott Jiang
 * @author LAST UPDATE $Author: crosenthal $
 * @version Since HL7 SDK v1.2
 *          revision    $Revision: 5652 $
 *          date        $Date: 2007-03-30 11:35:44 -0400 (Fri, 30 Mar 2007) $
 */
public class CDImplTest extends TestCase {

    /**
	 * Logging constant used to identify source of log entry, that could be later used to create
	 * logging mechanism to uniquely identify the logged class.
	 */
    private static final String LOGID = "$RCSfile$";

    /**
	 * String that identifies the class version and solves the serial version UID problem.
	 * This String is for informational purposes only and MUST not be made final.
	 *
	 * @see <a href="http://www.visi.com/~gyles19/cgi-bin/fom.cgi?file=63">JBuilder vice javac serial version UID</a>
	 */
    public static String RCSID = "$Header$";

    private CD instance1 = null;

    private CD instance2 = null;

    public void setUp() throws Exception {
        super.setUp();
        instance1 = CDimpl.valueOf("30971-6", "2.16.840.1.113883.19");
        instance2 = CDimpl.valueOf("30971-6", "2.16.840.1.113883.19");
    }

    public void testEquals() {
        assertEquals(instance1.equals(instance2), true);
    }

    public void testHashCodeEquals() {
        assertEquals(instance1.hashCode(), instance2.hashCode());
    }

    public void testStringEquals() {
        assertEquals(instance1.toString(), instance2.toString());
    }

    public void tearDown() throws Exception {
        instance1 = null;
        instance2 = null;
        super.tearDown();
    }
}
