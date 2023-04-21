package org.chartsy.main.actions;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;

/**
 *
 * @author Viorel
 */
@ActionID(id = "org.chartsy.main.actions.ExportImageAction", category = "Chart")
@ActionRegistration(displayName = "ExportImageAction.name", iconInMenu = true, asynchronous = false)
@ActionReferences({ @ActionReference(path = "Menu/Chart", position = 250), @ActionReference(path = "Toolbars/Chart", position = 250) })
public class ExportImageAction extends AbstractChartAction {

    public void performAction() {
        if (chartTopComponent != null) {
        }
    }
}
