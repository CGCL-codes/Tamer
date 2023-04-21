package org.simcom.pageengine;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentOnBeforeRenderListener;
import org.apache.wicket.injection.ComponentInjector;
import org.apache.wicket.injection.web.InjectorHolder;
import org.simcom.pageengine.injector.SimComInjector;

public class SimComComponentInjector extends ComponentInjector implements IComponentOnBeforeRenderListener {

    public SimComComponentInjector() {
    }

    public SimComComponentInjector(SimComApplication simComApplication) {
        System.out.println("===============================");
        InjectorHolder.setInjector(new SimComInjector());
    }

    private SimComInjector injector = new SimComInjector();

    @Override
    public void onBeforeRender(Component component) {
        injector.inject(component);
    }
}
