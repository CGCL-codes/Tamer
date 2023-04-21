package org.pr.usesystemdesktop.browse;

import java.io.File;
import java.io.IOException;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.*;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.pr.usesystemdesktop.open.OpenWithDesktopAction;

public final class BrowseAction extends OpenWithDesktopAction {

    @Override
    protected void performAction(Node[] activatedNodes) {
        if (activatedNodes != null) {
            if (activatedNodes.length == 1) {
                DataObject dataObject = activatedNodes[0].getLookup().lookup(DataObject.class);
                if (dataObject.getPrimaryFile() != null) {
                    FileObject obj = dataObject.getPrimaryFile();
                    File file = FileUtil.toFile(obj);
                    if (file != null && file.exists()) {
                        try {
                            _desktop.browse(file.toURI());
                            String message = NbBundle.getMessage(BrowseAction.class, "CTL_OpenWithDesktopAction.browsing.text", obj.getPath());
                            StatusDisplayer.getDefault().setStatusText(message);
                        } catch (IOException ex) {
                            String message = NbBundle.getMessage(BrowseAction.class, "CTL_OpenWithDesktopAction.browsing.io.error", obj.getPath());
                            StatusDisplayer.getDefault().setStatusText(message);
                            NotifyDescriptor.Exception exp = new NotifyDescriptor.Exception(ex, message);
                            DialogDisplayer.getDefault().notify(exp);
                        }
                    } else {
                        String message = NbBundle.getMessage(BrowseAction.class, "CTL_OpenWithDesktopAction.browsing.text", obj.getPath());
                        StatusDisplayer.getDefault().setStatusText(message);
                        NotifyDescriptor.Message msg = new NotifyDescriptor.Message(message, NotifyDescriptor.ERROR_MESSAGE);
                        DialogDisplayer.getDefault().notify(msg);
                    }
                }
            }
        }
    }

    @Override
    public boolean isDesktopActionEnabled() {
        boolean desktopActionEnabled = isDesktopEnabled();
        if (desktopActionEnabled) {
            desktopActionEnabled = java.awt.Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE);
        }
        return desktopActionEnabled;
    }

    public String getName() {
        return NbBundle.getMessage(BrowseAction.class, "CTL_BrowseAction");
    }
}
