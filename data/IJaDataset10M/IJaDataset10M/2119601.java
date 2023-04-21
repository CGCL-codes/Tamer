package edu.asu.vspace.timetree.ui;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import edu.asu.vspace.timetree.datamodel.Species;
import edu.asu.vspace.timetree.vspace.DiagramCreator;

public class CreateDiagramAction implements IViewActionDelegate {

    private TimeTreeExplorer explorer;

    public void run(IAction action) {
        if (explorer == null) return;
        Species tree = explorer.getCurrentTree();
        DiagramCreator creator = new DiagramCreator();
        creator.createDiagram(tree);
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void init(IViewPart view) {
        if (view instanceof TimeTreeExplorer) explorer = (TimeTreeExplorer) view;
    }
}
