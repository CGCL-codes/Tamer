package net.sf.rcer.rfcgen.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;
import com.google.inject.Injector;

/**
 *@generated
 */
public class RFCMappingExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

    @Override
    protected Bundle getBundle() {
        return net.sf.rcer.rfcgen.ui.internal.RFCMappingActivator.getInstance().getBundle();
    }

    @Override
    protected Injector getInjector() {
        return net.sf.rcer.rfcgen.ui.internal.RFCMappingActivator.getInstance().getInjector("net.sf.rcer.rfcgen.RFCMapping");
    }
}
