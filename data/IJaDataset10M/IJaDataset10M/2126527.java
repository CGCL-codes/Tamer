package org.eclipse.core.runtime.internal.adaptor;

import org.eclipse.osgi.framework.adaptor.core.BundleInstaller;
import org.eclipse.osgi.service.resolver.BundleDescription;
import org.osgi.framework.*;

public class EclipseBundleInstaller implements BundleInstaller {

    BundleContext context;

    public EclipseBundleInstaller(BundleContext context) {
        this.context = context;
    }

    public void installBundle(BundleDescription toInstall) throws BundleException {
        context.installBundle(toInstall.getLocation());
    }

    public void uninstallBundle(BundleDescription toUninstallId) throws BundleException {
        Bundle toUninstall = context.getBundle(toUninstallId.getBundleId());
        if (toUninstall != null) toUninstall.uninstall();
    }

    public void updateBundle(BundleDescription toUpdateId) throws BundleException {
        Bundle toUpdate = context.getBundle(toUpdateId.getBundleId());
        if (toUpdate != null) toUpdate.update();
    }
}
