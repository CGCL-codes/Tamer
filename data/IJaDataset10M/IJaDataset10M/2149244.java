package es.ulpgc.dis.heuristicide.rcp.ui.details.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import es.ulpgc.dis.heuriskein.model.solver.ExecutionDebug;
import es.ulpgc.dis.heuristicide.rcp.actions.Images;
import es.ulpgc.dis.heuristicide.rcp.ui.details.PopulationsView;

public class Backward extends Action {

    private IWorkbenchWindow window;

    private int step;

    public Backward(String str, IWorkbenchWindow window, int step) {
        super(str);
        this.window = window;
        setImageDescriptor(Images.createImageDescriptorFor("icons/previous.gif"));
        if (step == 1) setToolTipText("Previous " + String.valueOf(step) + " Populations"); else setToolTipText("Previous " + String.valueOf(step) + "% of Populations");
        this.step = step;
    }

    public Backward(String str, IWorkbenchWindow window) {
        super(str);
        this.window = window;
        setToolTipText("Previous Population");
        setImageDescriptor(Images.createImageDescriptorFor("icons/previous.gif"));
        step = 1;
    }

    public void run() {
        ((PopulationsView) window.getActivePage().getActivePart()).previous(step);
    }
}
