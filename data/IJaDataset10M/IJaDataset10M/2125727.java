package com.tensegrity.palowebviewer.modules.gui.client;

import com.google.gwt.user.client.ui.Widget;
import com.tensegrity.palowebviewer.modules.modelinterfaces.cleint.IApplication;

public interface IComponentFactory {

    public Widget createComponent(IApplication application);
}
