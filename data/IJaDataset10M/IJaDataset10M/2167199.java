package jimo.spi.im.impl;

import jimo.osgi.api.util.BundleActivatorHelper;
import jimo.spi.im.api.Protocol;
import jimo.spi.im.api.exception.IllegalStateException;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class SPIBundleActivator extends BundleActivatorHelper {

    public static SPIBundleActivator INSTANCE;

    public SPIBundleActivator() {
        INSTANCE = this;
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        IMAccounts accounts = IMAccounts.getInstance();
    }
}
