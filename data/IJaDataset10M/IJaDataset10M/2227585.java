package org.columba.mail.folder.imap;

import java.io.IOException;
import javax.swing.Action;
import org.columba.api.command.ICommandReference;
import org.columba.api.command.IWorkerStatusController;
import org.columba.core.command.Command;
import org.columba.core.command.CommandCancelledException;
import org.columba.core.command.StatusObservableImpl;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.mailchecking.MailCheckingManager;

/**
 * Check for new messages in IMAPFolder.
 * 
 * 
 * @author fdietz
 */
public class CheckForNewMessagesCommand extends Command {

    IMAPFolder imapFolder;

    private Action action;

    private boolean triggerNotification;

    public CheckForNewMessagesCommand(ICommandReference reference) {
        super(reference);
        triggerNotification = false;
        IMailFolderCommandReference r = (IMailFolderCommandReference) getReference();
        imapFolder = (IMAPFolder) r.getSourceFolder();
        imapFolder.setMailboxSyncEnabled(false);
    }

    public CheckForNewMessagesCommand(Action action, ICommandReference reference) {
        super(reference);
        this.action = action;
        triggerNotification = true;
        IMailFolderCommandReference r = (IMailFolderCommandReference) getReference();
        imapFolder = (IMAPFolder) r.getSourceFolder();
        imapFolder.setMailboxSyncEnabled(false);
    }

    public void execute(IWorkerStatusController worker) throws Exception {
        ((StatusObservableImpl) imapFolder.getObservable()).setWorker(worker);
        int total = imapFolder.getMessageFolderInfo().getExists();
        Object[] uids = new Object[0];
        try {
            uids = imapFolder.synchronizeHeaderlist();
        } catch (IOException e) {
            imapFolder.setMailboxSyncEnabled(true);
            worker.cancel();
            throw new CommandCancelledException(e);
        }
        int newTotal = imapFolder.getMessageFolderInfo().getExists();
        if (triggerNotification && (newTotal != total)) {
            if (((IMAPRootFolder) imapFolder.getRootFolder()).getAccountItem().getImapItem().getBoolean("enable_sound")) {
                IMailFolderCommandReference ref = new MailFolderCommandReference(imapFolder, uids);
                MailCheckingManager.getInstance().fireNewMessageArrived(ref);
            }
        }
    }

    /**
	 * @see org.columba.api.command.Command#updateGUI()
	 */
    public void updateGUI() throws Exception {
        if (action != null) action.setEnabled(true);
    }
}
