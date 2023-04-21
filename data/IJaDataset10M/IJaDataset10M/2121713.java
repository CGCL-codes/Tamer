package cnaf.sidoc.ide.retrievalrequests.core.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * SIDoc Functions Core Plugin.
 * 
 * @author Angelo ZERR
 *
 */
public class SIDocRetrievalRequestsCorePlugin implements BundleActivator {

    private static BundleContext context;

    static BundleContext getContext() {
        return context;
    }

    public void start(BundleContext bundleContext) throws Exception {
        SIDocRetrievalRequestsCorePlugin.context = bundleContext;
    }

    public void stop(BundleContext bundleContext) throws Exception {
        SIDocRetrievalRequestsCorePlugin.context = null;
    }
}
