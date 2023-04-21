package org.makagiga.commons;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/** A scroll pane (pane with scroll bars). */
public class MScrollPane extends JScrollPane {

    /**
	 * Component will be dragged/moved by the mouse inside the scroll pane.
	 */
    public static final int DRAG_SCROLLER = 1 << 0;

    public static final int NO_BORDER = 1 << 1;

    private static MouseWheelListener sharedWheelListener;

    /**
	 * Constructs a scroll pane.
	 * @param component A component to add to the scroll pane
	 */
    public MScrollPane(final JComponent component) {
        this(component, 0);
    }

    /**
	 * Constructs a scroll pane.
	 * @param component A component to add to the scroll pane
	 * @param flags The flags
	 */
    public MScrollPane(final JComponent component, final int flags) {
        super(component);
        Flags f = new Flags(flags);
        if (f.isSet(DRAG_SCROLLER)) installDragScroller(component);
        if (f.isSet(NO_BORDER)) setBorder(null);
        if (sharedWheelListener == null) {
            sharedWheelListener = new MouseWheelListener() {

                public void mouseWheelMoved(final MouseWheelEvent e) {
                    Object source = e.getSource();
                    if (source instanceof JScrollBar) {
                        JScrollBar scrollBar = (JScrollBar) source;
                        if (scrollBar.isEnabled() && (scrollBar.getOrientation() == JScrollBar.HORIZONTAL)) MAction.fire(e, "negativeUnitIncrement", "positiveUnitIncrement", scrollBar);
                    }
                }
            };
        }
        JScrollBar scrollBar = getHorizontalScrollBar();
        if (scrollBar != null) scrollBar.addMouseWheelListener(sharedWheelListener);
    }

    /**
	 * Scrolls the component.
	 * @param component A component to scroll
	 * @param x A horizontal offset
	 * @param x A vertical offset
	 */
    public static void autoScroll(final JComponent component, final int x, final int y) {
        component.scrollRectToVisible(new Rectangle(x, y, 1, 1));
    }

    public static MScrollPane getScrollPane(final JComponent component) {
        JViewport viewport = getViewport(component);
        if (viewport == null) return null;
        Component parent = viewport.getParent();
        return (parent instanceof MScrollPane) ? MScrollPane.class.cast(parent) : null;
    }

    /**
	 * Returns the @c JViewport associated with @p component, or @c null.
	 */
    public static JViewport getViewport(final JComponent component) {
        Container parent = component.getParent();
        return (parent instanceof JViewport) ? (JViewport) parent : null;
    }

    public static DragScroller installDragScroller(final JComponent component) {
        return new DragScroller(component);
    }

    /**
	 * Scrolls a component.
	 *
	 * Keyboard info:
	 * - Alt - scroll horizontally
	 * - Ctrl - double scroll speed
	 *
	 * @param component A component to scroll
	 * @param e A mouse wheel event
	 */
    public static void mouseWheelScroll(final JComponent component, final MouseWheelEvent e) {
        if (component == null) return;
        if (e == null) return;
        Rectangle r = component.getVisibleRect();
        if (r == null) return;
        int speed = (e.getWheelRotation() * 30);
        if (e.isControlDown()) speed *= 2;
        if (e.isAltDown()) r.translate(speed, 0); else r.translate(0, speed);
        component.scrollRectToVisible(r);
    }

    public static void scrollToBottom(final JComponent c, final boolean resetX) {
        doScroll(c, false, resetX);
    }

    public static void scrollToTop(final JComponent c, final boolean resetX) {
        doScroll(c, true, resetX);
    }

    /**
	 * @since 2.2
	 */
    public void setScrollBarVisible(final boolean value) {
        setHorizontalScrollBarPolicy(value ? HORIZONTAL_SCROLLBAR_AS_NEEDED : HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(value ? VERTICAL_SCROLLBAR_AS_NEEDED : VERTICAL_SCROLLBAR_NEVER);
    }

    public void setScrollSpeed(final int value) {
        getHorizontalScrollBar().setUnitIncrement(value);
        getVerticalScrollBar().setUnitIncrement(value);
    }

    private static void doScroll(final JComponent c, final boolean top, final boolean resetX) {
        JViewport v = (c instanceof JScrollPane) ? JScrollPane.class.cast(c).getViewport() : getViewport(c);
        if (v != null) {
            Point p = v.getViewPosition();
            if (resetX) p.x = 0;
            p.y = top ? 0 : v.getViewSize().height;
            v.setViewPosition(p);
        }
    }

    public static final class DragScroller {

        private Point startDrag = new Point();

        public DragScroller(final JComponent component) {
            MMouseAdapter mouseAdapter = new MMouseAdapter() {

                @Override
                public void mouseDragged(final MouseEvent e) {
                    int dx = startDrag.x - e.getX();
                    int dy = startDrag.y - e.getY();
                    JViewport viewport = MScrollPane.getViewport(component);
                    Point viewPosition = viewport.getViewPosition();
                    viewPosition.translate(dx, dy);
                    viewport.setViewPosition(viewPosition);
                    e.consume();
                }

                @Override
                public void mousePressed(final MouseEvent e) {
                    startDrag = e.getPoint();
                    UI.setStyle("cursor: pointer", component);
                }

                @Override
                public void mouseReleased(final MouseEvent e) {
                    startDrag.setLocation(0, 0);
                    UI.setStyle("cursor: default", component);
                }
            };
            component.addMouseListener(mouseAdapter);
            component.addMouseMotionListener(mouseAdapter);
        }
    }
}
