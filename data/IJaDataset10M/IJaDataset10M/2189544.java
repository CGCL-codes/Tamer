package net.sf.selfim.core.info;

import junit.framework.TestCase;

/**
 * @author raiser
 */
public class InformationTest extends TestCase {

    public void testIsIdValid() {
        assertEquals(false, Information.isIdValid("--"));
    }

    public void testIsOlder() {
        Information info1 = new Information("", "20040101101010");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Information info2 = new Information("", "20040101101011");
        assertEquals(true, info1.isOlder(info2));
        assertEquals(false, info1.isOlder(info1));
        assertEquals(false, info2.isOlder(info1));
        assertEquals(false, info2.isOlder(info2));
    }
}
