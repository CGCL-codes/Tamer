package net.sourceforge.olympos.dsl.domain.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;
import com.google.inject.Injector;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class DomainExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

    @Override
    protected Bundle getBundle() {
        return net.sourceforge.olympos.dsl.domain.ui.internal.DomainActivator.getInstance().getBundle();
    }

    @Override
    protected Injector getInjector() {
        return net.sourceforge.olympos.dsl.domain.ui.internal.DomainActivator.getInstance().getInjector("net.sourceforge.olympos.dsl.domain.Domain");
    }
}
