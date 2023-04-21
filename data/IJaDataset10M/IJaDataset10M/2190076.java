package org.columba.mail.gui.tree.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.frame.TreeViewOwner;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.util.MailResourceLoader;

/**
 * Move selected folder down for one row.
 * <p>
 * 
 * @author fdietz
 * @author redsolo
 */
public class MoveDownAction extends AbstractMoveFolderAction {

    /**
	 * @param frameMediator
	 *            the frame controller.
	 */
    public MoveDownAction(IFrameMediator frameMediator) {
        super(frameMediator, MailResourceLoader.getString("menu", "mainframe", "menu_folder_movedown"));
        setEnabled(false);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.ALT_MASK));
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent arg0) {
        MailFolderCommandReference r = (MailFolderCommandReference) ((MailFrameMediator) frameMediator).getTreeSelection();
        IMailFolder folder = (IMailFolder) r.getSourceFolder();
        int newIndex = folder.getParent().getIndex(folder);
        newIndex = newIndex + 1;
        ((IMailFolder) folder.getParent()).insert(folder, newIndex);
        FolderTreeModel.getInstance().nodeStructureChanged(folder.getParent());
        ((TreeViewOwner) frameMediator).getTreeController().setSelected(folder);
    }

    /** {@inheritDoc} */
    protected boolean isActionEnabledByIndex(int folderIndex) {
        return (folderIndex < (getLastSelectedFolder().getParent().getChildCount() - 1));
    }
}
