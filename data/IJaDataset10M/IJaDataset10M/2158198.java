package es.ulpgc.dis.heuristicide.rcp.ui.details.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import es.ulpgc.dis.heuriskein.model.solver.ExecutionDebug;
import es.ulpgc.dis.heuristicide.rcp.actions.Images;
import es.ulpgc.dis.heuristicide.rcp.ui.details.PopulationsView;

public class Beginning extends Action {

    private IWorkbenchWindow window;

    public Beginning(String str, IWorkbenchWindow window) {
        super(str);
        this.window = window;
        setToolTipText("First Population");
        setImageDescriptor(Images.createImageDescriptorFor("icons/first.gif"));
    }

    public void run() {
        ((PopulationsView) window.getActivePage().getActivePart()).first();
    }
}
