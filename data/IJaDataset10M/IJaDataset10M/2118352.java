package hu.ihash.common.gui.file.tree;

import hu.ihash.common.gui.ImageProvider;
import hu.ihash.common.model.file.ImageFilter;
import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * A renderer for the file tree.
 * 
 * @author Gergely Kiss
 */
public class FileTreeRenderer extends DefaultTreeCellRenderer {

    private static final Icon fileIcon = ImageProvider.getIcon("/ui/filetree/file.png");

    private static final Icon imageIcon = ImageProvider.getIcon("/ui/filetree/image.png");

    private static final Icon folderIcon = ImageProvider.getIcon("/ui/filetree/folder.png");

    private static final Color SELECTED_BACKGROUND = new Color(64, 64, 196);

    private static final Color SELECTED_FOREGROUND = new Color(255, 255, 255);

    private static final Color UNSELECTED_FOREGROUND = new Color(0, 0, 0);

    public FileTreeRenderer() {
        setOpaque(true);
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component c = super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
        File file = ((FileNode) value).getFile();
        String name = file.getName();
        name = name.equals("") ? file.toString() : name;
        setToolTipText(file.getAbsolutePath());
        setText(name);
        if (selected) {
            setBackground(SELECTED_BACKGROUND);
            setForeground(SELECTED_FOREGROUND);
        } else {
            setBackground(tree.getBackground());
            setForeground(UNSELECTED_FOREGROUND);
        }
        if (file.isFile()) {
            if (ImageFilter.isImage(file)) {
                setIcon(imageIcon);
            } else {
                setIcon(fileIcon);
            }
        } else if (file.isDirectory()) {
            setIcon(folderIcon);
        }
        return c;
    }
}
