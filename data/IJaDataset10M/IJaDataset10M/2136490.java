package uk.ac.bolton.archimate.editor.views.tree.actions;

import java.io.IOException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchCommandConstants;
import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.views.tree.ITreeModelView;
import uk.ac.bolton.archimate.model.IArchimateModel;

/**
 * Save Model Action
 * 
 * @author Phillip Beauvoir
 */
public class SaveModelAction extends ViewerAction {

    private ITreeModelView fView;

    public SaveModelAction(ITreeModelView view) {
        super(view.getSelectionProvider());
        setText(Messages.SaveModelAction_0);
        fView = view;
        setActionDefinitionId(IWorkbenchCommandConstants.FILE_SAVE);
    }

    @Override
    public void run() {
        IArchimateModel model = getModel();
        if (model != null) {
            try {
                IEditorModelManager.INSTANCE.saveModel(model);
            } catch (IOException ex) {
                MessageDialog.openError(fView.getSite().getShell(), Messages.SaveModelAction_1, ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void update(IStructuredSelection selection) {
        setEnabled(IEditorModelManager.INSTANCE.isModelDirty(getModel()));
    }

    private IArchimateModel getModel() {
        return (IArchimateModel) fView.getAdapter(IArchimateModel.class);
    }
}
