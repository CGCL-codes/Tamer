package inc.che.common.type;

import junit.framework.*;
import org.apache.log4j.*;

/**
 *  <b>Testklasse f�r @todo </b>
 * @version $Id: IntHashMapTest.java,v 1.1 2005/03/06 12:56:58 stevemcmee Exp $
 * @author <address> Steve McMee &lt;stevemcmee@users.sourceforge.net&gt;</address>
 */
public class IntHashMapTest extends TestCase {

    /** CVS ID of this file */
    public static final String CVS_ID = "$Id: IntHashMapTest.java,v 1.1 2005/03/06 12:56:58 stevemcmee Exp $";

    /**
     * logger instance for this class
     */
    private static Logger log = Logger.getLogger(IntHashMapTest.class);

    /** Creates a new instance of IntHashMapTest */
    public IntHashMapTest(String name) {
        super(name);
    }

    public void testIntHashMap() {
        IntHashMap hashMap = new IntHashMap();
        for (int i = 0; i < 10; i++) {
            hashMap.put(i, String.valueOf(i));
        }
        String value;
        for (int i = 0; i < 10; i++) {
            value = (String) hashMap.get(i);
            assertTrue(i == Integer.valueOf(value).intValue());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new junit.textui.TestRunner().run(IntHashMapTest.class);
    }
}
