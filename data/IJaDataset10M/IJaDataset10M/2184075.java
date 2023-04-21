package midnightmarsbrowser.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import midnightmarsbrowser.application.Application;
import midnightmarsbrowser.application.UpdateParams;
import midnightmarsbrowser.views.ImagesView;

public class GetLatestImagesAction extends Action implements IWorkbenchAction {

    private final IWorkbenchWindow window;

    public static final String ID = "midnightmarsbrowser.actions.GetLatestImagesAction";

    public GetLatestImagesAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("Get Latest Images");
        setToolTipText("Download latest images.");
        setAccelerator('u' + SWT.MOD1);
    }

    public void dispose() {
    }

    public void run() {
        UpdateParams params = new UpdateParams();
        params.mode = UpdateParams.MODE_CHECK;
        params.autoUpdate = true;
        Application.getWorkspace().startUpdateTask(params, window);
    }
}
