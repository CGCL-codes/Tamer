package org.jowidgets.workbench.toolkit.api;

import org.jowidgets.api.model.item.IMenuModel;
import org.jowidgets.workbench.api.IComponent;
import org.jowidgets.workbench.api.IDisposeCallback;

public interface IComponentNodeModelBuilder extends IComponentNodeContainerModelBuilder<IComponentNodeModelBuilder>, IWorkbenchPartBuilder<IComponentNodeModelBuilder> {

    IComponentNodeModelBuilder setSelected(boolean selected);

    IComponentNodeModelBuilder setExpanded(boolean expanded);

    IComponentNodeModelBuilder setPopupMenu(IMenuModel popupMenu);

    IComponentNodeModelBuilder setComponentFactory(IComponentFactory componentFactory);

    IComponentNodeModelBuilder setComponentFactory(Class<? extends IComponent> componentType);

    IComponentNodeModelBuilder setInitializeCallback(IComponentNodeInitializeCallback initializeCallback);

    IComponentNodeModelBuilder setDisposeCallback(IDisposeCallback disposeCallback);

    IComponentNodeModel build();
}
