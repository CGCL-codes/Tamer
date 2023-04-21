package co.fxl.gui.navigation.menu.impl;

import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IWidgetProvider;
import co.fxl.gui.navigation.menu.api.IMenuWidget;

public class MenuWidgetImplProvider implements IWidgetProvider<IMenuWidget> {

    @Override
    public IMenuWidget createWidget(IContainer panel) {
        return new MenuWidgetImpl(panel);
    }

    @Override
    public Class<IMenuWidget> widgetType() {
        return IMenuWidget.class;
    }
}
