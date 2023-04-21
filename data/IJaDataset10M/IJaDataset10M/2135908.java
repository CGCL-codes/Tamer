package org.columba.mail.gui.tree.action;

import java.awt.event.ActionEvent;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.selection.ISelectionListener;
import org.columba.api.selection.SelectionChangedEvent;
import org.columba.core.command.CommandProcessor;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.command.MarkFolderAsReadCommand;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.tree.selection.TreeSelectionChangedEvent;
import org.columba.mail.util.MailResourceLoader;

/**
 * Action to mark all messages as read.
 * <p>
 * This marks all messages in a folder as read. Using this action will make it
 * simpler for an user to mark all messages as read in huge mailing list
 * folders.
 * <p>
 * 
 * @author redsolo
 */
public class MarkFolderAsReadAction extends AbstractColumbaAction implements ISelectionListener {

    /**
	 * @param frameMediator
	 *            the frame mediator
	 */
    public MarkFolderAsReadAction(IFrameMediator frameMediator) {
        super(frameMediator, MailResourceLoader.getString("menu", "mainframe", "menu_folder_markasread"));
        putValue(SHORT_DESCRIPTION, MailResourceLoader.getString("menu", "mainframe", "menu_folder_markasread").replaceAll("&", ""));
        setEnabled(false);
        ((MailFrameMediator) frameMediator).registerTreeSelectionListener(this);
    }

    /** {@inheritDoc} */
    public void actionPerformed(ActionEvent e) {
        IMailFolderCommandReference r = ((MailFrameMediator) getFrameMediator()).getTreeSelection();
        CommandProcessor.getInstance().addOp(new MarkFolderAsReadCommand(r));
    }

    /** {@inheritDoc} */
    public void selectionChanged(SelectionChangedEvent e) {
        if (((TreeSelectionChangedEvent) e).getSelected().length == 1 && ((TreeSelectionChangedEvent) e).getSelected()[0] instanceof IMailbox) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }
}
