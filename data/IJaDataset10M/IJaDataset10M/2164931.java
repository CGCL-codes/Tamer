package com.vividsolutions.jump.workbench.ui.plugin;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;

/**
 * Opens a TaskFrame when the Workbench starts up
 */
public class FirstTaskFramePlugIn extends AbstractPlugIn {

    public FirstTaskFramePlugIn() {
    }

    private ComponentListener componentListener;

    public void initialize(final PlugInContext context) throws Exception {
        componentListener = new ComponentAdapter() {

            public void componentShown(ComponentEvent e) {
                context.getWorkbenchFrame().addTaskFrame();
                context.getWorkbenchFrame().removeComponentListener(componentListener);
            }
        };
        context.getWorkbenchFrame().addComponentListener(componentListener);
    }
}
