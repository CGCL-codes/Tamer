package disseminatorConfigGuiService.impl;

import gui.swt.ContainerService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {

    public static BundleContext mBundleContext = null;

    private ServiceTracker mContainerServiceTracker;

    private DisseminatorCompositeWrapper mDisseminatorCompositeWrapper;

    private BooleanObject mExternalStopCall;

    private BooleanObject mContainerServiceRemoved;

    public void start(BundleContext kBundleContext) throws Exception {
        System.out.println(kBundleContext.getBundle().getHeaders().get(Constants.BUNDLE_NAME) + " starting...");
        mBundleContext = kBundleContext;
        mExternalStopCall = new BooleanObject();
        mContainerServiceRemoved = new BooleanObject();
        mDisseminatorCompositeWrapper = new DisseminatorCompositeWrapper();
        ContainerServiceTrackerCustomizer kMainContainerServiceTrackerCustomizer = new ContainerServiceTrackerCustomizer(mDisseminatorCompositeWrapper, mExternalStopCall, mContainerServiceRemoved);
        mContainerServiceTracker = new ServiceTracker(kBundleContext, ContainerService.class.getName(), kMainContainerServiceTrackerCustomizer);
        mContainerServiceTracker.open();
    }

    public void stop(BundleContext bc) throws Exception {
        System.out.println(bc.getBundle().getHeaders().get(Constants.BUNDLE_NAME) + " stopping...");
        mExternalStopCall.setTrue();
        if (!mContainerServiceRemoved.getValue()) {
            mContainerServiceTracker.close();
        }
        mBundleContext = null;
    }
}
