package org.xvr.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;
import com.google.inject.Injector;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class S3DExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

    @Override
    protected Bundle getBundle() {
        return org.xvr.ui.internal.S3DActivator.getInstance().getBundle();
    }

    @Override
    protected Injector getInjector() {
        return org.xvr.ui.internal.S3DActivator.getInstance().getInjector("org.xvr.S3D");
    }
}
