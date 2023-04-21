package javax.help.plaf.basic;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.net.URL;
import java.util.Locale;
import javax.help.TOCItem;
import javax.help.TOCView;
import javax.help.Map;
import javax.help.HelpUtilities;
import javax.help.Map.ID;

/**
 * Basic cell renderer for TOC UI.
 *
 * @author Roger D. Brinkley
 * @author Eduardo Pelegri-Llopart
 * @version   1.25     10/30/06
 */
public class BasicTOCCellRenderer extends DefaultTreeCellRenderer {

    protected Map map;

    protected TOCView view;

    public BasicTOCCellRenderer(Map map) {
        this(map, null);
    }

    public BasicTOCCellRenderer(Map map, TOCView view) {
        super();
        this.map = map;
        this.view = view;
    }

    /**
      * Configures the renderer based on the components passed in.
      * Sets the value from messaging value with toString().
      * The foreground color is set based on the selection and the icon
      * is set based on on leaf and expanded.
      */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        String stringValue = "";
        try {
            this.hasFocus = hasFocus;
        } catch (IllegalAccessError e) {
        }
        TOCItem item = (TOCItem) ((DefaultMutableTreeNode) value).getUserObject();
        if (item != null) {
            stringValue = item.getName();
        }
        setText(stringValue);
        if (sel) {
            setForeground(getTextSelectionColor());
        } else {
            setForeground(getTextNonSelectionColor());
        }
        ImageIcon icon = null;
        if (item != null) {
            ID id = item.getImageID();
            if (id != null) {
                try {
                    URL url = map.getURLFromID(id);
                    icon = new ImageIcon(url);
                } catch (Exception e) {
                }
            }
        }
        if (item != null) {
            Locale locale = item.getLocale();
            if (locale != null) {
                setLocale(locale);
            }
        }
        if (icon != null) {
            setIcon(icon);
        } else if (leaf) {
            setIcon(getLeafIcon());
        } else if (expanded) {
            setIcon(getOpenIcon());
        } else {
            setIcon(getClosedIcon());
        }
        selected = sel;
        return this;
    }

    public Icon getLeafIcon() {
        Icon icon = null;
        if (view != null) {
            ID id = view.getTopicImageID();
            if (id != null) {
                try {
                    URL url = map.getURLFromID(id);
                    icon = new ImageIcon(url);
                    return icon;
                } catch (Exception e) {
                }
            }
        }
        return super.getLeafIcon();
    }

    public Icon getOpenIcon() {
        Icon icon = null;
        if (view != null) {
            ID id = view.getCategoryOpenImageID();
            if (id != null) {
                try {
                    URL url = map.getURLFromID(id);
                    icon = new ImageIcon(url);
                    return icon;
                } catch (Exception e) {
                }
            }
        }
        return super.getOpenIcon();
    }

    public Icon getClosedIcon() {
        Icon icon = null;
        if (view != null) {
            ID id = view.getCategoryClosedImageID();
            if (id != null) {
                try {
                    URL url = map.getURLFromID(id);
                    icon = new ImageIcon(url);
                    return icon;
                } catch (Exception e) {
                }
            }
        }
        return super.getClosedIcon();
    }
}
