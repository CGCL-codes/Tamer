package org.eclipse.osgi.framework.internal.defaultadaptor;

import java.io.IOException;
import org.eclipse.osgi.framework.adaptor.BundleProtectionDomain;
import org.eclipse.osgi.framework.adaptor.ClassLoaderDelegate;
import org.eclipse.osgi.framework.adaptor.core.*;

public class DefaultElementFactory implements AdaptorElementFactory {

    public AbstractBundleData createBundleData(AbstractFrameworkAdaptor adaptor, long id) throws IOException {
        return new DefaultBundleData((DefaultAdaptor) adaptor, id);
    }

    public org.eclipse.osgi.framework.adaptor.BundleClassLoader createClassLoader(ClassLoaderDelegate delegate, BundleProtectionDomain domain, String[] bundleclasspath, AbstractBundleData data) {
        return new DefaultClassLoader(delegate, domain, bundleclasspath, data.getAdaptor().getBundleClassLoaderParent(), data);
    }
}
