package net.sf.amemailchecker.gui.messageviewer.foldertree;

import net.sf.amemailchecker.db.DefaultDAOFactory;
import net.sf.amemailchecker.db.common.DataBaseNegotiationException;
import net.sf.amemailchecker.gui.component.tree.DefaultCheckedValueTreeModel;
import net.sf.amemailchecker.mail.model.Account;
import net.sf.amemailchecker.mail.model.Folder;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalFolderTreeMediatorProxy extends FolderTreeMediatorProxy {

    private static final Logger logger = Logger.getLogger(LocalFolderTreeMediatorProxy.class.getName());

    @Override
    public void update(Account account) {
        update(folders(account), false);
    }

    @Override
    JTree folderTree(Account account) {
        FolderCellLabelBuilder localFolderCellLabelBuilder = new FolderCellLabelBuilder() {

            @Override
            public void build(JLabel component, Folder folder) {
                int count = DefaultDAOFactory.getInstance().getLetterDAO().count(folder);
                if (count > 0) component.setText(component.getText() + " (" + count + ")");
            }
        };
        folderTreeModel = new DefaultCheckedValueTreeModel<Folder>();
        folderTree = folderTree(folderTreeModel, folders(account), false, localFolderCellLabelBuilder);
        return folderTree;
    }

    private List<Folder> folders(Account account) {
        List<Folder> localFolders;
        try {
            localFolders = DefaultDAOFactory.getInstance().getFolderDAO().load(account.getUuid());
        } catch (DataBaseNegotiationException e) {
            logger.log(Level.INFO, "Unable to load list of local folders for account " + account);
            logger.log(Level.SEVERE, e.getMessage(), e);
            localFolders = new ArrayList<Folder>();
        }
        return localFolders;
    }
}
