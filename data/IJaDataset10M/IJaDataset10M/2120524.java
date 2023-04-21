package net.sf.mailsomething.mail.actions;

import net.sf.mailsomething.mail.ImapAccount;
import net.sf.mailsomething.mail.MailAccount;
import net.sf.mailsomething.mail.MailAction;
import net.sf.mailsomething.mail.Mailbox;
import net.sf.mailsomething.util.ProgressListener;

/**
 * @author Stig tanggaard
 * @since 2005-03-20
 * 
 **/
public class RenameMailboxAction implements MailAction {

    Mailbox mailbox;

    String nPath, nName;

    ImapAccount mailAccount;

    public RenameMailboxAction(Mailbox mailbox, String nName) {
        this.mailbox = mailbox;
        this.nName = nName;
        this.nPath = mailbox.getParentMailbox().getPath() + '/' + nName;
    }

    public void invoke() {
        int selectResult = mailAccount.getController().selectMailbox(mailbox.getPath());
        if (selectResult == -1) {
            selectResult = mailAccount.getController().selectMailbox(nPath);
            if (selectResult == 1) {
                return;
            }
            return;
        }
        selectResult = mailAccount.getController().selectMailbox(nPath);
        if (selectResult == 1) {
            return;
        }
        if (mailAccount.getController().renameMailbox(mailbox.getPath(), nPath)) {
            mailbox.rename(nName);
        }
    }

    public void undo() {
    }

    public void setMailAccount(MailAccount account) {
    }

    public void addProgressListener(ProgressListener l) {
    }
}
