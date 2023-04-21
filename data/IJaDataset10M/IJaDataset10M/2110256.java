package org.gjt.sp.jedit.browser;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import org.gjt.sp.jedit.io.VFS;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.jEdit;

public final class FileCellRenderer implements javax.swing.ListCellRenderer, javax.swing.tree.TreeCellRenderer {

    public FileCellRenderer() {
        font = UIManager.getFont("Tree.font");
        font = new Font(font.getName(), Font.PLAIN, font.getSize());
        treeSelectionForeground = UIManager.getColor("Tree.selectionForeground");
        treeNoSelectionForeground = UIManager.getColor("Tree.textForeground");
        treeSelectionBackground = UIManager.getColor("Tree.selectionBackground");
        treeNoSelectionBackground = UIManager.getColor("Tree.textBackground");
        UIDefaults metalDefaults = new javax.swing.plaf.metal.MetalLookAndFeel().getDefaults();
        fileIcon = metalDefaults.getIcon("FileView.fileIcon");
        dirIcon = metalDefaults.getIcon("FileView.directoryIcon");
        filesystemIcon = metalDefaults.getIcon("FileView.hardDriveIcon");
        loadingIcon = metalDefaults.getIcon("FileView.hardDriveIcon");
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean sel, boolean focus) {
        if (listCellRenderer == null) {
            listCellRenderer = new JLabel();
            listCellRenderer.setOpaque(true);
            listCellRenderer.setFont(font);
        }
        VFS.DirectoryEntry file = (VFS.DirectoryEntry) value;
        boolean opened = (jEdit.getBuffer(file.path) != null);
        listCellRenderer.setBorder(opened ? openBorder : closedBorder);
        if (sel) {
            listCellRenderer.setBackground(list.getSelectionBackground());
            listCellRenderer.setForeground(list.getSelectionForeground());
        } else {
            listCellRenderer.setBackground(list.getBackground());
            listCellRenderer.setForeground(list.getForeground());
        }
        listCellRenderer.setIcon(getIconForFile(file));
        listCellRenderer.setText(file.name);
        listCellRenderer.setEnabled(list.isEnabled());
        return listCellRenderer;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean focus) {
        if (treeCellRenderer == null) {
            treeCellRenderer = new JLabel();
            treeCellRenderer.setOpaque(true);
            treeCellRenderer.setFont(font);
        }
        if (sel) {
            treeCellRenderer.setBackground(treeSelectionBackground);
            treeCellRenderer.setForeground(treeSelectionForeground);
        } else {
            treeCellRenderer.setBackground(treeNoSelectionBackground);
            treeCellRenderer.setForeground(treeNoSelectionForeground);
        }
        treeCellRenderer.setEnabled(tree.isEnabled());
        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
        Object userObject = treeNode.getUserObject();
        if (userObject instanceof VFS.DirectoryEntry) {
            VFS.DirectoryEntry file = (VFS.DirectoryEntry) userObject;
            boolean opened = (jEdit.getBuffer(file.path) != null);
            treeCellRenderer.setBorder(opened ? openBorder : closedBorder);
            treeCellRenderer.setIcon(getIconForFile(file));
            treeCellRenderer.setText(file.name);
        } else if (userObject instanceof BrowserTreeView.LoadingPlaceholder) {
            treeCellRenderer.setIcon(loadingIcon);
            treeCellRenderer.setText(jEdit.getProperty("vfs.browser.tree.loading"));
            treeCellRenderer.setBorder(closedBorder);
        } else if (userObject instanceof String) {
            treeCellRenderer.setIcon(dirIcon);
            treeCellRenderer.setText((String) userObject);
            treeCellRenderer.setBorder(closedBorder);
        }
        return treeCellRenderer;
    }

    protected Icon getIconForFile(VFS.DirectoryEntry file) {
        if (file.type == VFS.DirectoryEntry.DIRECTORY) return dirIcon; else if (file.type == VFS.DirectoryEntry.FILESYSTEM) return filesystemIcon; else return fileIcon;
    }

    private JLabel listCellRenderer = null;

    private JLabel treeCellRenderer = null;

    private Font font;

    private Icon fileIcon;

    private Icon dirIcon;

    private Icon filesystemIcon;

    private Icon loadingIcon;

    private Border closedBorder = new EmptyBorder(0, 3, 1, 0);

    private Border openBorder = new CompoundBorder(new EmptyBorder(0, 1, 1, 0), new MatteBorder(0, 2, 0, 0, Color.black));

    private Color treeSelectionForeground;

    private Color treeNoSelectionForeground;

    private Color treeSelectionBackground;

    private Color treeNoSelectionBackground;
}
