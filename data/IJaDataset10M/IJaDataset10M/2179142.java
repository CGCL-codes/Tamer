package eu.commius;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * 
 * @author Martin �eleng
 *
 */
public class FelixActivator implements BundleActivator {

    private BundleContext m_context = null;

    /**
	 * 
	 */
    public void start(BundleContext context) {
        m_context = context;
    }

    /**
	 * 
	 */
    public void stop(BundleContext context) {
        m_context = null;
    }

    /**
	 * 
	 * @return
	 */
    public BundleContext getContext() {
        return m_context;
    }
}
