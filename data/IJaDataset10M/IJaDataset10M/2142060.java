package midnightmarsbrowser.actions;

import midnightmarsbrowser.application.Activator;
import midnightmarsbrowser.editors.MMBEditorBase;
import midnightmarsbrowser.model.ViewerSettings;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;

public class IncludeDriveImagesAction extends BaseViewerAction {

    public static final String ID = "midnightmarsbrowser.actions.ShowDriveImagesAction";

    public IncludeDriveImagesAction(IWorkbenchWindow window) {
        super(window, Action.AS_CHECK_BOX);
        setId(ID);
        setText("Include Drive Images");
        setImageDescriptor(Activator.getImageDescriptor("icons/drive_images.gif"));
    }

    public void run() {
        ViewerSettings settings = currentViewer.getViewerSettings();
        settings.excludeDriveImages = !isChecked();
        currentViewer.setViewerSettings(settings, true);
    }

    public void currentEditorChanged(MMBEditorBase newEditor) {
        super.currentEditorChanged(newEditor);
        if (currentViewer != null) {
            ViewerSettings settings = currentViewer.getViewerSettings();
            setChecked(!settings.excludeDriveImages);
        }
    }
}
