package org.rcpquizengine.control.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.rcpquizengine.model.Folder;
import org.rcpquizengine.ui.views.QuizTreeView;

public class AddFolderHandler extends AbstractHandler implements IHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            String name = "";
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            QuizTreeView view = (QuizTreeView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.rcpquizengine.views.quizzes");
            Folder root = view.getRootFolder();
            if (root.isEncrypted()) {
                MessageDialog.openError(shell, "Error adding folder", "Quiz bank is locked");
                return null;
            }
            InputDialog dialog = new InputDialog(shell, "Add folder", "Enter folder name:", "", null);
            if (dialog.open() == Window.OK) {
                name = dialog.getValue();
            }
            if (!name.equals("")) {
                Folder folder = new Folder(name);
                view.addFolder(folder);
            }
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        return null;
    }
}
