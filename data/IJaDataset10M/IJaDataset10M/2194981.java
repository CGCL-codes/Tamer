package org.glossitope.container.wm.buffered;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import org.glossitope.desklet.DeskletContainer;
import org.glossitope.container.security.DefaultContext;
import org.glossitope.container.util.MoveMouseListener;

public class BufferedDeskletContainer extends DeskletContainer {

    private JComponent topComponent;

    private JComponent content;

    private DefaultContext context;

    private DeskletProxy proxy;

    private DCPeer peer;

    private boolean draggable = false;

    protected BufferedWM wm;

    MoveMouseListener mml;

    public List<Buffered2DSubSurface> surfaces;

    public boolean showSurfaces = false;

    boolean isBuffered = true;

    public BufferedDeskletContainer(BufferedWM wm, DefaultContext context, DeskletProxy proxy) {
        this.wm = wm;
        this.setProxy(proxy);
        this.setContext(context);
        topComponent = new DeskletToplevel(this);
        getTopComponent().setLayout(new BorderLayout());
        mml = new MoveMouseListener(this, wm);
        setBackgroundDraggable(false);
        surfaces = new ArrayList<Buffered2DSubSurface>();
    }

    public Dimension2D getSize() {
        return getPeer().getSize();
    }

    public void setSize(Dimension2D dimension2D) {
        Dimension2D old = getPeer().getSize();
        if (!old.equals(dimension2D)) {
            getTopComponent().setSize((Dimension) dimension2D);
            getPeer().setSize(dimension2D);
        }
    }

    public Point2D getLocation() {
        return getPeer().getLocation();
    }

    public void setLocation(Point2D point) {
        getPeer().setLocation(point);
    }

    public void setBackgroundDraggable(boolean backgroundDraggable) {
        if (backgroundDraggable == this.draggable) {
            return;
        }
        if (backgroundDraggable) {
            getTopComponent().addMouseListener(mml);
            getTopComponent().addMouseMotionListener(mml);
        } else {
            getTopComponent().removeMouseListener(mml);
            getTopComponent().removeMouseMotionListener(mml);
        }
    }

    public boolean isBackgroundDraggable() {
        return this.draggable;
    }

    public JComponent getContent() {
        return this.content;
    }

    public void setContent(JComponent content) {
        if (this.content != null) {
            getTopComponent().remove(this.content);
            getPeer().contentChanged();
        }
        this.content = content;
        getTopComponent().add(content, "Center");
        getPeer().contentChanged();
        pack();
    }

    public void setVisible(boolean b) {
        pack();
        getPeer().setVisible(b);
    }

    public void pack() {
        getTopComponent().setSize(getTopComponent().getLayout().preferredLayoutSize(getTopComponent()));
        setSize(getTopComponent().getSize());
    }

    public boolean isVisible() {
        if (getPeer() == null) return false;
        return getPeer().isVisible();
    }

    public void setShaped(boolean shaped) {
        getPeer().setShaped(shaped);
    }

    public boolean isShaped() {
        return getPeer().isShaped();
    }

    public void setShape(Shape arg0) {
    }

    public Shape getShape() {
        return null;
    }

    public void setResizable(boolean arg0) {
    }

    public boolean isResizable() {
        return false;
    }

    public DefaultContext getContext() {
        return this.context;
    }

    public void setContext(DefaultContext context) {
        this.context = context;
    }

    public DeskletProxy getProxy() {
        return proxy;
    }

    public void setProxy(DeskletProxy proxy) {
        this.proxy = proxy;
    }

    public DCPeer getPeer() {
        return peer;
    }

    public void setPeer(DCPeer peer) {
        DCPeer old = getPeer();
        this.peer = peer;
        if (peer instanceof Buffered2DPeer) {
            this.isBuffered = true;
        } else {
            this.isBuffered = false;
        }
        if (old != null) {
            this.peer.setShaped(old.isShaped());
            if (this.peer instanceof JFramePeer) {
                JFramePeer jfp = (JFramePeer) this.peer;
                jfp.setBackgroundDraggable(this.isBackgroundDraggable());
            }
        }
    }

    public JComponent getTopComponent() {
        return topComponent;
    }
}
