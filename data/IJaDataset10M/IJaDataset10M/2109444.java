package guijavacommander.actions;

import guijavacommander.FilePanel;
import guijavacommander.JavaCommander;
import guijavacommander.tasks.Task;
import guijavacommander.tasks.MoveFilesTask;
import guijavacommander.tasks.ThreadStatusDialog;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * User: Deady
 * Date: 16.07.2009
 * Time: 11:47:43
 */
public class MoveFilesAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        JavaCommander cmd = JavaCommander.instance;
        FilePanel active = cmd.getActivePanel();
        FilePanel inactive = cmd.getInactivePanel();
        File[] selected = active.getSelectedFiles();
        File dest = inactive.getCurrentFolder();
        if (JOptionPane.showConfirmDialog(cmd, "Move " + selected.length + " files to " + dest + "?", "Attention", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Task task = new MoveFilesTask(dest, selected);
            new ThreadStatusDialog(cmd, task).execute();
        }
        active.refresh();
        inactive.refresh();
    }
}
