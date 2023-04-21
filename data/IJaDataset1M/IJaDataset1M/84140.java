package net.sf.amemailchecker.gui.messageviewer.extension.action;

import net.sf.amemailchecker.app.ApplicationContext;
import net.sf.amemailchecker.app.extension.MailActionContext;
import net.sf.amemailchecker.app.extension.viewer.FolderTree;
import net.sf.amemailchecker.app.extension.viewer.MessageViewerContext;
import net.sf.amemailchecker.db.DefaultDAOFactory;
import net.sf.amemailchecker.db.common.DataBaseNegotiationException;
import net.sf.amemailchecker.db.dao.AccountDAO;
import net.sf.amemailchecker.db.dao.FolderDAO;
import net.sf.amemailchecker.gui.messageviewer.MessageViewer;
import net.sf.amemailchecker.mail.impl.mailbox.LocalFolder;
import net.sf.amemailchecker.mail.model.Account;
import net.sf.amemailchecker.mail.model.Folder;
import net.sf.amemailchecker.mail.model.FolderType;
import net.sf.amemailchecker.util.StringUtil;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FolderCreateLocalAction extends AbstractAction {

    private static final Logger logger = Logger.getLogger(FolderCreateLocalAction.class.getName());

    private MailActionContext context;

    public FolderCreateLocalAction(String name, MailActionContext context) {
        super(name);
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String value = JOptionPane.showInputDialog(ApplicationContext.getInstance().getI18NBundleValue("message.enter.folder.name"));
        if (StringUtil.isNullOrEmpty(value)) return;
        Folder parent = context.getFolder();
        FolderDAO folderDAO = DefaultDAOFactory.getInstance().getFolderDAO();
        LocalFolder folder = new LocalFolder(UUID.randomUUID().toString());
        folder.setLabel(value);
        try {
            AccountDAO accountDAO = DefaultDAOFactory.getInstance().getAccountDAO();
            Account account = accountDAO.find(context.getAccount().getUuid());
            if (account == null) {
                accountDAO.insert(context.getAccount());
            }
            folderDAO.insert(folder, parent, context.getAccount());
        } catch (DataBaseNegotiationException exception) {
            logger.log(Level.INFO, "Unable to create local folder " + folder.getName() + " for account " + context.getAccount());
            logger.log(Level.SEVERE, exception.getMessage(), exception);
            return;
        }
        MessageViewerContext viewerContext = MessageViewer.Viewer.getContext();
        FolderTree folderTree = viewerContext.getFolderTree().getLocalFolderTree(context.getAccount());
        Folder current = folderTree.getSelectedFolder();
        folderTree.insert(folder, context.getFolder());
        folderTree.setSelectedFolder(current);
    }

    @Override
    public boolean isEnabled() {
        Folder folder = context.getFolder();
        return (folder == null || folder.getType().equals(FolderType.LOCAL)) && super.isEnabled();
    }
}
