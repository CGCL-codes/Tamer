package org.hip.vif.member;

import java.util.logging.Level;
import org.hip.kernel.sys.VSys;
import org.hip.vif.interfaces.IMessages;
import org.hip.vif.interfaces.IPartletHelper;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Activator for this OSGi bundle.
 *
 * @author Luthiger
 * Created: 30.11.2007
 */
public class Activator implements BundleActivator {

    private static BundleContext cBundleContext;

    private static IMessages cMessages;

    private static IPartletHelper cHelper;

    public void start(BundleContext inContext) throws Exception {
        cBundleContext = inContext;
        cMessages = new Messages();
        cHelper = new PartletHelper(inContext, cMessages, Constants.PLUGIN_ID_PARTLET);
        VSys.trace(Level.INFO, this, "start", inContext.getBundle().getSymbolicName() + " started.");
    }

    public void stop(BundleContext inContext) throws Exception {
        cHelper.dispose();
        cHelper = null;
        cBundleContext = null;
        cMessages = null;
        VSys.trace(Level.INFO, this, "stop", inContext.getBundle().getSymbolicName() + " stopped.");
    }

    /**
	 * @return BundleContext this bundle's context.
	 */
    public static BundleContext getContext() {
        return cBundleContext;
    }

    public static IMessages getMessages() {
        return cMessages;
    }

    /**
	 * @return IPartletHelper this bundle's partlet helper.
	 */
    public static IPartletHelper getPartletHelper() {
        return cHelper;
    }

    /**
	 * Convenience method.
	 * 
	 * @return String the name of the bundel.
	 */
    public static synchronized String getBundleName() {
        return cBundleContext.getBundle().getSymbolicName();
    }
}
