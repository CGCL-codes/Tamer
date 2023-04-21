package org.magnesia.client.gui;

import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import org.magnesia.client.gui.actions.CreateDirectory;
import org.magnesia.client.gui.actions.DownloadAll;
import org.magnesia.client.gui.actions.RenameDirectory;
import org.magnesia.client.gui.data.CachedJImage;
import org.magnesia.client.gui.data.Directory;

public class TreeView extends DefaultTreeModel implements TreeSelectionListener, MouseListener, TreeExpansionListener {

    private static final long serialVersionUID = -1512283560221534792L;

    private ImageArea ia;

    private JPopupMenu menu;

    private JTree t;

    public TreeView(ImageArea ia, JTree t, Frame parent) {
        super(new Directory("", null));
        this.ia = ia;
        this.t = t;
        menu = new JPopupMenu();
        if (!ClientConnection.getConnection().isReadOnly()) {
            menu.add(new CreateDirectory(t));
            menu.add(new RenameDirectory(t));
        }
        if (parent != null) {
            menu.add(new DownloadAll(t, parent));
        }
    }

    public void treeCollapsed(TreeExpansionEvent event) {
        ((TreeView) t.getModel()).refresh((Directory) event.getPath().getLastPathComponent());
    }

    public void valueChanged(TreeSelectionEvent arg0) {
        Directory d = (Directory) arg0.getPath().getLastPathComponent();
        ia.clean();
        ClientConnection cc = ClientConnection.getConnection();
        List<String> images = cc.getImages(d.getPath());
        List<CachedJImage> list = new LinkedList<CachedJImage>();
        for (String s : images) {
            list.add(new CachedJImage(d.getPath(), s));
        }
        ia.setImages(list);
    }

    public void refresh(Directory d) {
        d.clear();
        reload(d);
    }

    public void mousePressed(MouseEvent e) {
        handlePopupMenu(e);
    }

    public void mouseReleased(MouseEvent e) {
        handlePopupMenu(e);
    }

    private void handlePopupMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {
            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void treeExpanded(TreeExpansionEvent event) {
    }
}
