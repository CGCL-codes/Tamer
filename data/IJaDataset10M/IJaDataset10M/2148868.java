package de.hpi.eworld.networkview;

import java.awt.Cursor;
import java.awt.Point;
import de.hpi.eworld.core.resources.ResourceLoader;

/**
 * The <code>GraphCursor</code> class encapsulates the custom <code>Cursor</code> objects used in the <code>GraphPlugin</code>.<br>
 * It is by itself no <code>java.awt.Cursor</code>! It is merely used as a data store.
 * @author Stefan.Schaefer
 *
 */
public class GraphCursor {

    /**
	 * The systems default cursor.<br>
	 * Set when the map is exited
	 */
    public static final Cursor DEFAULT = Cursor.getDefaultCursor();

    /**
	 * "Not-allowed" Cursor icon.<br>
	 * Set on dragging an item over a forbidden drop target
	 */
    public static final Cursor FORBIDDEN = ResourceLoader.createCursorFromLocalResource(GraphPlugin.class, "cursor-forbidden.png", new Point(13, 13));

    /**
	 * Default cursor icon on GraphView<br>
	 * Set on mouseEntered of map and mouseExited of items and buttons
	 */
    public static final Cursor HAND_OPEN = ResourceLoader.createCursorFromLocalResource(GraphPlugin.class, "cursor-openhand.png", new Point(8, 7));

    /**
	 * "Drag" cursor icon<br>
	 * Set on dragging an item or the map
	 */
    public static final Cursor HAND_CLOSED = ResourceLoader.createCursorFromLocalResource(GraphPlugin.class, "cursor-closedhand.png", new Point(8, 7));

    /**
	 * "Link" cursor icon<br>
	 * Set on hovering an item or button
	 */
    public static final Cursor HAND_POINTING = ResourceLoader.createCursorFromLocalResource(GraphPlugin.class, "cursor-hand.png", new Point(9, 1));

    /**
	 * "Resize" Cursor icon with arrows in every direction.<br>
	 * Set on moving a single Polygon Point
	 */
    public static final Cursor RESIZE_ALL = ResourceLoader.createCursorFromLocalResource(GraphPlugin.class, "cursor-sizeall.png", new Point(12, 12));

    /**
	 * "Resize" cursor icon.<br>
	 * Set when hovering an EventView at its northern border. 
	 */
    public static final Cursor RESIZE_N = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);

    /**
	 * "Resize" cursor icon.<br>
	 * Set when hovering an EventView at its north-eastern border. 
	 */
    public static final Cursor RESIZE_NE = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);

    /**
	 * "Resize" cursor icon.<br>
	 * Set when hovering an EventView at its eastern border. 
	 */
    public static final Cursor RESIZE_E = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);

    /**
	 * "Resize" cursor icon.<br>
	 * Set when hovering an EventView at its south-eastern border. 
	 */
    public static final Cursor RESIZE_SE = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);

    /**
	 * "Resize" cursor icon.<br>
	 * Set when hovering an EventView at its southern border. 
	 */
    public static final Cursor RESIZE_S = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);

    /**
	 * "Resize" cursor icon.<br>
	 * Set when hovering an EventView at its south-western border. 
	 */
    public static final Cursor RESIZE_SW = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);

    /**
	 * "Resize" cursor icon.<br>
	 * Set when hovering an EventView at its western border. 
	 */
    public static final Cursor RESIZE_W = Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR);

    /**
	 * "Resize" cursor icon.<br>
	 * Set when hovering an EventView at its north-western border. 
	 */
    public static final Cursor RESIZE_NW = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);

    /**
	 * Being a data class, it is not necessary to instantiate this class
	 * @Deprecated
	 */
    private GraphCursor() {
    }
}
