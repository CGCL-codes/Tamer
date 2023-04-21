package org.personalsmartspace.sre.ems.test.mock;

import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventAdmin;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.personalsmartspace.onm.api.platform.IPeerGroupMgr;

public class MockServiceTracker extends ServiceTracker {

    private Object mockObject;

    public MockServiceTracker(BundleContext bc, String className, ServiceTrackerCustomizer atc) {
        super(bc, className, null);
        if (className.equals(EventAdmin.class.getName())) {
            mockObject = new MockEventAdmin();
        } else if (className.equals(IPeerGroupMgr.class.getName())) {
            mockObject = new MockIPeerGroupMgr();
        }
    }

    @Override
    public Object getService() {
        return mockObject;
    }
}
