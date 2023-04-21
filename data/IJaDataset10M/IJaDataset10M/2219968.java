package com.buschmais.maexo.mbeans.osgi.core.impl.lifecyle;

import javax.management.DynamicMBean;
import javax.management.ObjectName;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import com.buschmais.maexo.framework.commons.mbean.lifecycle.MBeanLifeCycleSupport;
import com.buschmais.maexo.mbeans.osgi.core.BundleMBean;
import com.buschmais.maexo.mbeans.osgi.core.impl.BundleMBeanImpl;

/**
 * This class implements a bundle event listener to manage the life cycle of the
 * associated bundle MBeans.
 */
public final class BundleMBeanLifeCycle extends MBeanLifeCycleSupport implements BundleListener {

    /**
	 * Constructor.
	 * 
	 * @param bundleContext
	 *            The bundle context of the exporting bundle.
	 */
    public BundleMBeanLifeCycle(BundleContext bundleContext) {
        super(bundleContext);
    }

    /**
	 * Registers all existing bundles as MBeans. Adds bundle listener.
	 */
    public void start() {
        for (org.osgi.framework.Bundle bundle : super.getBundleContext().getBundles()) {
            this.bundleChanged(new BundleEvent(BundleEvent.INSTALLED, bundle));
        }
        super.getBundleContext().addBundleListener(this);
    }

    /**
	 * Removes bundle listener. Unregisters all registered bundle MBeans.
	 */
    public void stop() {
        super.getBundleContext().removeBundleListener(this);
        for (org.osgi.framework.Bundle bundle : super.getBundleContext().getBundles()) {
            this.bundleChanged(new BundleEvent(BundleEvent.UNINSTALLED, bundle));
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public synchronized void bundleChanged(BundleEvent bundleEvent) {
        org.osgi.framework.Bundle bundle = bundleEvent.getBundle();
        ObjectName objectName = super.getObjectNameHelper().getObjectName(bundle, org.osgi.framework.Bundle.class);
        switch(bundleEvent.getType()) {
            case BundleEvent.INSTALLED:
                BundleMBean bundleMBean = new BundleMBeanImpl(super.getBundleContext(), bundle);
                super.registerMBeanService(DynamicMBean.class, objectName, bundleMBean);
                break;
            case BundleEvent.UNINSTALLED:
                super.unregisterMBeanService(objectName);
                break;
            default:
                assert false : "Unexpected BundleEvent";
        }
    }
}
