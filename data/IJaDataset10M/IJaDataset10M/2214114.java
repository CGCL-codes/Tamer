package org.jimcat.gui.albumlist;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.TreePath;
import org.jimcat.gui.AlbumControl;
import org.jimcat.gui.SwingClient;
import org.jimcat.gui.ViewControl;
import org.jimcat.gui.icons.Icons;

/**
 * the popup menu for a album.
 * 
 * $Id$
 * 
 * @author Herbert
 */
public class AlbumPopupMenu extends JPopupMenu implements ActionListener {

    /**
	 * constant to identify rename action
	 */
    private static final String COMMAND_RENAME = "rename";

    /**
	 * constant to identify delete action
	 */
    private static final String COMMAND_DELETE = "delete";

    /**
	 * the currently associated album
	 */
    private AlbumTreeAlbumNode currentAlbumNode;

    /**
	 * default constructor
	 */
    public AlbumPopupMenu() {
        initComponents();
    }

    /**
	 * build up gui components
	 */
    private void initComponents() {
        JMenuItem rename = new JMenuItem("Rename...");
        rename.setActionCommand(COMMAND_RENAME);
        rename.setIcon(Icons.ALBUM_EDIT);
        rename.addActionListener(this);
        add(rename);
        JMenuItem delete = new JMenuItem("Delete...");
        delete.setActionCommand(COMMAND_DELETE);
        delete.setIcon(Icons.ALBUM_REMOVE);
        delete.addActionListener(this);
        add(delete);
    }

    /**
	 * @param currentAlbum
	 *            the currentAlbum to set
	 */
    public void setCurrentAlbumNode(AlbumTreeAlbumNode currentAlbum) {
        this.currentAlbumNode = currentAlbum;
    }

    /**
	 * handling menu actions
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (COMMAND_RENAME.equals(command)) {
            performRename();
        } else if (COMMAND_DELETE.equals(command)) {
            performDelete();
        }
    }

    /**
	 * start the editing command
	 */
    private void performRename() {
        TreePath path = currentAlbumNode.getPath();
        currentAlbumNode.getTree().startEditingAtPath(path);
    }

    /**
	 * initiate delete process
	 */
    private void performDelete() {
        AlbumControl control = SwingClient.getInstance().getAlbumControl();
        control.deleteAlbum(currentAlbumNode.getAlbum());
        ViewControl viewControl = SwingClient.getInstance().getViewControl();
        viewControl.clearFilter();
    }
}
