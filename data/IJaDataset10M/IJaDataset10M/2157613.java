package com.definity.toolkit.ui.widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JMenuBar;
import com.definity.toolkit.ui.Body;

public class MenuBar extends JMenuBar implements Widget<MenuBar> {

    private static final long serialVersionUID = -5920498941360730462L;

    public MenuBar() {
    }

    @Override
    public String id() {
        return getName();
    }

    @Override
    public MenuBar id(String id) {
        setName(id);
        return this;
    }

    @Override
    public MenuBar properties(Body<MenuBar> body) {
        body.content(this);
        return this;
    }

    @Override
    public MenuBar size(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        return this;
    }

    public MenuBar add(Menu menu) {
        super.add(menu);
        return this;
    }

    @Override
    public Color background() {
        return getBackground();
    }

    @Override
    public MenuBar background(Color background) {
        setBackground(background);
        return this;
    }

    @Override
    public Color foreground() {
        return getForeground();
    }

    @Override
    public MenuBar foreground(Color foreground) {
        setForeground(foreground);
        return this;
    }

    @Override
    public Font font() {
        return getFont();
    }

    @Override
    public MenuBar font(Font font) {
        setFont(font);
        return this;
    }
}
