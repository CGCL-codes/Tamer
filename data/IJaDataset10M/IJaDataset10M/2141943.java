package org.personalsmartspace.ext.rms040;

import java.util.Collection;
import java.util.Iterator;
import junit.framework.TestCase;
import org.osgi.util.tracker.ServiceTracker;
import org.personalsmartspace.ext.Activator;
import org.personalsmartspace.pss_sm_api.api.pss3p.IServiceDiscovery;
import org.personalsmartspace.pss_sm_api.impl.PssService;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtException;

/**
 * @author mcrotty
 *
 */
public class RecommenderServiceDiscovery extends TestCase {

    private IServiceDiscovery discovery = null;

    private ServiceTracker discoveryTracker = null;

    protected void setUp() throws Exception {
        super.setUp();
        if (discovery == null) {
            if (discoveryTracker == null) {
                discoveryTracker = new ServiceTracker(Activator.bundleContext, IServiceDiscovery.class.getName(), null);
            }
            discoveryTracker.open();
            discovery = (IServiceDiscovery) discoveryTracker.getService();
            discoveryTracker.close();
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests that the context broker is present and resolves.
     */
    public void testRequiredServicesPresent() {
        assertNotNull("DEPLOYMENT ERROR: Service Discovery is not active or present (check with T52)", discovery);
    }

    /**
     * Tests that the discovery replies with something.
     */
    public void testDiscoveryRequest() {
        try {
            Collection<PssService> services = discovery.findAllServices();
            assertNotNull("Null collection returned", services);
        } catch (ServiceMgmtException e) {
            e.printStackTrace();
            fail("Unexpected service management exception");
        }
        try {
            Collection<PssService> services = discovery.findAllServices();
            assertFalse("Expected one or more framework services to be returned", services.isEmpty());
            for (Iterator<PssService> i = services.iterator(); i.hasNext(); ) {
                PssService p = i.next();
                assertNotNull("Null PssService passed in return type", p);
                assertNotNull("Null service id, passed in return type", p.getServiceId());
                assertNotNull("Empty service id, passed in return type", p.getServiceId().getLocalServiceId());
                assertFalse("Empty service id, passed in return type", p.getServiceId().getLocalServiceId().isEmpty());
            }
            System.out.println("Checked " + services.size() + " returned services");
        } catch (ServiceMgmtException e) {
            e.printStackTrace();
            fail("Unexpected service management exception");
        }
    }
}
