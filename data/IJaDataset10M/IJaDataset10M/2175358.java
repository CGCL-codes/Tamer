package org.makagiga.desktop.desklet;

import java.awt.BorderLayout;
import java.awt.Shape;
import java.awt.geom.Dimension2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.TK;
import org.makagiga.desktop.Widget;

/**
 * @since 2.0
 */
public final class DeskletWidgetContainer extends AbstractDeskletContainer<Widget> {

    public DeskletWidgetContainer(final Widget container) {
        super(container);
    }

    public void pack() {
        container.pack();
        container.setDefaultSize(null);
    }

    public void setBackgroundDraggable(final boolean backgroundDraggable) {
        this.backgroundDraggable = backgroundDraggable;
        container.setBackgroundDraggable(backgroundDraggable);
    }

    /**
	 * @throws NullPointerException If @p content is @c null
	 */
    public void setContent(final JComponent content) {
        container.removeAll();
        this.content = TK.checkNull(content, "content");
        container.add(content, BorderLayout.CENTER);
        pack();
    }

    public void setLocation(final Point2D location) {
        this.location = location;
        if (location == null) container.setLocation(0, 0); else container.setLocation((int) location.getX(), (int) location.getY());
    }

    public void setShape(final Shape shape) {
        this.shape = shape;
        MLogger.warning("desklet", "setShape( %s ) not implemented", shape);
    }

    public void setShaped(final boolean shaped) {
        this.shaped = shaped;
        container.setShaped(shaped);
    }

    public void setResizable(final boolean resizable) {
        this.resizable = resizable;
        container.setResizable(resizable);
    }

    @Override
    public void setSize(final Dimension2D size) {
        super.setSize(size);
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
        container.setVisible(visible);
    }
}
