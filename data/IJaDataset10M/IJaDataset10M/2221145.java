package org.nakedobjects.plugins.dndviewer.viewer.lookup;

import java.awt.event.KeyEvent;
import org.nakedobjects.plugins.dndviewer.BackgroundTask;
import org.nakedobjects.plugins.dndviewer.BackgroundThread;
import org.nakedobjects.plugins.dndviewer.Canvas;
import org.nakedobjects.plugins.dndviewer.Click;
import org.nakedobjects.plugins.dndviewer.ColorsAndFonts;
import org.nakedobjects.plugins.dndviewer.KeyboardAction;
import org.nakedobjects.plugins.dndviewer.Toolkit;
import org.nakedobjects.plugins.dndviewer.View;
import org.nakedobjects.plugins.dndviewer.viewer.border.AbstractBorder;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Color;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Location;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Shape;
import org.nakedobjects.plugins.dndviewer.viewer.drawing.Size;

/**
 * Field border that provides a drop-down list.
 */
public abstract class OpenDropDownBorder extends AbstractBorder {

    private boolean over;

    public OpenDropDownBorder(final View wrappedView) {
        super(wrappedView);
        right = 18;
    }

    protected abstract View createOverlay();

    @Override
    public void draw(final Canvas canvas) {
        final Size size = getSize();
        final int x = size.getWidth() - right + 5 - HPADDING;
        final int y = (size.getHeight() - 6) / 2;
        if (isAvailable()) {
            final Shape triangle = new Shape(0, 0);
            triangle.addVertex(6, 6);
            triangle.addVertex(12, 0);
            canvas.drawShape(triangle, x, y, Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY3));
            if (over) {
                final Color color = over ? Toolkit.getColor(ColorsAndFonts.COLOR_SECONDARY1) : Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY2);
                canvas.drawSolidShape(triangle, x, y, color);
            }
        }
        super.draw(canvas);
    }

    @Override
    public void exited() {
        if (over) {
            markDamaged();
        }
        over = false;
        super.exited();
    }

    @Override
    public void firstClick(final Click click) {
        final float x = click.getLocation().getX() - 2;
        final float boundary = getSize().getWidth() - right;
        if (x >= boundary) {
            if (isAvailable()) {
                open();
            }
        } else {
            super.firstClick(click);
        }
    }

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        maximumSize.contractWidth(HPADDING);
        final Size size = super.getRequiredSize(maximumSize);
        size.extendWidth(HPADDING);
        return size;
    }

    protected boolean isAvailable() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return isAvailable();
    }

    @Override
    public void keyPressed(final KeyboardAction key) {
        if (key.getKeyCode() == KeyEvent.VK_DOWN && isAvailable()) {
            open();
            key.consume();
        }
        super.keyPressed(key);
    }

    @Override
    public void mouseMoved(final Location at) {
        if (at.getX() >= getSize().getWidth() - right) {
            getFeedbackManager().showDefaultCursor();
            if (!over) {
                markDamaged();
            }
            over = true;
        } else {
            if (over) {
                markDamaged();
            }
            over = false;
            super.mouseMoved(at);
        }
    }

    private void open() {
        BackgroundThread.run(this, new BackgroundTask() {

            public void execute() {
                final View overlay = createOverlay();
                final Location location = getView().getAbsoluteLocation();
                location.add(getView().getPadding().getLeft() - 1, getSize().getHeight() + 2);
                overlay.setLocation(location);
                getViewManager().setOverlayView(overlay);
            }

            public String getDescription() {
                return "";
            }

            public String getName() {
                return "Opening lookup";
            }
        });
    }
}
