package com.ibm.tuningfork.core.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import com.ibm.tuningfork.core.TuningForkCorePlugin;

/**
 * Action to configure a figure with a wizard
 */
public final class ConfigureFigureAction extends Action {

    private static final String DEFINITION_ID = "com.ibm.tuningfork.core.action.ConfigureFigureAction";

    private static final ImageDescriptor IMAGE_DESCRIPTOR = TuningForkCorePlugin.getImageDescriptor("icons/dfb_from_eclipse_failures.gif");

    private final Action runner;

    public ConfigureFigureAction(String figureName, Action runner) {
        this.runner = runner;
        setActionDefinitionId(DEFINITION_ID);
        setImageDescriptor(IMAGE_DESCRIPTOR);
        setText("Configure...");
        setToolTipText("Configure " + figureName + "...");
        setEnabled(true);
    }

    public final void run() {
        runner.run();
    }
}
