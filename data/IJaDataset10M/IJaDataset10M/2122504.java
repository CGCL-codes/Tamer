package midnightmarsbrowser.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import midnightmarsbrowser.application.Application;
import midnightmarsbrowser.application.UpdateParams;
import midnightmarsbrowser.views.ImagesView;

public class UpdateMetadataAction extends Action implements IWorkbenchAction {

    private final IWorkbenchWindow window;

    public static final String ID = "midnightmarsbrowser.actions.UpdateMetadataAction";

    public UpdateMetadataAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("Update Metadata");
        setToolTipText("Download latest metadata");
    }

    public void dispose() {
    }

    public void run() {
        UpdateParams params = new UpdateParams();
        params.mode = UpdateParams.MODE_UPDATE;
        params.updateMetadata = true;
        Application.getWorkspace().startUpdateTask(params, window);
    }
}
