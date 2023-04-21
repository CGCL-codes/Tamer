package org.dbe.composer.wfengine.bpeladmin.war.graph.bpel;

import org.dbe.composer.wfengine.bpeladmin.war.graph.SdlGraphProperties;

/**
 * Layout preferences used by the Autolayout algorithm.
 */
public class SdlLayoutPrefs {

    /** Indicates a vertical layout direction */
    public static int LAYOUT_VERTICAL = 0;

    /** Indicates a horizontal layout direction */
    public static int LAYOUT_HORIZONTAL = 1;

    /** Top margin */
    private int mMarginTop;

    /** Bottom margin */
    private int mMarginBottom;

    /** Left margin */
    private int mMarginLeft;

    /** Right margin */
    private int mMarginRight;

    /** spacing between levels */
    private int mInterLevelSpacing;

    /** spacing between activity nodes */
    private int mInterNodeSpacing;

    /** layout direction (horizontal or vertical */
    private int mLayoutDirection;

    /** spacing between partner lanes */
    private int mInterLaneSpacing;

    /**
     * Constructor which loads the plugin preferences for the layout and stores
     * them for later use.
     */
    public SdlLayoutPrefs() {
        mLayoutDirection = LAYOUT_VERTICAL;
        mMarginTop = 5;
        mMarginBottom = 5;
        mMarginLeft = 5;
        mMarginRight = 5;
        mInterLevelSpacing = 20;
        mInterNodeSpacing = 20;
        mInterLaneSpacing = 10;
        readLayoutPreferences();
    }

    /**
     * Getter method for inter level spacing value used during layout.
     * @return number of pixels used between levels during layout
     */
    public int getInterLevelSpacing() {
        return mInterLevelSpacing;
    }

    /**
     * Getter method for inter node spacing value used during layout.
     * @return number of pixels used between nodes during layout
     */
    public int getInterNodeSpacing() {
        return mInterNodeSpacing;
    }

    /**
     * Getter method for inter lane spacing value used during layout.
     * @return number of pixels used between lanes during layout
     */
    public int getInterLaneSpacing() {
        return mInterLaneSpacing;
    }

    /**
     * Getter method to return the layout orientation.
     * @return numeric constant of either LAYOUT_VERTICAL or LAYOUT_HORIZONTAL
     */
    public int getLayoutDirection() {
        return mLayoutDirection;
    }

    /**
     * Getter method to return the top margin used during layout.
     * @return the margin in pixels for the top border of the component
     */
    public int getMarginTop() {
        return mMarginTop;
    }

    /**
     * Getter method to return the bottom margin used during layout.
     * @return the margin in pixels for the bottom border of the component
     */
    public int getMarginBottom() {
        return mMarginBottom;
    }

    /**
     * Getter method to return the left margin used during layout.
     * @return the margin in pixels for the left border of the component
     */
    public int getMarginLeft() {
        return mMarginLeft;
    }

    /**
     * Getter method to return the right margin used during layout.
     * @return the margin in pixels for the right border of the component
     */
    public int getMarginRight() {
        return mMarginRight;
    }

    /**
     * Read the layout preferences from file.
     */
    protected void readLayoutPreferences() {
        mMarginTop = 5;
        mMarginBottom = 5;
        mMarginLeft = 5;
        mMarginRight = 5;
        mInterLevelSpacing = 20;
        mInterNodeSpacing = 20;
        mInterLaneSpacing = 10;
        SdlGraphProperties props = SdlGraphProperties.getInstance();
        mMarginTop = props.getPropertyInt(SdlGraphProperties.LAYOUT_MARGIN_TOP, mMarginTop);
        mMarginBottom = props.getPropertyInt(SdlGraphProperties.LAYOUT_MARGIN_BOTTOM, mMarginBottom);
        mMarginLeft = props.getPropertyInt(SdlGraphProperties.LAYOUT_MARGIN_LEFT, mMarginLeft);
        mMarginRight = props.getPropertyInt(SdlGraphProperties.LAYOUT_MARGIN_RIGHT, mMarginRight);
        mInterLevelSpacing = props.getPropertyInt(SdlGraphProperties.LAYOUT_LEVEL_SPACING, mInterLevelSpacing);
        mInterNodeSpacing = props.getPropertyInt(SdlGraphProperties.LAYOUT_NODE_SPACING, mInterNodeSpacing);
    }
}
