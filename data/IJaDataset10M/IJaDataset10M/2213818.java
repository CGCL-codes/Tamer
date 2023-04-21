package net.sf.jimo.modules.swt.impl;

import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class SWTBundleActivator implements BundleActivator {

    private Thread swtThread;

    private Display display;

    private ServiceRegistration registration;

    public void start(final BundleContext context) throws Exception {
        display = Display.getDefault();
        if (display != null) {
            registration = context.registerService(Display.class.getName(), display, null);
            return;
        }
    }

    public void stop(BundleContext context) throws Exception {
        if (registration != null) {
            registration.unregister();
        }
    }
}
