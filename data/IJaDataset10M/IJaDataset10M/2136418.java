package filemanager.context.impl;

import filemanager.context.ContextAction;
import filemanager.vfs.WriteableFileIfc;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author sahaqiel
 */
public class RenameAction implements ContextAction {

    private WriteableFileIfc fileIfc;

    public RenameAction(WriteableFileIfc fileIfc) {
        this.fileIfc = fileIfc;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String newName = JOptionPane.showInputDialog(null, "Please input a new file name", "Rename file", JOptionPane.QUESTION_MESSAGE);
        if (newName != null) {
            fileIfc.rename(newName);
        }
    }

    @Override
    public int getIndex() {
        return 6;
    }

    @Override
    public String getName() {
        return "Rename";
    }

    @Override
    public String getToolTip() {
        return "";
    }
}
