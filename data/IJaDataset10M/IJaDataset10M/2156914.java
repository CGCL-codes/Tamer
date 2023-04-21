package org.nakedobjects.nos.client.dnd.view.simple;

import java.util.Vector;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.NakedObjectRuntimeException;
import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.nof.core.util.DebugString;
import org.nakedobjects.nof.core.util.ToString;
import org.nakedobjects.nos.client.dnd.Canvas;
import org.nakedobjects.nos.client.dnd.CompositeViewBuilder;
import org.nakedobjects.nos.client.dnd.CompositeViewSpecification;
import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.FocusManager;
import org.nakedobjects.nos.client.dnd.Toolkit;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.ViewAreaType;
import org.nakedobjects.nos.client.dnd.ViewAxis;
import org.nakedobjects.nos.client.dnd.drawing.Bounds;
import org.nakedobjects.nos.client.dnd.drawing.Location;
import org.nakedobjects.nos.client.dnd.drawing.Padding;
import org.nakedobjects.nos.client.dnd.drawing.Size;

public class CompositeView extends ObjectView {

    private static final Logger LOG = Logger.getLogger(CompositeView.class);

    private int buildCount = 0;

    private CompositeViewBuilder builder;

    private boolean buildInvalid = true;

    private boolean canDragView = true;

    private int layoutCount = 0;

    private boolean layoutInvalid = true;

    protected Vector views;

    private FocusManager focusManager;

    public CompositeView(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        super(content, specification, axis);
        views = new Vector();
        builder = specification.getSubviewBuilder();
    }

    public void refresh() {
        View views[] = getSubviews();
        for (int i = 0; i < views.length; i++) {
            views[i].refresh();
        }
    }

    public void addView(final View view) {
        LOG.debug("adding " + view + " to " + this);
        views.addElement(view);
        view.setParent(getView());
        invalidateLayout();
    }

    public boolean canDragView() {
        return canDragView;
    }

    public String debugDetails() {
        DebugString b = new DebugString();
        b.append(super.debugDetails());
        b.appendTitle("Composite view");
        b.appendln("Built", (buildInvalid ? "no" : "yes") + ", " + buildCount + " builds");
        b.appendln("Laid out:  " + (layoutInvalid ? "no" : "yes") + ", " + layoutCount + " layouts");
        b.appendln("Subviews");
        View views[] = getSubviews();
        b.indent();
        for (int i = 0; i < views.length; i++) {
            View subview = views[i];
            b.appendln(subview.getSpecification().getName());
            b.indent();
            b.appendln("Bounds", subview.getBounds());
            b.appendln("Required size ", subview.getRequiredSize(new Size()));
            b.appendln("Content", subview.getContent().getId());
            b.unindent();
        }
        b.unindent();
        b.append("\n");
        return b.toString();
    }

    public void dispose() {
        View views[] = getSubviews();
        for (int i = 0; i < views.length; i++) {
            views[i].dispose();
        }
        super.dispose();
    }

    public void draw(final Canvas canvas) {
        View views[] = getSubviews();
        for (int i = 0; i < views.length; i++) {
            View subview = views[i];
            Bounds bounds = subview.getBounds();
            if (Toolkit.debug) {
                LOG.debug("compare: " + bounds + "  " + canvas);
            }
            if (canvas.overlaps(bounds)) {
                Canvas subCanvas = canvas.createSubcanvas(bounds.getX(), bounds.getY(), bounds.getWidth() - 0, bounds.getSize().getHeight());
                if (Toolkit.debug) {
                    LOG.debug("-- repainting " + subview);
                    LOG.debug("subcanvas " + subCanvas);
                }
                subview.draw(subCanvas);
                if (Toolkit.debug) {
                }
            }
        }
    }

    public int getBaseline() {
        View[] e = getSubviews();
        if (e.length == 0) {
            return 14;
        } else {
            View subview = e[0];
            return subview.getBaseline();
        }
    }

    public FocusManager getFocusManager() {
        return focusManager == null ? super.getFocusManager() : focusManager;
    }

    public Size getMaximumSize() {
        Size size = builder.getRequiredSize(this);
        size.extend(getPadding());
        size.ensureHeight(1);
        return size;
    }

    public View[] getSubviews() {
        if (buildInvalid) {
            buildInvalid = false;
            getFeedbackManager().setBusy(this, null);
            builder.build(getView());
            buildCount++;
            getFeedbackManager().clearBusy(this);
        }
        View v[] = new View[views.size()];
        views.copyInto(v);
        return v;
    }

    public void invalidateContent() {
        buildInvalid = true;
        invalidateLayout();
    }

    public void invalidateLayout() {
        layoutInvalid = true;
        super.invalidateLayout();
    }

    /**
     * The default layout for composite views, which asks each subview to lay itself out first before asking
     * its own builder to layout its own views. The act of laying out the children first ensures that the
     * parent is big enough to accommodate all its children.
     */
    public void layout(final Size maximumSize) {
        if (layoutInvalid) {
            getFeedbackManager().setBusy(this, null);
            layoutInvalid = false;
            layoutCount++;
            markDamaged();
            builder.layout(getView(), new Size(maximumSize));
            markDamaged();
            getFeedbackManager().clearBusy(this);
        }
    }

    protected boolean isLayoutInvalid() {
        return layoutInvalid;
    }

    public View subviewFor(final Location location) {
        Location l = new Location(location);
        Padding padding = getPadding();
        l.subtract(padding.getLeft(), padding.getTop());
        View views[] = getSubviews();
        for (int i = views.length - 1; i >= 0; i--) {
            if (views[i].getBounds().contains(l)) {
                return views[i];
            }
        }
        return null;
    }

    public View pickupView(final Location location) {
        return canDragView ? super.pickupView(location) : null;
    }

    public void removeView(final View view) {
        if (views.contains(view)) {
            LOG.debug("removing " + view + " from " + this);
            views.removeElement(view);
            markDamaged();
            invalidateLayout();
        } else {
            throw new NakedObjectRuntimeException(view + " not in " + getView());
        }
    }

    public void replaceView(final View toReplace, final View replacement) {
        for (int i = 0; i < views.size(); i++) {
            if (views.elementAt(i) == toReplace) {
                replacement.setParent(getView());
                replacement.setLocation(toReplace.getLocation());
                views.insertElementAt(replacement, i);
                invalidateLayout();
                toReplace.dispose();
                return;
            }
        }
        throw new NakedObjectRuntimeException(toReplace + " not found to replace");
    }

    public void setCanDragView(final boolean canDragView) {
        this.canDragView = canDragView;
    }

    public void setFocusManager(final FocusManager focusManager) {
        this.focusManager = focusManager;
    }

    public String toString() {
        ToString to = new ToString(this, getId());
        to.append("type", getSpecification().getName());
        return to.toString();
    }

    public void update(final Naked object) {
        LOG.debug("update notify on " + this);
        invalidateContent();
    }

    public ViewAreaType viewAreaType(final Location location) {
        View subview = subviewFor(location);
        if (subview == null) {
            return ViewAreaType.VIEW;
        } else {
            location.subtract(subview.getLocation());
            return subview.viewAreaType(location);
        }
    }
}
