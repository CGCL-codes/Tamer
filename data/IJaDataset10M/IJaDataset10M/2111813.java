package eu.redseeds.engine.navigator.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import eu.redseeds.engine.navigator.dnd.SCLElementTransfer;
import eu.redseeds.scl.model.rsl.rslrequirements.requirementsspecifications.RequirementDTO;

public class CutSCLElementAction extends Action {

    protected Clipboard clipboard;

    protected StructuredViewer viewer;

    public CutSCLElementAction(StructuredViewer viewer, Clipboard clipboard) {
        super("Cut");
        this.viewer = viewer;
        this.clipboard = clipboard;
    }

    public void run() {
        IStructuredSelection sel = (IStructuredSelection) viewer.getSelection();
        Object[] objects = sel.toArray();
        clipboard.setContents(new Object[] { objects }, new Transfer[] { SCLElementTransfer.getInstance() });
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof RequirementDTO) {
                ((RequirementDTO) objects[i]).setParent(null);
            }
        }
        viewer.refresh();
    }
}
