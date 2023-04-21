package gov.ornl.nice.niceclient;

import java.io.File;
import java.net.URL;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.internal.util.BundleUtility;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class SwitchWorkspaceAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;

    private static final String ID = "gov.ornl.nice.niceclient.switchWSAction";

    private ChooseWorkspaceDialog dialog;

    public SwitchWorkspaceAction(IWorkbenchWindow window) {
        this.window = window;
        this.setId(ID);
        this.setText("&Switch Workspace");
        this.setToolTipText("Switch to a new Workspace location...");
        this.window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        window.getSelectionService().removeSelectionListener(this);
    }

    @Override
    public void run() {
        Bundle bundle = FrameworkUtil.getBundle(getClass());
        URL fullPath = BundleUtility.find(bundle, "icons/Cool-icon.png");
        ImageDescriptor descriptor = ImageDescriptor.createFromURL(fullPath);
        Image icon = descriptor.createImage();
        dialog = new ChooseWorkspaceDialog(true, icon);
        int click = dialog.open();
        if (click == Window.CANCEL) {
            return;
        } else {
            MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Switch Workspace", "The client will now restart with the new workspace.");
            PlatformUI.getWorkbench().restart();
        }
        return;
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    }
}
