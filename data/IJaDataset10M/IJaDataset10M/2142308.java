package org.teaframework.services.persistence;

import junit.framework.TestCase;
import org.teaframework.container.ServiceFactory;
import org.teaframework.container.ServiceLoader;
import org.teaframework.services.persistence.HibernateService;

/**
 * JUnit test case for the
 * {@link org.teaframework.services.persistence.HibernateServiceImpl).
 * 
 * @author <a href="mailto:founder_chen@yahoo.com">Peter Cheng </a>
 * @version $Revision: 1.5 $ $Date: 2005/04/27 08:08:25 $
 * @version Revision: 1.0
 */
public class HibernateServiceImplTest extends TestCase {

    private HibernateService hService = null;

    private ServiceLoader sl = ServiceLoader.getInstance();

    private ServiceFactory sf = ServiceFactory.getInstance();

    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        if (!sl.isServiceLoaded()) {
            sl.initService("tea-config.xml");
        }
        hService = (HibernateService) sf.getService(HibernateService.class);
    }

    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test for Configuration getConfiguration()
     */
    public void testGetConfiguration() {
    }
}
