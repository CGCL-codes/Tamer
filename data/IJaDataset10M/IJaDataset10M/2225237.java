package tyrex.resource.jdbc;

import tyrex.resource.jdbc.xa.ResourceJdbcXaSuite;
import junit.framework.TestSuite;

/**
 *
 * @author <a href="mailto:mills@intalio.com">David Mills</a>
 * @version $Revision: 1.1 $
 */
public class ResourceJdbcUnit {

    public ResourceJdbcUnit() {
    }

    public static TestSuite suite() {
        TestSuite suite = new TestSuite("ResourceJdbcUnit test harness");
        suite.addTest(ResourceJdbcXaSuite.suite());
        return suite;
    }

    public static void main(String args[]) {
        tyrex.Unit.runTests(args, suite());
    }
}
