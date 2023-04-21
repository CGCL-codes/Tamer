package com.wizzer.m3g.viewer.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import com.wizzer.m3g.viewer.domain.SceneGraphManager;
import com.wizzer.m3g.viewer.domain.SceneGraphException;
import com.wizzer.m3g.viewer.ui.M3gFileView;
import com.wizzer.m3g.viewer.ui.M3gGraphView;

/**
 * Open a Mobile 3D Graphics file.
 * 
 * @author Mark Millard
 */
public class OpenAction extends Action implements IWorkbenchAction {

    /** The Open Action identifier/ */
    public static final String OPEN_ACTION_ID = "com.wizzer.m3g.viewer.ui.actions.openaction";

    /**
	 * Get the unique identifier for this action.
	 * 
	 * @return The identifier is returned.
	 */
    public String getId() {
        return OPEN_ACTION_ID;
    }

    /**
	 * Get the text for this action.
	 * <p>
	 * This method is associated with the TEXT property;
	 * property change events are reported when its value changes.
     * </p>
	 */
    public String getText() {
        return ("Open");
    }

    /**
	 * Runs this action.
	 */
    public void run() {
        String[] extensions = { "*.m3g", "*.*" };
        Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
        FileDialog dialog = new FileDialog(shell, SWT.OPEN);
        dialog.setFilterExtensions(extensions);
        String filename = dialog.open();
        if (filename != null) {
            SceneGraphManager scenegraph = SceneGraphManager.getInstance();
            try {
                scenegraph.setFile(filename);
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                IViewPart part = page.findView(M3gFileView.ID);
                ((M3gFileView) part).enableDisplay(true);
                part = page.findView(M3gGraphView.ID);
                ((M3gGraphView) part).enableDisplay(true);
            } catch (SceneGraphException ex) {
                MessageBox msg = new MessageBox(shell, SWT.ICON_ERROR);
                msg.setText("M3G Viewer Error");
                msg.setMessage(ex.getMessage() + "\nIs this a valid .m3g file?");
                msg.open();
            }
        }
    }

    /**
	 * Disposes of this action.
	 * <p>
	 * Once disposed, this action cannot be used. This operation has no
	 * effect if the action has already been disposed.
	 * </p>
	 */
    public void dispose() {
    }
}
