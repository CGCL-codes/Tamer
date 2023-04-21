package se.runa.ui;

import java.awt.Component;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import se.runa.common.Constants;
import se.runa.file.FileManager;

/**
 * 
 * @author frebjo
 */
public class FileTreeRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 4841077728594704184L;

    private Icon computerIcon;

    private String computerName;

    public FileTreeRenderer() {
        URL url = getClass().getResource(Constants.COMPUTER_ICON);
        computerIcon = new ImageIcon(url);
        ResourceBundle bundle = ResourceBundle.getBundle(Constants.RESOURCE_BUNDLE);
        computerName = bundle.getString(Constants.COMPUTER_NAME);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (selected) {
            setForeground(getTextSelectionColor());
        } else {
            setForeground(getTextNonSelectionColor());
        }
        setEnabled(tree.isEnabled());
        setComponentOrientation(tree.getComponentOrientation());
        this.selected = selected;
        File file = (File) value;
        Icon icon;
        String name;
        if (file instanceof FileTreeModel) {
            icon = computerIcon;
            name = computerName;
        } else {
            icon = FileManager.getIcon(file);
            name = file.getName();
        }
        setIcon(icon);
        setText(name);
        return this;
    }
}
