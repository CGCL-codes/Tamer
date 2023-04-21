package de.inovox.pipeline.monitor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import de.inovox.pipeline.monitor.View;
import de.inovox.pipeline.plugin.PluginState;
import de.inovox.pipeline.service.PluginData;

/**
 * @author Carsten Burghardt
 * @version $Id: PluginStartAction.java 103 2007-01-08 19:13:13Z carsten $
 */
public class PluginStartAction extends GenericAction implements IViewActionDelegate {

    private View view;

    /**
     * Constructor
     */
    public PluginStartAction() {
    }

    /**
     * Start the plugin
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        PluginData plugin = view.getSelectedPlugin();
        if (plugin != null) {
            view.getPipeline().startPlugin(plugin.getId());
        }
    }

    /**
     * Activate the action if a plugin is selected
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        if (view.getSelectedPlugin() != null) {
            int state = view.getSelectedPlugin().getState();
            if (state == PluginState.STATE_STOPPED) {
                action.setEnabled(true);
                return;
            }
        }
        action.setEnabled(false);
    }

    public void init(IViewPart view) {
        this.view = (View) view;
        init(view.getSite().getWorkbenchWindow());
    }
}
