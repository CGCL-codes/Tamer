package org.jsens.project.internal;

import java.io.File;
import java.io.FileNotFoundException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jsens.project.DataHolder;

public class FileOpenHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        Shell parentShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        FileDialog fileDialog = new FileDialog(parentShell, SWT.OPEN | SWT.SINGLE);
        fileDialog.setText("Datei öffnen...");
        fileDialog.setFilterExtensions(new String[] { "*.jsens" });
        fileDialog.setFilterNames(new String[] { "JSens-Dateien" });
        if (fileDialog.open() != null) {
            String fileName = fileDialog.getFilterPath() + File.separatorChar + fileDialog.getFileName();
            try {
                DataHolder.openFromFile(new File(fileName));
            } catch (FileNotFoundException e) {
                MessageBox messageBox = new MessageBox(parentShell, SWT.ICON_ERROR | SWT.OK);
                messageBox.setText("Datei öffnen");
                String msg = "Datei existiert nicht!";
                messageBox.setMessage(msg);
                messageBox.open();
                Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, msg));
            } catch (Exception e) {
                MessageBox messageBox = new MessageBox(parentShell, SWT.ICON_ERROR | SWT.OK);
                messageBox.setText("Datei öffnen");
                String msg = "{0} ist keine JSens-Datei!";
                messageBox.setMessage(msg);
                messageBox.open();
                Activator.getDefault().getLog().log(new Status(Status.ERROR, Activator.PLUGIN_ID, msg));
            }
        }
        return null;
    }
}
