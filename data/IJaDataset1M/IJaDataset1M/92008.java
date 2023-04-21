package net.sf.lostclan.graphics;

/**
 * LayoutHints hold keys and variable strings associated with LayoutManager.
 * @author Benjamin Tarrant
 * @see LayoutManager
 */
public class LayoutHints {

    /**
     * Key for posistion style of Layout.<br>
     * Example <code>"posistion:relative;"</code>
     */
    public static final String KEY_POSISTION = "posistion";

    /**
     * Indicates that this layout is not dynamic and will not update its position when a parent is moved.
     */
    public static final String VALUE_POSISTION_ABSOLUTE = "absolute";

    /**
     * Indicates that this layout is dynamic and will update its position when a parent is moved.
     */
    public static final String VALUE_POSISTION_RELATIVE = "relative";

    /**
     * Key for Horizontal Alignment style of Layout.
     */
    public static final String KEY_ALIGN_HORIZONTAL = "align-horizontal";

    /**
     * Key for Vertical Alignment style of Layout.
     */
    public static final String KEY_ALIGN_VERTICAL = "align-vertical";

    /**
     * Aligns LayoutManager to left edge of it's parent.
     */
    public static final String VALUE_ALIGN_LEFT = "left";

    /**
     * Aligns LayoutManager to left outside edge of it's parent.
     */
    public static final String VALUE_ALIGN_LEFTOF = "leftof";

    /**
     * Aligns LayoutManager to right edge of it's parent.
     */
    public static final String VALUE_ALIGN_RIGHT = "right";

    /**
     * Aligns LayoutManager to right outside edge of it's parent.
     */
    public static final String VALUE_ALIGN_RIGHTOF = "rightof";

    /**
     * Aligns LayoutManager to top of it's parent.
     */
    public static final String VALUE_ALIGN_TOP = "top";

    /**
     * Aligns LayoutManager above the top of it's parent.
     */
    public static final String VALUE_ALIGN_ABOVE = "above";

    /**
     * Aligns LayoutManager to bottom of it's parent.
     */
    public static final String VALUE_ALIGN_BOTTOM = "bottom";

    /**
     * Aligns LayoutManager below the bottom of it's parent.
     */
    public static final String VALUE_ALIGN_BELOW = "below";

    /**
     * Centers LayoutManager to the center of it's parent.
     */
    public static final String VALUE_ALIGN_CENTER = "center";

    /**
     * Key for width style requires string variable for value.<br>
     * <code>"width:100px|100% or 100"</code>
     */
    public static final String KEY_WIDTH = "width";

    /**
     * Key for height style requires string variable for value.<br>
     * <code>"height:100px|100% or 100"</code>
     */
    public static final String KEY_HEIGHT = "height";

    /**
     * Key for xoffset style requires string variable for value.<br>
     * <code>"xoffset:100px|100% or 100"</code>
     */
    public static final String KEY_XOFFSET = "xoffset";

    /**
     * Key for yoffset style requires string variable for value.<br>
     * <code>"yoffset:100px|100% or 100"</code>
     */
    public static final String KEY_YOFFSET = "yoffset";
}
