package visualbiology.sbmlEditor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import visualbiology.sbmlEditor.actions.EditModelAction;
import visualbiology.sbmlEditor.actions.EditModelRetarget;
import visualbiology.sbmlEditor.actions.ExportBioPEPAAction;
import visualbiology.sbmlEditor.actions.ExportBioPEPARetarget;
import visualbiology.sbmlEditor.actions.ExportSbmlAction;
import visualbiology.sbmlEditor.actions.ExportSbmlRetarget;
import visualbiology.sbmlEditor.actions.FindRetarget;
import visualbiology.sbmlEditor.actions.RedoRetarget;
import visualbiology.sbmlEditor.actions.UndoRetarget;
import visualbiology.sbmlEditor.actions.ValidateSBMLAction;
import visualbiology.sbmlEditor.actions.ValidateSBMLRetarget;

public class SbmlEditorContributor extends ActionBarContributor {

    @Override
    protected void buildActions() {
        addRetargetAction(new EditModelRetarget());
        addRetargetAction(new ExportSbmlRetarget());
        addRetargetAction(new ExportBioPEPARetarget());
        addRetargetAction(new ValidateSBMLRetarget());
        addRetargetAction(new UndoRetarget());
        addRetargetAction(new RedoRetarget());
        addRetargetAction(new FindRetarget());
    }

    @Override
    protected void declareGlobalActionKeys() {
    }

    @Override
    public void contributeToToolBar(IToolBarManager toolBarManager) {
        toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
        toolBarManager.add(getAction(ActionFactory.REDO.getId()));
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(ActionFactory.FIND.getId()));
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(ExportSbmlAction.ID));
        toolBarManager.add(getAction(ExportBioPEPAAction.ID));
        toolBarManager.add(getAction(ValidateSBMLAction.ID));
    }

    @Override
    public void contributeToMenu(IMenuManager menuManager) {
        MenuManager sbmlSubMenu = new MenuManager("SBML");
        sbmlSubMenu.add(getAction(EditModelAction.ID));
        sbmlSubMenu.add(new Separator());
        sbmlSubMenu.add(getAction(ExportSbmlAction.ID));
        sbmlSubMenu.add(getAction(ExportBioPEPAAction.ID));
        sbmlSubMenu.add(new Separator());
        sbmlSubMenu.add(getAction(ValidateSBMLAction.ID));
        menuManager.insertAfter("edit", sbmlSubMenu);
    }
}
