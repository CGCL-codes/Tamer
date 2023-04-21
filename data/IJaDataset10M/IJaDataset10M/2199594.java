package edu.ufpa.ppgcc.visualpseudo.gui.components;

import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import edu.ufpa.ppgcc.visualpseudo.gui.MainWindow;

public class WindowManager extends DefaultDesktopManager {

    private MainWindow main = null;

    private WindowWatcher ww = null;

    public WindowManager(MainWindow main, JDesktopPane desktop) {
        this.main = main;
        this.ww = new WindowWatcher(desktop);
        desktop.setDesktopManager(this);
    }

    public WindowWatcher getWindowWatcher() {
        return ww;
    }

    public void activateFrame(JInternalFrame f) {
        super.activateFrame(f);
        main.refresh();
        ww.repaint();
    }

    public void beginDraggingFrame(JComponent f) {
        super.beginDraggingFrame(f);
        ww.repaint();
    }

    public void beginResizingFrame(JComponent f, int direction) {
        super.beginResizingFrame(f, direction);
        ww.repaint();
    }

    public void closeFrame(JInternalFrame f) {
        super.closeFrame(f);
        main.refresh();
        ww.repaint();
    }

    public void deactivateFrame(JInternalFrame f) {
        super.deactivateFrame(f);
        ww.repaint();
    }

    public void deiconifyFrame(JInternalFrame f) {
        super.deiconifyFrame(f);
        ww.repaint();
    }

    public void dragFrame(JComponent f, int newX, int newY) {
        f.setLocation(newX, newY);
        ww.repaint();
    }

    public void endDraggingFrame(JComponent f) {
        super.endDraggingFrame(f);
        ww.repaint();
    }

    public void endResizingFrame(JComponent f) {
        super.endResizingFrame(f);
        ww.repaint();
    }

    public void iconifyFrame(JInternalFrame f) {
        super.iconifyFrame(f);
        ww.repaint();
    }

    public void maximizeFrame(JInternalFrame f) {
        super.maximizeFrame(f);
        ww.repaint();
    }

    public void minimizeFrame(JInternalFrame f) {
        super.minimizeFrame(f);
        ww.repaint();
    }

    public void openFrame(JInternalFrame f) {
        super.openFrame(f);
        ww.repaint();
    }

    public void resizeFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
        f.setBounds(newX, newY, newWidth, newHeight);
        ww.repaint();
    }

    public void setBoundsForFrame(JComponent f, int newX, int newY, int newWidth, int newHeight) {
        f.setBounds(newX, newY, newWidth, newHeight);
        ww.repaint();
    }
}
