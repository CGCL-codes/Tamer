package org.columba.mail.gui.tree.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.api.selection.ISelectionListener;
import org.columba.api.selection.SelectionChangedEvent;
import org.columba.core.gui.action.AbstractColumbaAction;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.mail.command.MailFolderCommandReference;
import org.columba.mail.folder.FolderFactory;
import org.columba.mail.folder.IMailFolder;
import org.columba.mail.folder.virtual.VirtualFolder;
import org.columba.mail.gui.config.search.SearchFrame;
import org.columba.mail.gui.frame.AbstractMailFrameController;
import org.columba.mail.gui.frame.MailFrameMediator;
import org.columba.mail.gui.tree.FolderTreeModel;
import org.columba.mail.gui.tree.selection.TreeSelectionChangedEvent;
import org.columba.mail.gui.tree.util.CreateFolderDialog;
import org.columba.mail.util.MailResourceLoader;

/**
 * @author frd
 * 
 * To change this generated comment go to Window>Preferences>Java>Code
 * Generation>Code and Comments
 */
public class CreateVirtualFolderAction extends AbstractColumbaAction implements ISelectionListener {

    public CreateVirtualFolderAction(IFrameMediator frameMediator) {
        super(frameMediator, MailResourceLoader.getString("menu", "mainframe", "menu_folder_newvirtualfolder"));
        putValue(SHORT_DESCRIPTION, MailResourceLoader.getString("menu", "mainframe", "menu_folder_newvirtualfolder").replaceAll("&", ""));
        putValue(SMALL_ICON, ImageLoader.getSmallIcon("folder-saved-search.png"));
        putValue(LARGE_ICON, ImageLoader.getIcon("folder-saved-search.png"));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        setEnabled(false);
        ((MailFrameMediator) frameMediator).registerTreeSelectionListener(this);
    }

    public void actionPerformed(ActionEvent evt) {
        CreateFolderDialog dialog = new CreateFolderDialog(getFrameMediator(), null);
        String name;
        if (dialog.success()) {
            name = dialog.getName();
            try {
                MailFolderCommandReference r = (MailFolderCommandReference) ((AbstractMailFrameController) getFrameMediator()).getTreeSelection();
                VirtualFolder vfolder = (VirtualFolder) FolderFactory.getInstance().createChild((IMailFolder) r.getSourceFolder(), name, "VirtualFolder");
                FolderTreeModel.getInstance().nodeStructureChanged(r.getSourceFolder());
                vfolder.getConfiguration().setInteger("property", "source_uid", r.getSourceFolder().getUid());
                new SearchFrame((AbstractMailFrameController) frameMediator, vfolder);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            return;
        }
    }

    public void selectionChanged(SelectionChangedEvent e) {
        if (((TreeSelectionChangedEvent) e).getSelected().length > 0) {
            setEnabled(true);
        } else {
            setEnabled(false);
        }
    }
}
