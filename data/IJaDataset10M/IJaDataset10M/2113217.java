package ti.plato.menu.edit;

import ti.plato.ui.views.manager.util.IPerspectiveConsts;

public class EditPasteAction extends AEditAction {

    private static EditPasteAction _action = new EditPasteAction(IPerspectiveConsts.ACTION_DEFINITION_ID_EDIT_PASTE);

    public static EditPasteAction getDefault() {
        return _action;
    }

    public EditPasteAction(String actionDefinitionId) {
        super(actionDefinitionId);
    }
}
