package co.fxl.gui.swing;

import javax.swing.JComponent;
import co.fxl.gui.api.IWidgetProvider;

interface ComponentParent {

    void add(JComponent component);

    void remove(JComponent component);

    JComponent getComponent();

    IWidgetProvider<?> lookupWidgetProvider(Class<?> interfaceClass);

    SwingDisplay lookupSwingDisplay();
}
