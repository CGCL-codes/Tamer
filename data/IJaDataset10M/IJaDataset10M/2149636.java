package es.ulpgc.dis.heuristicide.rcp.ui.navigation.actions;

import java.util.Observable;
import java.util.Observer;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import es.ulpgc.dis.heuriskein.model.solver.MetaHeuristic;
import es.ulpgc.dis.heuristicide.control.*;
import es.ulpgc.dis.heuristicide.project.Project;
import es.ulpgc.dis.heuristicide.rcp.HeuriskeinApplication;
import es.ulpgc.dis.heuristicide.rcp.actions.ICommandIds;
import es.ulpgc.dis.heuristicide.rcp.actions.OpenEditorAction;
import es.ulpgc.dis.heuristicide.rcp.actions.SelectRepresentationAction;

public class NewMetaHeuristicAction extends Action implements Observer {

    private final IWorkbenchWindow window;

    public NewMetaHeuristicAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        setId(ICommandIds.CMD_NEW_METAHEURISTIC);
        if (HeuriskeinApplication.getProjectManager().getCurrentProject() != null) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
        HeuriskeinApplication.getProjectManager().addObserver(this);
    }

    public void run() {
        int i = 0;
        boolean finded = false;
        Project current = HeuriskeinApplication.getProjectManager().getCurrentProject();
        while (!finded) {
            if (current.getMetaheuristicList().contains(new MetaHeuristic("MetaHeuristic" + String.valueOf(i)))) {
                i++;
            } else {
                finded = true;
            }
        }
        NewProjectMember.createMetaheuristic("MetaHeuristic" + String.valueOf(i), "MetaHeuristic" + String.valueOf(i));
        MetaHeuristic meta = current.getMetaheuristic("MetaHeuristic" + String.valueOf(i));
        SelectRepresentationAction.run(window, meta);
        OpenEditorAction.run(window, meta);
        LoadSaveController.saveAll(current);
    }

    public void update(Observable o, Object arg) {
        if (HeuriskeinApplication.getProjectManager().getCurrentProject() != null) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }
}
