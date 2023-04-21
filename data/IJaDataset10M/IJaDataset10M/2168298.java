package pl.org.minions.utils.ui.widgets;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.EnumSet;
import pl.org.minions.utils.ui.PressedKeysMap;

/**
 * A most basic widget implementation. All abstract methods
 * are implemented as empty.
 * <p>
 * Intended to be an invisible containing widget, a node in
 * widget hierarchy.
 */
public class Container extends Widget {

    /**
     * Creates a new container.
     * @param bounds
     *            bounding rectangle (still used for mouse
     *            interception)
     * @param parent
     *            parent widget
     */
    public Container(Rectangle bounds, Widget parent) {
        super(bounds, parent, null);
    }

    /**
     * Creates a new container.
     * @param bounds
     *            bounding rectangle (still used for mouse
     *            interception)
     * @param parent
     *            parent widget
     * @param bufferingOptions
     *            buffer creation flags
     */
    public Container(Rectangle bounds, Widget parent, EnumSet<BufferingOptions> bufferingOptions) {
        super(bounds, parent, bufferingOptions);
    }

    /** {@inheritDoc} */
    @Override
    protected void paintWidget(Painter painter) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void processKeyboardState(PressedKeysMap keysMap) {
    }

    /** {@inheritDoc} */
    @Override
    protected void updateWidget(Point mousePosition, long timeElapsed) {
    }
}
