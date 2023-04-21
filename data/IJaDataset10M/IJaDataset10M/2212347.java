package org.jowidgets.workbench.toolkit.api;

import org.jowidgets.workbench.api.IComponent;

public interface IWorkbenchToolkit {

    ILayoutBuilderFactory getLayoutBuilderFactory();

    IWorkbenchPartBuilderFactory getWorkbenchPartBuilderFactory();

    IWorkbenchPartFactory getWorkbenchPartFactory();

    IComponentFactory createComponentFactory(Class<? extends IComponent> componentType);
}
