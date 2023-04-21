package org.wsmostudio.bpmo.ui.actions;

import java.util.List;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.wsmostudio.bpmo.model.WorkflowEntitiesContainer;
import org.wsmostudio.bpmo.ui.editor.BpmoEditor;
import org.wsmostudio.bpmo.ui.editor.layout.BPMOModelLayouter;

public class LayoutAction extends SelectionAction {

    public static final String ID = "Reset Layout";

    private BpmoEditor editor;

    public LayoutAction(BpmoEditor editor) {
        super(editor);
        this.editor = editor;
    }

    public String getId() {
        return ID;
    }

    public String getText() {
        return ID;
    }

    protected boolean calculateEnabled() {
        List<?> selection = getSelectedObjects();
        if (selection.size() == 0) {
            return true;
        }
        if (selection.size() == 1) {
            return selection.get(0) instanceof AbstractEditPart && ((AbstractEditPart) selection.get(0)).getModel() instanceof WorkflowEntitiesContainer;
        }
        return false;
    }

    public void run() {
        if (getSelectedObjects().size() == 1) {
            BPMOModelLayouter.doLayout((WorkflowEntitiesContainer) ((AbstractEditPart) getSelectedObjects().get(0)).getModel());
        } else {
            BPMOModelLayouter.doLayout(editor.getModel());
        }
        editor.getModel().notifyContentChanged();
    }
}
