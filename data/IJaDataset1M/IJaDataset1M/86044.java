package com.mainatom.ui.stdform;

import com.mainatom.ui.*;
import javax.swing.*;
import java.awt.*;

/**
 * Обертка вокруг JFrame для реализации интерфейса IWindow
 */
public class WrapperJFrame implements IWindow {

    private AWindow _window;

    private JFrame _jFrame;

    public JFrame getJFrame() {
        return _jFrame;
    }

    public void setJFrame(JFrame jFrame) {
        _jFrame = jFrame;
    }

    public AWindow getWindow() {
        return _window;
    }

    public void setWindow(AWindow window) {
        _window = window;
    }

    public Dimension getSize(Dimension rv) {
        return getJFrame().getSize(rv);
    }

    public Dimension getSize() {
        return getJFrame().getSize();
    }

    public void setSize(Dimension d) {
        getJFrame().setSize(d);
    }

    public void setSize(int width, int height) {
        getJFrame().setSize(width, height);
    }

    public void setLocation(Point p) {
        getJFrame().setLocation(p);
    }

    public Point getLocation() {
        return getJFrame().getLocation();
    }

    public void setLocation(int x, int y) {
        getJFrame().setLocation(x, y);
    }

    public void refresh() {
        JComponent c = (JComponent) getJFrame().getContentPane();
        c.paintImmediately(c.getVisibleRect());
    }

    public void setTitle(String s) {
        if (getJFrame() != null) {
            getJFrame().setTitle(s);
        }
    }
}
