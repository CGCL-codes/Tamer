package ch.jester.commonservices.impl.internal;

import org.osgi.framework.BundleContext;
import ch.jester.common.ui.activator.AbstractUIActivator;

public class CommonServicesActivator extends AbstractUIActivator {

    private static AbstractUIActivator mInstance;

    public static AbstractUIActivator getDefault() {
        return mInstance;
    }

    @Override
    public void startDelegate(BundleContext pContext) {
        mInstance = this;
    }

    @Override
    public void stopDelegate(BundleContext pContext) {
    }
}
