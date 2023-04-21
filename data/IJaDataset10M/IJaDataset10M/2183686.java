package com.xiledsystems.AlternateJavaBridgelib.components;

import com.xiledsystems.AlternateJavaBridgelib.components.common.ComponentConstants;

/**
 * Interface for Simple components.
 *
 */
public abstract interface Component {

    /**
   * Returns the dispatch delegate that is responsible for dispatching events
   * for this component.
   */
    public HandlesEventDispatching getDispatchDelegate();

    static final int ALIGNMENT_NORMAL = 0;

    static final int ALIGNMENT_CENTER = 1;

    static final int ALIGNMENT_OPPOSITE = 2;

    static final int COLOR_NONE = 0x00FFFFFF;

    static final int COLOR_BLACK = 0xFF000000;

    static final int COLOR_BLUE = 0xFF0000FF;

    static final int COLOR_CYAN = 0xFF00FFFF;

    static final int COLOR_DKGRAY = 0xFF444444;

    static final int COLOR_GRAY = 0xFF888888;

    static final int COLOR_GREEN = 0xFF00FF00;

    static final int COLOR_LTGRAY = 0xFFCCCCCC;

    static final int COLOR_MAGENTA = 0xFFFF00FF;

    static final int COLOR_ORANGE = 0xFFFFC800;

    static final int COLOR_PINK = 0xFFFFAFAF;

    static final int COLOR_RED = 0xFFFF0000;

    static final int COLOR_WHITE = 0xFFFFFFFF;

    static final int COLOR_YELLOW = 0xFFFFFF00;

    static final int COLOR_DEFAULT = 0x00000000;

    static final String DEFAULT_VALUE_COLOR_NONE = "&H00FFFFFF";

    static final String DEFAULT_VALUE_COLOR_BLACK = "&HFF000000";

    static final String DEFAULT_VALUE_COLOR_BLUE = "&HFF0000FF";

    static final String DEFAULT_VALUE_COLOR_CYAN = "&HFF00FFFF";

    static final String DEFAULT_VALUE_COLOR_DKGRAY = "&HFF444444";

    static final String DEFAULT_VALUE_COLOR_GRAY = "&HFF888888";

    static final String DEFAULT_VALUE_COLOR_GREEN = "&HFF00FF00";

    static final String DEFAULT_VALUE_COLOR_LTGRAY = "&HFFCCCCCC";

    static final String DEFAULT_VALUE_COLOR_MAGENTA = "&HFFFF00FF";

    static final String DEFAULT_VALUE_COLOR_ORANGE = "&HFFFFC800";

    static final String DEFAULT_VALUE_COLOR_PINK = "&HFFFFAFAF";

    static final String DEFAULT_VALUE_COLOR_RED = "&HFFFF0000";

    static final String DEFAULT_VALUE_COLOR_WHITE = "&HFFFFFFFF";

    static final String DEFAULT_VALUE_COLOR_YELLOW = "&HFFFFFF00";

    static final String DEFAULT_VALUE_COLOR_DEFAULT = "&H00000000";

    static final float FONT_DEFAULT_SIZE = 14;

    static final int LAYOUT_ORIENTATION_HORIZONTAL = ComponentConstants.LAYOUT_ORIENTATION_HORIZONTAL;

    static final int LAYOUT_ORIENTATION_VERTICAL = ComponentConstants.LAYOUT_ORIENTATION_VERTICAL;

    static final int TYPEFACE_DEFAULT = 0;

    static final int TYPEFACE_SANSSERIF = 1;

    static final int TYPEFACE_SERIF = 2;

    static final int TYPEFACE_MONOSPACE = 3;

    static final int LENGTH_PREFERRED = -1;

    static final int LENGTH_FILL_PARENT = -2;

    static final int LENGTH_UNKNOWN = -3;

    static final int DIRECTION_NORTH = 1;

    static final int DIRECTION_NORTHEAST = 2;

    static final int DIRECTION_EAST = 3;

    static final int DIRECTION_SOUTHEAST = 4;

    static final int DIRECTION_SOUTH = -1;

    static final int DIRECTION_SOUTHWEST = -2;

    static final int DIRECTION_WEST = -3;

    static final int DIRECTION_NORTHWEST = -4;

    static final int DIRECTION_NONE = 0;

    static final int DIRECTION_MIN = -4;

    static final int DIRECTION_MAX = 4;
}
