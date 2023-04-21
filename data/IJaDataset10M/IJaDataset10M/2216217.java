package org.codemon.gui.listener;

import org.codemon.gui.CodemonGui;
import org.codemon.gui.CodemonProjectPropertiesGui;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * The Selection Listener for ProjectProperties.
 * @author Xu Mingming(������)
 * @see CodemonGui
 */
public class ProjectPropertiesListener extends SelectionAdapter {

    private CodemonGui demo;

    public ProjectPropertiesListener(CodemonGui demo) {
        this.demo = demo;
    }

    public void widgetSelected(SelectionEvent e) {
        if (demo.getCurrentProject() == null) {
            return;
        }
        CodemonProjectPropertiesGui gui = new CodemonProjectPropertiesGui(demo);
        gui.go();
    }
}
