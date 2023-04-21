package net.taylor.event.web;

import java.util.Iterator;
import net.taylor.testing.jboss.EJB3Container;

/**
 * @author jgilbert
 * @version $Id: ManagedBeanTest.java,v 1.1 2006/01/03 22:38:39 jgilbert01 Exp $
 *
 */
public class ManagedBeanTest extends junit.framework.TestCase {

    static {
        EJB3Container.startup();
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * @param arg0
	 */
    public ManagedBeanTest(String arg0) {
        super(arg0);
    }

    /**
	 * 
	 */
    public void testQuery() {
        EventBean bean = new EventBean();
        String result = bean.search();
        System.out.println(result);
        Iterator i = bean.getEvents().iterator();
        while (i.hasNext()) {
            System.out.println(i.next());
        }
    }
}
