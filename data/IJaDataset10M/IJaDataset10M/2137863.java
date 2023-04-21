package org.columba.mail.gui.tree.command;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.columba.api.command.ICommandReference;
import org.columba.api.command.IWorkerStatusController;
import org.columba.core.command.Command;
import org.columba.core.command.Worker;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.FolderFactory;
import org.columba.mail.folder.IMailFolder;

/**
 * @author fdietz
 * 
 */
public class CreateAndSelectSubFolderCommand extends Command {

    private IMailFolder parentFolder;

    private boolean success;

    private JTree tree;

    private IMailFolder childFolder;

    public CreateAndSelectSubFolderCommand(JTree tree, ICommandReference reference) {
        super(reference);
        success = true;
        this.tree = tree;
    }

    /**
	 * @see org.columba.api.command.Command#updateGUI()
	 */
    public void updateGUI() throws Exception {
        if (success) {
            TreeNode[] nodes = childFolder.getPath();
            tree.setSelectionPath(new TreePath(nodes));
        }
    }

    /**
	 * @see org.columba.api.command.Command#execute(Worker)
	 */
    public void execute(IWorkerStatusController worker) throws Exception {
        parentFolder = (IMailFolder) ((IMailFolderCommandReference) getReference()).getSourceFolder();
        String name = ((IMailFolderCommandReference) getReference()).getFolderName();
        try {
            childFolder = FolderFactory.getInstance().createDefaultChild(parentFolder, name);
            if (childFolder == null) {
                success = false;
            }
        } catch (Exception ex) {
            success = false;
            throw ex;
        }
    }
}
