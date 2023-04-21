package com.peterhi.app;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;

public interface ActionConfigurer {

    void toolBar(ToolBar toolBar);

    void menuBar(Menu menu);
}
