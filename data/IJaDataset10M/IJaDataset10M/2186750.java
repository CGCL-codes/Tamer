package org.columba.mail.folder.command;

import java.util.ArrayList;
import java.util.List;
import org.columba.api.command.ICommand;
import org.columba.api.command.ICommandReference;
import org.columba.api.command.IWorkerStatusController;
import org.columba.core.command.Command;
import org.columba.core.command.CommandProcessor;
import org.columba.core.command.StatusObservableImpl;
import org.columba.core.command.Worker;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.config.AccountItem;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.RootFolder;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.spam.command.CommandHelper;
import org.columba.mail.spam.command.LearnMessageAsHamCommand;
import org.columba.mail.spam.command.LearnMessageAsSpamCommand;
import org.columba.ristretto.message.Flags;

/**
 * Toggle flag.
 * <p>
 * Creates two sets of messages and uses {@link MarkMessageCommand}, which does
 * the flag change.
 * <p>
 * Additionally, if message is marked as spam or non-spam the bayesian filter is
 * trained.
 * 
 * @see MarkMessageCommand
 * @author fdietz
 */
public class ToggleMarkCommand extends Command {

    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger("org.columba.mail.folder.command");

    private IWorkerStatusController worker;

    private List<MarkMessageCommand> commandList;

    /**
	 * Constructor for ToggleMarkCommand.
	 * 
	 * @param frameMediator
	 * @param references
	 */
    public ToggleMarkCommand(ICommandReference reference) {
        super(reference);
        commandList = new ArrayList<MarkMessageCommand>();
    }

    /**
	 * @see org.columba.api.command.Command#execute(Worker)
	 */
    public void execute(IWorkerStatusController worker) throws Exception {
        this.worker = worker;
        IMailFolderCommandReference r = (IMailFolderCommandReference) getReference();
        Object[] uids = r.getUids();
        IMailbox srcFolder = (IMailbox) r.getSourceFolder();
        ((StatusObservableImpl) srcFolder.getObservable()).setWorker(worker);
        int markVariant = r.getMarkVariant();
        List<Object> list1 = new ArrayList<Object>();
        List<Object> list2 = new ArrayList<Object>();
        for (int j = 0; j < uids.length; j++) {
            Flags flags = srcFolder.getFlags(uids[j]);
            boolean result = false;
            if (markVariant == MarkMessageCommand.MARK_AS_READ) {
                if (flags.getSeen()) result = true;
            } else if (markVariant == MarkMessageCommand.MARK_AS_FLAGGED) {
                if (flags.getFlagged()) result = true;
            } else if (markVariant == MarkMessageCommand.MARK_AS_EXPUNGED) {
                if (flags.getDeleted()) result = true;
            } else if (markVariant == MarkMessageCommand.MARK_AS_ANSWERED) {
                if (flags.getAnswered()) result = true;
            } else if (markVariant == MarkMessageCommand.MARK_AS_DRAFT) {
                if (flags.getDraft()) result = true;
            } else if (markVariant == MarkMessageCommand.MARK_AS_SPAM) {
                boolean spam = ((Boolean) srcFolder.getAttribute(uids[j], "columba.spam")).booleanValue();
                if (spam) result = true;
            }
            if (result) list1.add(uids[j]); else list2.add(uids[j]);
        }
        MailFolderCommandReference ref = null;
        if (list1.size() > 0) {
            ref = new MailFolderCommandReference(srcFolder, list1.toArray());
            ref.setMarkVariant(-markVariant);
            MarkMessageCommand c = new MarkMessageCommand(ref);
            commandList.add(c);
            c.execute(worker);
            if ((markVariant == MarkMessageCommand.MARK_AS_SPAM) || (markVariant == MarkMessageCommand.MARK_AS_NOTSPAM)) {
                processSpamFilter(uids, srcFolder, -markVariant);
            }
        }
        if (list2.size() > 0) {
            ref = new MailFolderCommandReference(srcFolder, list2.toArray());
            ref.setMarkVariant(markVariant);
            MarkMessageCommand c = new MarkMessageCommand(ref);
            commandList.add(c);
            c.execute(worker);
            if ((markVariant == MarkMessageCommand.MARK_AS_SPAM) || (markVariant == MarkMessageCommand.MARK_AS_NOTSPAM)) {
                processSpamFilter(uids, srcFolder, markVariant);
            }
        }
    }

    /**
	 * Train spam filter.
	 * <p>
	 * Move message to specified folder or delete message immediately based on
	 * account configuration.
	 * 
	 * @param uids
	 *            message uid
	 * @param srcFolder
	 *            source folder
	 * @param markVariant
	 *            mark variant (spam/not spam)
	 * @throws Exception
	 */
    private void processSpamFilter(Object[] uids, IMailbox srcFolder, int markVariant) throws Exception {
        worker.setDisplayText("Training messages...");
        worker.setProgressBarMaximum(uids.length);
        for (int j = 0; j < uids.length; j++) {
            worker.setDisplayText("Training messages...");
            worker.setProgressBarMaximum(uids.length);
            worker.setProgressBarValue(j);
            if (worker.cancelled()) {
                break;
            }
            AccountItem item = CommandHelper.retrieveAccountItem(srcFolder, uids[j]);
            if (item == null) continue;
            if (item.getSpamItem().isEnabled() == false) continue;
            LOG.info("learning uid=" + uids[j]);
            IMailFolderCommandReference ref = new MailFolderCommandReference(srcFolder, new Object[] { uids[j] });
            ICommand c = null;
            if (markVariant == MarkMessageCommand.MARK_AS_SPAM) c = new LearnMessageAsSpamCommand(ref); else c = new LearnMessageAsHamCommand(ref);
            c.execute(worker);
            if (markVariant == MarkMessageCommand.MARK_AS_NOTSPAM) continue;
            if (item.getSpamItem().isMoveMessageWhenMarkingEnabled() == false) continue;
            if (item.getSpamItem().isMoveTrashSelected() == false) {
                IMailFolder destFolder = FolderTreeModel.getInstance().getFolder(item.getSpamItem().getMoveCustomFolder());
                MailFolderCommandReference ref2 = new MailFolderCommandReference(srcFolder, destFolder, new Object[] { uids[j] });
                CommandProcessor.getInstance().addOp(new MoveMessageCommand(ref2));
            } else {
                IMailbox trash = (IMailbox) ((RootFolder) srcFolder.getRootFolder()).getTrashFolder();
                MailFolderCommandReference ref2 = new MailFolderCommandReference(srcFolder, trash, new Object[] { uids[j] });
                CommandProcessor.getInstance().addOp(new MoveMessageCommand(ref2));
            }
        }
    }
}
