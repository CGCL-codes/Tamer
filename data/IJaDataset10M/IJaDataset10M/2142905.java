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
import org.rcpquizengine.model.Quiz;
import org.rcpquizengine.ui.views.ProgressReportView;
import org.rcpquizengine.ui.views.QuizTreeView;

public class AddQuizHandler extends AbstractHandler implements IHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            String name = "";
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            QuizTreeView view = (QuizTreeView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("org.rcpquizengine.views.quizzes");
            Folder root = view.getRootFolder();
            if (root.isEncrypted()) {
                MessageDialog.openError(shell, "Error adding quiz", "Quiz bank is locked");
                return null;
            }
            InputDialog dialog = new InputDialog(shell, "Add quiz", "Enter quiz name:", "", null);
            if (dialog.open() == Window.OK) {
                name = dialog.getValue();
            }
            if (!name.equals("")) {
                Quiz quiz = new Quiz(name, 0, 0);
                view.addQuiz(quiz);
                ProgressReportView progressReportView = (ProgressReportView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(ProgressReportView.ID);
                progressReportView.refreshModel();
            }
        } catch (PartInitException e) {
            e.printStackTrace();
        }
        return null;
    }
}
