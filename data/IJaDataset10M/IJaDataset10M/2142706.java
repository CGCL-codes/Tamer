package com.horstmann.violet.eclipseplugin.tools;

import java.net.URL;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;

public class EclipseUtils {

    /**
     * Open a UML Diagram file (that will create a new editor instance)
     * 
     * @param arg0
     * @param display
     */
    public static void openUMLDiagram(URL arg0, Display display) {
        final IPath path = new Path(arg0.getPath());
        display.asyncExec(new Runnable() {

            public void run() {
                IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                IFile file = IDEWorkbenchPlugin.getPluginWorkspace().getRoot().getFileForLocation(path);
                try {
                    IDE.openEditor(page, file, true);
                } catch (PartInitException e) {
                }
            }
        });
    }
}
