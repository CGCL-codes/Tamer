package net.sf.salmon.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;

public class IconView extends LiteComponent {

    protected Icon icon = null;

    protected void addImpl(Component comp, Object constraints, int index) {
    }

    public Icon getIcon() {
        return icon;
    }

    public Dimension getMinimumSize() {
        int w = this.getInsets().left + this.getInsets().right;
        int h = this.getInsets().top + this.getInsets().bottom;
        if (icon != null) return new Dimension(w + icon.getIconWidth(), h + icon.getIconHeight());
        return new Dimension(w, h);
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public void paintComponent(Graphics g) {
        if (icon != null) icon.paintIcon(this, g, this.getInsets().left, this.getInsets().top);
        this.paintBorder(g);
    }

    public void setIcon(Icon newIcon) {
        icon = newIcon;
        if (this.getParent() != null) this.getParent().doLayout();
    }

    public void setLayout(LayoutManager mgr) {
    }
}
