package g2.routemaster.bundle.gui;

import g2.routemaster.gui.ApplicationStart;
import g2.routemaster.model.ModelAdaptor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

public class GuiAdaptorActivator implements BundleActivator {

    private ServiceTracker finderTracker;

    private ServiceRegistration listerReg;

    public void start(BundleContext context) throws Exception {
        finderTracker = new ServiceTracker(context, ModelAdaptor.class.getName(), null);
        finderTracker.open();
        GuiAdaptor lister = new GuiAdaptorImpl(finderTracker);
        listerReg = context.registerService(GuiAdaptor.class.getName(), lister, null);
        ApplicationStart as = new ApplicationStart(lister);
        as.run();
    }

    public void stop(BundleContext context) throws Exception {
        listerReg.unregister();
        finderTracker.close();
    }
}
