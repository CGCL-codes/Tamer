package org.columba.mail.gui.tree.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.selection.ISelectionListener;
import org.columba.api.selection.SelectionChangedEvent;
import org.columba.core.command.CommandProcessor;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.IMailbox;
import org.columba.mail.folder.command.ApplyFilterCommand;
import org.columba.mail.gui.frame.AbstractMailFrameController;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.tree.selection.TreeSelectionChangedEvent;
import org.columba.mail.util.MailResourceLoader;

/**
 * @author frd
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class ApplyFilterAction extends AbstractColumbaAction implements ISelectionListener {

    public ApplyFilterAction(IFrameMediator frameMediator) {
        super(frameMediator, MailResourceLoader.getString("menu", "mainframe", "menu_folder_applyfilter"));
        putValue(SHORT_DESCRIPTION, MailResourceLoader.getString("menu", "mainframe", "menu_folder_applyfilter").replaceAll("&", ""));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        setEnabled(false);
        ((MailFrameMediator) frameMediator).registerTreeSelectionListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        IMailFolderCommandReference r = ((AbstractMailFrameController) getFrameMediator()).getTreeSelection();
        CommandProcessor.getInstance().addOp(new ApplyFilterCommand(r));
    }

    public void selectionChanged(SelectionChangedEvent e) {
        if (((TreeSelectionChangedEvent) e).getSelected().length == 1 && ((TreeSelectionChangedEvent) e).getSelected()[0] instanceof IMailbox) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }
}
