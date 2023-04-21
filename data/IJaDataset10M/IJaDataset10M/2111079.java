package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.viewer.skylark.InternalDrag;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.Viewer;
import org.nakedobjects.viewer.skylark.drawing.Bounds;
import org.nakedobjects.viewer.skylark.drawing.Location;
import org.nakedobjects.viewer.skylark.drawing.Size;

public class ResizeDrag extends InternalDrag {

    public static final int BOTTOM = 2;

    public static final int BOTTOM_LEFT = 7;

    public static final int BOTTOM_RIGHT = 8;

    public static final int LEFT = 3;

    public static final int RIGHT = 4;

    public static final int TOP = 1;

    public static final int TOP_LEFT = 5;

    public static final int TOP_RIGHT = 6;

    /**
     * the location of the corner opposite the pointer that will form the resizing rectangle.
     */
    private final Location anchor;

    private final int direction;

    private final ViewResizeOutline overlay;

    private final View view;

    private final Size minimumSize;

    private final Size maximumSize;

    public ResizeDrag(final View view, final Bounds resizeArea, final int direction) {
        this(view, resizeArea, direction, null, null);
    }

    public ResizeDrag(final View view, final Bounds resizeArea, final int direction, final Size minimumSize, final Size maximumSize) {
        this.view = view;
        this.direction = direction;
        this.anchor = resizeArea.getLocation();
        this.minimumSize = minimumSize;
        this.maximumSize = maximumSize;
        overlay = new ViewResizeOutline(resizeArea);
        overlay.setLocation(resizeArea.getLocation());
    }

    public void cancel(final Viewer viewer) {
        view.dragCancel(this);
    }

    public void drag(final View target, final Location location, final int mods) {
        switch(direction) {
            case TOP:
                extendUpward(location);
                break;
            case BOTTOM:
                extendDownward(location);
                break;
            case LEFT:
                extendLeft(location);
                break;
            case RIGHT:
                extendRight(location);
                break;
            case TOP_RIGHT:
                extendRight(location);
                extendUpward(location);
                break;
            case BOTTOM_RIGHT:
                extendRight(location);
                extendDownward(location);
                break;
            case TOP_LEFT:
                extendLeft(location);
                extendUpward(location);
                break;
            case BOTTOM_LEFT:
                extendLeft(location);
                extendDownward(location);
                break;
            default:
                break;
        }
    }

    public void end(final Viewer viewer) {
        view.dragTo(this);
        view.getViewManager().clearOverlayView(view);
    }

    private void extendDownward(final Location location) {
        overlay.markDamaged();
        int height = location.getY() - anchor.getY();
        int width = overlay.getSize().getWidth();
        overlay.setSize(new Size(width, height));
        overlay.markDamaged();
    }

    private void extendLeft(final Location location) {
        overlay.markDamaged();
        int height = overlay.getSize().getHeight();
        int width = anchor.getX() - location.getX();
        overlay.setSize(new Size(width, height));
        int x = anchor.getX() - width;
        int y = anchor.getY();
        overlay.setBounds(new Bounds(x, y, width, height));
        overlay.markDamaged();
    }

    private void extendRight(final Location location) {
        overlay.markDamaged();
        int height = overlay.getSize().getHeight();
        int width = location.getX() - anchor.getX();
        if (maximumSize != null && width > maximumSize.getWidth()) {
            width = maximumSize.getWidth();
        }
        if (minimumSize != null && width < minimumSize.getWidth()) {
            width = minimumSize.getWidth();
        }
        overlay.setSize(new Size(width, height));
        overlay.markDamaged();
    }

    private void extendUpward(final Location location) {
        overlay.markDamaged();
        int height = anchor.getY() - location.getY();
        int width = overlay.getSize().getWidth();
        overlay.setSize(new Size(width, height));
        int x = anchor.getX();
        int y = anchor.getY() - height;
        overlay.setBounds(new Bounds(x, y, width, height));
        overlay.markDamaged();
    }

    public int getDirection() {
        return direction;
    }

    public Location getLocation() {
        Size size = overlay.getSize();
        return new Location(size.getWidth(), size.getHeight());
    }

    public View getOverlay() {
        return overlay;
    }
}
