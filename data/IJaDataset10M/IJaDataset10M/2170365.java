package org.columba.mail.gui.table.action;

import java.awt.event.ActionEvent;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.selection.ISelectionListener;
import org.columba.api.selection.SelectionChangedEvent;
import org.columba.core.command.CommandProcessor;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.command.CreateVFolderOnMessageCommand;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.table.selection.TableSelectionChangedEvent;
import org.columba.mail.util.MailResourceLoader;

/**
 * @author frd
 *
 * To change this generated comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CreateVFolderOnToAction extends AbstractColumbaAction implements ISelectionListener {

    public CreateVFolderOnToAction(IFrameMediator frameMediator) {
        super(frameMediator, MailResourceLoader.getString("menu", "mainframe", "menu_message_vfolderonto"));
        putValue(SHORT_DESCRIPTION, MailResourceLoader.getString("menu", "mainframe", "menu_message_vfolderonto_tooltip").replaceAll("&", ""));
        setEnabled(false);
        ((MailFrameMediator) frameMediator).registerTableSelectionListener(this);
    }

    /**
     * Called for execution of this action
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent evt) {
        IMailFolderCommandReference r = ((MailFrameMediator) getFrameMediator()).getTableSelection();
        CreateVFolderOnMessageCommand c = new CreateVFolderOnMessageCommand(getFrameMediator(), r, CreateVFolderOnMessageCommand.VFOLDER_ON_TO);
        CommandProcessor.getInstance().addOp(c);
    }

    /**
     * Called when selection changes in message table
     * @see org.columba.core.gui.util.ISelectionListener#connectionChanged(org.columba.core.gui.util.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent e) {
        setEnabled(((TableSelectionChangedEvent) e).getUids().length > 0);
    }
}
