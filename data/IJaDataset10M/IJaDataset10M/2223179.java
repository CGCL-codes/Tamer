package skanque.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Composite icon for making icons next to each other
 */
public class IconComposite extends ImageIcon {

    private List<Icon> children = new ArrayList<Icon>();

    private int separator = 2;

    public IconComposite() {
        super();
    }

    public void add(Icon icon) {
        children.add(icon);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int foo = x;
        for (int i = 0; i < children.size(); i++) {
            Icon child = children.get(i);
            if (i != 0) {
                foo += child.getIconWidth() + separator;
            }
            child.paintIcon(c, g, foo, y);
        }
        super.paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        int foo = 0;
        for (Icon child : children) {
            foo += child.getIconWidth() + separator;
        }
        return foo;
    }

    @Override
    public int getIconHeight() {
        int max = 0;
        for (Icon child : children) {
            int current = child.getIconHeight();
            if (current > max) {
                max = current;
            }
        }
        return max;
    }

    public void remove(Icon icon) {
        children.remove(icon);
    }
}
