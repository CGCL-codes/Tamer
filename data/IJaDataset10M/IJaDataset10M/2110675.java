package es.ulpgc.dis.heuristicide.rcp.actions;

import java.util.ArrayList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import es.ulpgc.dis.heuriskein.model.solver.Heuristic;
import es.ulpgc.dis.heuristicide.rcp.ui.metaheuristic.MetaheuristicEditor;

public class MoveDownAction extends Action implements IEditorActionDelegate {

    private MetaheuristicEditor part;

    public MoveDownAction(String name, IWorkbenchWindow window) {
        super(name);
        setEnabled(false);
    }

    public void run() {
        ArrayList<Heuristic> heuristicList = part.getMetaheuristic().getHeuristics();
        Heuristic holder = part.getHeuristicSelected();
        int i = heuristicList.indexOf(holder);
        heuristicList.set(i, heuristicList.get(i + 1));
        heuristicList.set(i + 1, holder);
        part.update();
    }

    public void setActiveEditor(IAction arg0, IEditorPart part) {
        if (this.part != null) {
            this.part.removeHeuristicListener(this);
        }
        this.part = (MetaheuristicEditor) part;
        this.part.addHeuristicListener(this);
    }

    public void run(IAction arg0) {
    }

    public void selectionChanged(IAction arg0, ISelection selection) {
        if (part == null) {
            return;
        }
        Heuristic heuristic = part.getHeuristicSelected();
        if (heuristic != null) {
            int index = part.getMetaheuristic().getHeuristics().indexOf(heuristic);
            if (index < part.getMetaheuristic().getHeuristics().size() - 1) {
                setEnabled(true);
            } else {
                setEnabled(false);
            }
        } else {
            setEnabled(false);
        }
    }
}
