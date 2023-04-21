package net.sourceforge.olympos.dsl.domain;

import org.eclipse.xtext.junit4.IInjectorProvider;
import com.google.inject.Injector;

public class DomainUiInjectorProvider implements IInjectorProvider {

    public Injector getInjector() {
        return net.sourceforge.olympos.dsl.domain.ui.internal.DomainActivator.getInstance().getInjector("net.sourceforge.olympos.dsl.domain.Domain");
    }
}
