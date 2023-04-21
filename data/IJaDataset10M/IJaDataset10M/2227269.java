package org.eclipse.bpel.common.ui.decorator;

import org.eclipse.draw2d.PositionConstants;

/**
 * Useful constants for markers and for the DecorationLayout
 */
public interface IMarkerConstants {

    /**
	 * @deprecated use IModelMarkerConstants.DECORATION_GRAPHICAL_MARKER_ANCHOR_POINT_ATTR
	 */
    public final String MARKER_ANCHORPOINT = "anchorPoint";

    public final String MARKER_ANCHORPOINT_TOP_CENTRE = "TOP_CENTRE";

    public final String MARKER_ANCHORPOINT_BOTTOM_CENTRE = "BOTTOM_CENTRE";

    public final String MARKER_ANCHORPOINT_LEFT = "LEFT";

    public final String MARKER_ANCHORPOINT_RIGHT = "RIGHT";

    public final String MARKER_ANCHORPOINT_CENTRE = "CENTRE";

    public final String MARKER_ANCHORPOINT_TOP_LEFT = "TOP_LEFT";

    public final String MARKER_ANCHORPOINT_TOP_RIGHT = "TOP_RIGHT";

    public final String MARKER_ANCHORPOINT_BOTTOM_LEFT = "BOTTOM_LEFT";

    public final String MARKER_ANCHORPOINT_BOTTOM_RIGHT = "BOTTOM_RIGHT";

    public final String MARKER_ANCHORPOINT_SOURCE = "SOURCE";

    public final String MARKER_ANCHORPOINT_TARGET = "TARGET";

    public static final Integer CENTER = new Integer(PositionConstants.CENTER);

    public static final Integer TOP = new Integer(PositionConstants.TOP);

    public static final Integer BOTTOM = new Integer(PositionConstants.BOTTOM);

    public static final Integer LEFT = new Integer(PositionConstants.LEFT);

    public static final Integer RIGHT = new Integer(PositionConstants.RIGHT);

    public static final Integer TOP_LEFT = new Integer(PositionConstants.TOP | PositionConstants.LEFT);

    public static final Integer TOP_RIGHT = new Integer(PositionConstants.TOP | PositionConstants.RIGHT);

    public static final Integer BOTTOM_LEFT = new Integer(PositionConstants.BOTTOM | PositionConstants.LEFT);

    public static final Integer BOTTOM_RIGHT = new Integer(PositionConstants.BOTTOM | PositionConstants.RIGHT);

    public static final int PRIORITY_CURRENT_EXECUTION_INDICATOR = 80;

    public static final int PRIORITY_STATUS_EXECUTION_INDICATOR = 70;

    public static final int PRIORITY_ERROR_INDICATOR = 60;

    public static final int PRIORITY_WARNING_INDICATOR = 50;

    public static final int PRIORITY_OCCURANCE_INDICATOR = 40;

    public static final int PRIORITY_BREAKPOINT_INDICATOR = 30;

    public static final int PRIORITY_INFO_INDICATOR = 20;

    public static final int PRIORITY_SEARCH_RESULT_INDICATOR = 10;

    public static final int PRIORITY_DEFAULT = 5;

    public static final int PRIORITY_NONE = 0;
}
