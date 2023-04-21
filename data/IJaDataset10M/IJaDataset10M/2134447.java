package de.grogra.icon;

import java.awt.*;

public final class IconAdapter extends Dimension implements javax.swing.Icon {

    private final IconSource source;

    private Icon icon;

    private int state;

    public static IconAdapter create(IconSource source, Dimension size, boolean forceSize, int state) {
        if (source == null) {
            return null;
        }
        Icon i = source.getIcon(size, state);
        return (i == null) ? null : new IconAdapter(source, i, size.width, size.height, forceSize, state);
    }

    public static IconAdapter create(IconSource source, Dimension size) {
        return create(source, size, true, Icon.DEFAULT);
    }

    public static IconAdapter create(IconSource source, Dimension size, boolean forceSize) {
        return create(source, size, forceSize, Icon.DEFAULT);
    }

    public IconAdapter(IconSource source, Icon icon, int width, int height, boolean forceSize, int state) {
        super(forceSize || (source.getPreferredIconSize(false) == null) ? width : source.getPreferredIconSize(false).width, forceSize || (source.getPreferredIconSize(false) == null) ? height : source.getPreferredIconSize(false).height);
        this.source = source;
        this.icon = icon;
        this.state = state;
    }

    public void set(Icon icon, int width, int height, int state) {
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.state = state;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (icon != null) {
            icon.paintIcon(c, (Graphics2D) g, x, y, width, height, state);
        }
    }

    public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }

    public IconSource getIconSource() {
        return source;
    }

    public Icon getIcon() {
        return icon;
    }

    public IconAdapter toState(int state) {
        return create(source, this, true, state);
    }
}
