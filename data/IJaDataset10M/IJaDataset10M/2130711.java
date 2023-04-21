package org.nakedobjects.nos.client.dnd;

import org.nakedobjects.nof.core.util.ToString;
import org.nakedobjects.nos.client.dnd.drawing.Location;
import org.nakedobjects.nos.client.dnd.drawing.Offset;
import org.nakedobjects.nos.client.dnd.drawing.Padding;

public class SimpleInternalDrag extends InternalDrag {

    private final Location location;

    private final Location offset;

    private final View view;

    /**
     * Creates a new drag event. The source view has its pickup(), and then, exited() methods called on it.
     * The view returned by the pickup method becomes this event overlay view, which is moved continuously so
     * that it tracks the pointer,
     * 
     * @param view
     *            the view over which the pointer was when this event started
     * @param location
     *            the location within the viewer (the Frame/Applet/Window etc)
     * 
     * TODO combine the two constructors
     */
    public SimpleInternalDrag(final View view, final Location location) {
        this.view = view;
        this.location = new Location(location);
        offset = view.getAbsoluteLocation();
        Padding targetPadding = view.getPadding();
        Padding containerPadding = view.getView().getPadding();
        offset.add(containerPadding.getLeft() - targetPadding.getLeft(), containerPadding.getTop() - targetPadding.getTop());
        this.location.subtract(offset);
    }

    public SimpleInternalDrag(final View view, final Offset off) {
        this.view = view;
        location = new Location();
        offset = new Location(off.getDeltaX(), off.getDeltaY());
        Padding targetPadding = view.getPadding();
        Padding containerPadding = view.getView().getPadding();
        offset.add(containerPadding.getLeft() - targetPadding.getLeft(), containerPadding.getTop() - targetPadding.getTop());
        this.location.subtract(offset);
    }

    public void cancel(final Viewer viewer) {
        view.dragCancel(this);
    }

    public void drag(final View target, final Location location, final int mods) {
        this.location.setX(location.getX());
        this.location.setY(location.getY());
        this.location.subtract(offset);
        view.drag(this);
    }

    public void end(final Viewer viewer) {
        view.dragTo(this);
    }

    /**
     * Gets the location of the pointer relative to the view.
     */
    public Location getLocation() {
        return new Location(location);
    }

    public View getOverlay() {
        return null;
    }

    public String toString() {
        ToString s = new ToString(this, super.toString());
        s.append("location", location);
        s.append("relative", getLocation());
        return s.toString();
    }
}
