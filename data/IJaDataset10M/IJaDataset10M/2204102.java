package org.columba.mail.gui.table.action;

import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.selection.ISelectionListener;
import org.columba.api.selection.SelectionChangedEvent;
import org.columba.core.command.CommandProcessor;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.mail.command.IMailFolderCommandReference;
import org.columba.mail.folder.command.SaveMessageSourceAsCommand;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.table.selection.TableSelectionChangedEvent;
import org.columba.mail.util.MailResourceLoader;

/**
 * Action for saving message source, i.e. for saving a message
 * as-is incl. all headers.
 * @author Karl Peder Olesen (karlpeder), 20030615
 */
public class SaveMessageSourceAsAction extends AbstractColumbaAction implements ISelectionListener {

    /** JDK 1.4+ logging framework logger, used for logging. */
    private static final Logger LOG = Logger.getLogger("org.columba.mail.gui.table.action");

    public SaveMessageSourceAsAction(IFrameMediator controller) {
        super(controller, MailResourceLoader.getString("menu", "mainframe", "menu_file_save"));
        putValue(SHORT_DESCRIPTION, MailResourceLoader.getString("menu", "mainframe", "menu_file_save_tooltip").replaceAll("&", ""));
        putValue(SMALL_ICON, ImageLoader.getSmallIcon("document-save.png"));
        putValue(LARGE_ICON, ImageLoader.getIcon("document-save.png"));
        setEnabled(false);
        ((MailFrameMediator) frameMediator).registerTableSelectionListener(this);
    }

    /**
     * Executes this action - i.e. saves message source
     * by invocing the necessary command.
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent evt) {
        IMailFolderCommandReference r = ((MailFrameMediator) getFrameMediator()).getTableSelection();
        LOG.info("Save Message Source As... called");
        SaveMessageSourceAsCommand c = new SaveMessageSourceAsCommand(r);
        CommandProcessor.getInstance().addOp(c);
    }

    /**
     * Handles enabling / disabling of menu/action depending
     * on selection
     * @see org.columba.core.gui.util.ISelectionListener#selectionChanged(org.columba.core.gui.util.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent e) {
        setEnabled(((TableSelectionChangedEvent) e).getUids().length > 0);
    }
}
