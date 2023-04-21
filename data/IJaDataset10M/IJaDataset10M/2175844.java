package net.suberic.pooka.gui;

import net.suberic.pooka.*;
import javax.swing.tree.*;
import java.awt.*;
import javax.mail.MessagingException;
import javax.swing.JTree;

/**
 * This class overrides the default TreeCellRenderer in order to 
 * provide notification of some such, like for unread messages.  
 * Subclasses could probably add additional enhancements.
 *
 */
public class FolderChooserTreeCellRenderer extends DefaultTreeCellRenderer {

    private boolean hasFocus;

    Font specialFont = null;

    Font defaultFont = null;

    public FolderChooserTreeCellRenderer() {
        super();
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        String stringValue = tree.convertValueToText(value, sel, expanded, leaf, row, hasFocus);
        this.hasFocus = hasFocus;
        setText(stringValue);
        if (sel) setForeground(getTextSelectionColor()); else setForeground(getTextNonSelectionColor());
        if (!tree.isEnabled()) {
            setEnabled(false);
            if (leaf) {
                setDisabledIcon(getLeafIcon());
            } else if (expanded) {
                setDisabledIcon(getOpenIcon());
            } else {
                setDisabledIcon(getClosedIcon());
            }
        } else {
            setEnabled(true);
            if (leaf) {
                setIcon(getLeafIcon());
            } else if (expanded) {
                setIcon(getOpenIcon());
            } else {
                setIcon(getClosedIcon());
            }
        }
        selected = sel;
        TreePath tp = tree.getPathForRow(row);
        if (tp != null && tp.getLastPathComponent() instanceof ChooserFolderNode) {
            ChooserFolderNode node = (ChooserFolderNode) tp.getLastPathComponent();
            try {
                if (isSpecial(node)) setFontToSpecial(); else {
                    setFontToDefault();
                }
            } catch (MessagingException me) {
                this.setFontToDefault();
                this.setForeground(Color.getColor(Pooka.getProperty("MailTreeNode.color.error", "red")));
                return this;
            } catch (NullPointerException npe) {
                this.setFontToDefault();
                this.setForeground(Color.getColor(Pooka.getProperty("MailTreeNode.color.error", "red")));
                return this;
            }
        } else {
            setFontToDefault();
        }
        this.setForeground(Color.getColor(Pooka.getProperty("MailTreeNode.color", "black")));
        return this;
    }

    public void setFontToDefault() {
        if (getDefaultFont() != null) {
            setFont(getDefaultFont());
        } else {
            String fontStyle;
            fontStyle = Pooka.getProperty("FolderTree.readStyle", "");
            Font f = null;
            if (this.getFont() == null) return;
            if (fontStyle.equalsIgnoreCase("BOLD")) f = this.getFont().deriveFont(Font.BOLD); else if (fontStyle.equalsIgnoreCase("ITALIC")) f = this.getFont().deriveFont(Font.ITALIC); else if (fontStyle.equals("")) f = this.getFont().deriveFont(Font.PLAIN);
            if (f == null) f = this.getFont();
            setDefaultFont(f);
            this.setFont(f);
        }
    }

    public void setFontToSpecial() {
        if (getSpecialFont() != null) {
            setFont(getSpecialFont());
        } else {
            String fontStyle;
            fontStyle = Pooka.getProperty("FolderChooser.subscribedStyle", "BOLD");
            Font f = null;
            if (fontStyle.equalsIgnoreCase("BOLD")) f = this.getFont().deriveFont(Font.BOLD); else if (fontStyle.equalsIgnoreCase("ITALIC")) f = this.getFont().deriveFont(Font.ITALIC);
            if (f == null) f = this.getFont();
            setSpecialFont(f);
            this.setFont(f);
        }
    }

    /**
     * Returns whether or not we should render the default style or a 
     * special style.
     */
    public boolean isSpecial(ChooserFolderNode node) throws MessagingException {
        return (node != null && node.isSubscribed());
    }

    public Font getSpecialFont() {
        return specialFont;
    }

    public void setSpecialFont(Font newValue) {
        specialFont = newValue;
    }

    public Font getDefaultFont() {
        return defaultFont;
    }

    public void setDefaultFont(Font newValue) {
        defaultFont = newValue;
    }

    /**
     * Overrides <code>JComponent.getPreferredSize</code> to
     * return slightly wider preferred size value.
     */
    public Dimension getPreferredSize() {
        Dimension retDimension = super.getPreferredSize();
        if (retDimension != null) retDimension = new Dimension((int) ((retDimension.width + 3) * 1.2), retDimension.height);
        return retDimension;
    }
}
