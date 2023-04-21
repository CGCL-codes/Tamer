package net.sourceforge.olympos.xtext;

import org.eclipse.xtext.junit4.IInjectorProvider;
import com.google.inject.Injector;

public class DslUiInjectorProvider implements IInjectorProvider {

    public Injector getInjector() {
        return net.sourceforge.olympos.xtext.ui.internal.DslActivator.getInstance().getInjector("net.sourceforge.olympos.xtext.Dsl");
    }
}
