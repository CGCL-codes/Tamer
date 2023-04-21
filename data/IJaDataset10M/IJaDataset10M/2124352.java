package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewDrag;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.WindowControl;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Color;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Offset;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;

public abstract class AbstractWindowBorder extends AbstractBorder {

    protected BorderDrawing borderRender = new SwingStyleWindowBorder();

    protected WindowControl controls[];

    private WindowControl overControl;

    public AbstractWindowBorder(final View enclosedView) {
        super(enclosedView);
        left = borderRender.getLeft();
        right = borderRender.getRight();
        top = borderRender.getTop();
        bottom = borderRender.getBottom();
    }

    @Override
    public void debugDetails(final DebugString debug) {
        super.debugDetails(debug);
        borderRender.debugDetails(debug);
        if (controls.length > 0) {
            debug.appendln("controls:-");
            debug.indent();
            for (WindowControl control : controls) {
                debug.append(control);
                debug.appendln();
            }
            debug.unindent();
        }
    }

    @Override
    public Drag dragStart(final DragStart drag) {
        if (overBorder(drag.getLocation())) {
            final Location location = drag.getLocation();
            final View dragOverlay = Toolkit.getViewFactory().createDragViewOutline(getView());
            return new ViewDrag(this, new Offset(location.getX(), location.getY()), dragOverlay);
        } else {
            return super.dragStart(drag);
        }
    }

    protected void setControls(final WindowControl[] controls) {
        this.controls = controls;
    }

    @Override
    public void setSize(final Size size) {
        super.setSize(size);
        layoutControls(size);
    }

    @Override
    public void setBounds(final Bounds bounds) {
        super.setBounds(bounds);
        layoutControls(bounds.getSize());
    }

    private void layoutControls(final Size size) {
        borderRender.layoutControls(size, controls);
    }

    @Override
    public void draw(final Canvas canvas) {
        final Bounds bounds = getBounds();
        Color color = Toolkit.getColor(ColorsAndFonts.COLOR_WINDOW + "." + getSpecification().getName());
        canvas.drawSolidRectangle(1, 1, bounds.getWidth() - 2, bounds.getHeight() - 2, color);
        final boolean hasFocus = containsFocus();
        final ViewState state = getState();
        borderRender.draw(canvas, getSize(), hasFocus, state, controls, title());
        for (int i = 0; controls != null && i < controls.length; i++) {
            final Canvas controlCanvas = canvas.createSubcanvas(controls[i].getBounds());
            controls[i].draw(controlCanvas);
        }
        super.draw(canvas);
    }

    protected abstract String title();

    @Override
    public Size getRequiredSize(final Size maximumSize) {
        final Size size = super.getRequiredSize(maximumSize);
        borderRender.getRequiredSize(size, title(), controls);
        return size;
    }

    @Override
    public void secondClick(final Click click) {
        final View control = overControl(click.getLocation());
        if (control == null) {
            super.secondClick(click);
        }
    }

    @Override
    public void thirdClick(final Click click) {
        final View control = overControl(click.getLocation());
        if (control == null) {
            super.thirdClick(click);
        }
    }

    @Override
    public void firstClick(final Click click) {
        final View control = overControl(click.getLocation());
        if (control == null) {
            if (overBorder(click.getLocation())) {
                final Workspace workspace = getWorkspace();
                if (workspace != null) {
                    if (click.button2()) {
                        workspace.lower(getView());
                    } else if (click.button1()) {
                        workspace.raise(getView());
                    }
                }
            } else {
                super.firstClick(click);
            }
        } else {
            control.firstClick(click);
        }
    }

    @Override
    public void mouseMoved(Location at) {
        final WindowControl control = (WindowControl) overControl(at);
        if (control != null) {
            if (control != overControl) {
                control.entered();
                overControl = control;
                return;
            }
        } else {
            if (control != overControl) {
                overControl.exited();
                overControl = null;
                return;
            }
        }
        super.mouseMoved(at);
    }

    private View overControl(final Location location) {
        for (int i = 0; i < controls.length; i++) {
            final WindowControl control = controls[i];
            if (control.getBounds().contains(location)) {
                return control;
            }
        }
        return null;
    }
}
