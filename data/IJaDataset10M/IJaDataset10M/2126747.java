package ti.plato.menu.edit;

import ti.plato.ui.views.manager.util.IPerspectiveConsts;

public class EditRedoAction extends AEditAction {

    private static EditRedoAction _action = new EditRedoAction(IPerspectiveConsts.ACTION_DEFINITION_ID_EDIT_REDO);

    public static EditRedoAction getDefault() {
        return _action;
    }

    public EditRedoAction(String actionDefinitionId) {
        super(actionDefinitionId);
    }
}
