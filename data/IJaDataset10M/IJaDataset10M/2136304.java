package es.ulpgc.dis.heuristicide.rcp.ui.metaheuristic;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionGroup;
import es.ulpgc.dis.heuristicide.rcp.actions.AddHeuristicAction;
import es.ulpgc.dis.heuristicide.rcp.actions.RemoveHeuristicAction;
import es.ulpgc.dis.heuristicide.rcp.actions.SelectRepresentationAction;

public class MetaheuristicActionGroup extends ActionGroup {

    private RemoveHeuristicAction removeHeuristic;

    private AddHeuristicAction addHeuristic;

    private SelectRepresentationAction selectRepresentation;

    public MetaheuristicActionGroup(IWorkbenchWindow window) {
        addHeuristic = new AddHeuristicAction("Add Heuristic", window);
        removeHeuristic = new RemoveHeuristicAction("Remove Heuristic", window);
        selectRepresentation = new SelectRepresentationAction("Select Representation", window);
    }

    public void fillContextMenu(IMenuManager menuManager) {
        menuManager.add(selectRepresentation);
        menuManager.add(addHeuristic);
        menuManager.add(removeHeuristic);
    }

    public void fillActionBars(IActionBars actionBars) {
    }
}
