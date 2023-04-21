package gnu.java.awt.peer.qt;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuBar;
import java.awt.Rectangle;
import java.awt.peer.FramePeer;

public class QtFramePeer extends QtWindowPeer implements FramePeer {

    private int theState;

    long frameObject;

    public QtFramePeer(QtToolkit kit, Component owner) {
        super(kit, owner);
    }

    protected native void init();

    protected void setup() {
        super.setup();
        setTitle(((Frame) owner).getTitle());
        if (((Frame) owner).getMenuBar() != null) setMenuBar(((Frame) owner).getMenuBar());
    }

    private native void setIcon(QtImage image);

    private native void setMaximizedBounds(int w, int h);

    private native void setMenu(QtMenuBarPeer mb);

    private native int menuBarHeight();

    public void destroy() {
        dispose();
    }

    public int getState() {
        return theState;
    }

    public Insets getInsets() {
        int mbHeight = (((Frame) owner).getMenuBar() != null) ? menuBarHeight() : 0;
        return new Insets(mbHeight, 0, 0, 0);
    }

    public void setIconImage(Image im) {
        if (im instanceof QtImage) setIcon((QtImage) im); else setIcon(new QtImage(im.getSource()));
    }

    public void setMaximizedBounds(Rectangle rect) {
    }

    public void setMenuBar(MenuBar mb) {
        if (mb != null) {
            QtMenuBarPeer mbpeer = (QtMenuBarPeer) mb.getPeer();
            if (mbpeer == null) {
                mb.addNotify();
                mbpeer = (QtMenuBarPeer) mb.getPeer();
                if (mbpeer == null) throw new IllegalStateException("No menu bar peer.");
            }
            mbpeer.addMenus();
            setMenu(mbpeer);
        } else setMenu(null);
    }

    public void setResizable(boolean resizeable) {
    }

    public void setState(int s) {
        theState = s;
    }

    public void setBoundsPrivate(int x, int y, int width, int height) {
    }

    public void updateAlwaysOnTop() {
    }

    public boolean requestWindowFocus() {
        return false;
    }
}
