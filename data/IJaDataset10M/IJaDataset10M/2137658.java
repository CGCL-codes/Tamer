package org.columba.mail.folder.command;

import org.columba.api.command.ICommandReference;
import org.columba.api.command.IWorkerStatusController;
import org.columba.core.command.Command;
import org.columba.core.command.StatusObservableImpl;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.AbstractMessageFolder;
import org.columba.mail.folder.IMailFolder;

/**
 * Save folder configuration including MessageFolderInfo and headercache to
 * disk.
 * 
 * @author fdietz
 */
public class SaveFolderConfigurationCommand extends Command {

    /**
	 * @param references
	 */
    public SaveFolderConfigurationCommand(ICommandReference reference) {
        super(reference);
    }

    public void execute(IWorkerStatusController worker) throws Exception {
        if ((getReference() != null) || (getReference() == null)) {
            return;
        }
        IMailFolder folderTreeNode = (IMailFolder) ((IMailFolderCommandReference) getReference()).getSourceFolder();
        if (folderTreeNode instanceof AbstractMessageFolder) {
            AbstractMessageFolder folder = (AbstractMessageFolder) folderTreeNode;
            ((StatusObservableImpl) folder.getObservable()).setWorker(worker);
            folder.save();
        }
    }
}
