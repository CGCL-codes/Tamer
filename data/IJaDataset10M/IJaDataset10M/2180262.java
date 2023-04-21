package org.opene.test.perf;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;
import org.openejb.test.stateless.StatelessRmiIiopTests;
import org.openejb.test.stateful.StatefulRmiIiopTests;
import org.openejb.test.entity.bmp.BmpRmiIiopTests;
import org.openejb.test.entity.cmp.CmpRmiIiopTests;

/**
 * 
 * @author <a href="mailto:david.blevins@visi.com">David Blevins</a>
 * @author <a href="mailto:Richard@Monson-Haefel.com">Richard Monson-Haefel</a>
 */
public class MultiClientTests extends junit.framework.TestCase {

    public MultiClientTests(String name) {
        super(name);
    }

    public static junit.framework.Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new StatelessRmiIiopTests());
        suite.addTest(new StatefulRmiIiopTests());
        suite.addTest(new CmpRmiIiopTests());
        suite.addTest(new BmpRmiIiopTests());
        return suite;
    }
}
