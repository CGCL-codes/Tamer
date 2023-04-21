package gnu.java.awt.peer.qt;

import java.awt.Container;
import java.awt.Component;
import java.awt.Insets;
import java.awt.peer.ContainerPeer;

public class QtContainerPeer extends QtComponentPeer implements ContainerPeer {

    public QtContainerPeer(QtToolkit kit, Component owner) {
        super(kit, owner);
    }

    protected void init() {
    }

    protected void setup() {
        super.setup();
    }

    public void beginLayout() {
    }

    public void beginValidate() {
    }

    public void endLayout() {
        QtUpdate();
    }

    public void endValidate() {
    }

    public Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    public Insets insets() {
        return getInsets();
    }

    public boolean isPaintPending() {
        return false;
    }

    public boolean isRestackSupported() {
        return false;
    }

    public void cancelPendingPaint(int x, int y, int width, int height) {
    }

    public void restack() {
    }
}
