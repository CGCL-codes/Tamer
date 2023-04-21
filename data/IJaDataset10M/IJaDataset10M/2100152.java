package es.ulpgc.dis.heuristicide.rcp.actions;

import java.util.Observable;
import java.util.Observer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import es.ulpgc.dis.heuriskein.model.solver.Heuristic;
import es.ulpgc.dis.heuristicide.rcp.ui.metaheuristic.MetaheuristicEditor;

public class AddHeuristicAction extends Action implements IEditorActionDelegate, Observer {

    private MetaheuristicEditor part;

    public AddHeuristicAction(String name, IWorkbenchWindow window) {
        super(name);
        setId(ICommandIds.CMD_ADDHEURISTIC);
        setToolTipText("Add heuristic step to the current Metaheuristic");
        setImageDescriptor(Images.createImageDescriptorFor("icons/add.png"));
    }

    public void run() {
        Heuristic heuristic = new Heuristic();
        part.getMetaheuristic().addHeuristic(heuristic);
        part.addHeuristic(heuristic);
    }

    public void setActiveEditor(IAction arg0, IEditorPart part) {
        if (this.part != null) {
            this.part.getMetaheuristic().deleteObserver(this);
        }
        this.part = (MetaheuristicEditor) part;
        this.part.getMetaheuristic().addObserver(this);
        update(null, null);
    }

    public void run(IAction arg0) {
    }

    public void selectionChanged(IAction arg0, ISelection arg1) {
    }

    public void update(Observable o, Object arg) {
        if (part.getMetaheuristic().getType() != null) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }
}
