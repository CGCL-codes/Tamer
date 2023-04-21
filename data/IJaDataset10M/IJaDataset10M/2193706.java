package org.kumenya.ide.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.kumenya.openapi.Driver;

/**
 * 
 */
public class CQLExecuteAction extends AnAction {

    public void actionPerformed(AnActionEvent e) {
        System.out.println("CQLExecuteAction.actionPerformed");
        Project tProject = ProjectManager.getInstance().getOpenProjects()[0];
        Editor editor = FileEditorManager.getInstance(tProject).getSelectedTextEditor();
        String text = editor.getSelectionModel().getSelectedText();
        System.out.println("execute query: " + text);
        try {
            Driver driver = Driver.connect("admin:admin://localhost: ");
            driver.execute(text);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
