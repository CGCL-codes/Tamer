package fr.insa.rennes.pelias.pcreator.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import fr.insa.rennes.pelias.pcreator.Application;
import fr.insa.rennes.pelias.pcreator.views.ChainNavigator;
import fr.insa.rennes.pelias.pcreator.views.ServiceNavigator;

/**
 * 
 * @author Otilia Damian
 * @modified by Kévin Le Corre
 *
 */
public class DisconnectRepositoriesAction extends Action implements IWorkbenchWindowActionDelegate {

    public static final String ID = "fr.insa.rennes.pelias.pcreator.fr.insa.rennes.pelias.fr.insa.rennes.pelias.disconnect";

    @SuppressWarnings("unused")
    private IWorkbenchWindow window;

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    public void run(IAction action) {
        Application.disconnect();
        IWorkbenchPage p = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        p.activate(p.findView(ServiceNavigator.ID));
        p.activate(p.findView(ChainNavigator.ID));
        ((ServiceNavigator) window.getActivePage().findView(ServiceNavigator.ID)).getTreeViewer().refresh();
        ((ChainNavigator) window.getActivePage().findView(ChainNavigator.ID)).getTreeViewer().refresh();
    }

    public void selectionChanged(IAction action, ISelection selection) {
        boolean connected = Application.isConnected();
        action.setEnabled(connected);
    }
}
