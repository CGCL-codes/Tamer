package com.rendion.ajl.gui;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.ImageIcon;

public class ButtonFactory extends WidgetFactory {

    static Layout newButton(Layout layout, String id, String caption) {
        Button button = new Button(caption);
        button.setName(id.intern());
        button.setMaximumSize(button.getPreferredSize());
        button.setMinimumSize(button.getPreferredSize());
        button.addActionListener(button);
        return attach(layout, id, button);
    }

    static Layout newButton(Layout layout, String caption, int width) {
        Button button = new Button(caption);
        button.setName(caption.intern());
        Dimension d = button.getPreferredSize();
        d = new Dimension(LF.isTiger ? width + 4 : LF.isLeopard ? width + 5 : width, (int) d.getHeight());
        button.setPreferredSize(d);
        d.setSize(d);
        button.setMaximumSize(d);
        button.setMinimumSize(d);
        button.addActionListener(button);
        return attach(layout, caption, button);
    }

    static Layout newButton(Layout layout, String caption, Dimension size) {
        Button button = new Button(caption);
        button.setName(caption.intern());
        Dimension d = button.getPreferredSize();
        d = new Dimension(size.width, d.height);
        button.setPreferredSize(d);
        d.setSize(size.getWidth(), d.getHeight());
        button.setMaximumSize(d);
        button.setMinimumSize(d);
        button.addActionListener(button);
        return attach(layout, caption, button);
    }

    public static Layout newImgButton(Layout layout, String path) {
        Button button = new Button(null, new ImageIcon(path));
        button.setName(("imgButton" + path).intern());
        button.setMaximumSize(button.getPreferredSize());
        button.setMinimumSize(button.getPreferredSize());
        button.addActionListener(button);
        return attach(layout, button.getName(), button);
    }

    public static Layout newImgButton(Layout layout, String name, String path) {
        ImageIcon icon = new ImageIcon(path);
        Button button = new Button(null, icon);
        button.setName(name.intern());
        Dimension d = button.getPreferredSize();
        if (LF.isMac) {
            Insets insets = button.getInsets();
            insets.set(2, 2, 2, 2);
        }
        button.setMaximumSize(d);
        button.setMinimumSize(d);
        button.addActionListener(button);
        return attach(layout, button.getName(), button);
    }

    public static Layout newImgButton(Layout layout, String name, String path, int width) {
        ImageIcon icon = new ImageIcon(path);
        Button button = new Button(null, icon);
        button.setName(name.intern());
        Dimension d = button.getPreferredSize();
        d.width = width;
        if (LF.isMac) {
            Insets insets = button.getInsets();
            insets.set(2, 2, 2, 2);
        }
        button.setPreferredSize(d);
        button.setMinimumSize(d);
        button.setMaximumSize(d);
        button.addActionListener(button);
        return attach(layout, button.getName(), button);
    }
}
