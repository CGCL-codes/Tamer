package com.simplemonkey.layout;

import com.simplemonkey.layout.ILayoutData;

/**
 * Type to give the border layout manager a clue how to
 * layout its children. The possible values are
 * <ul>
 * <li>Use <code>NORTH</code> to align a Widget at the top of the parent Container.</li>
 * <li>Use <code>SOUTH</code> to align a Widget at the bottom of the parent Container.</li>
 * <li>Use <code>EAST</code> to align a Widget at the right hand side of the parent Container.</li>
 * <li>Use <code>WEST</code> to align a Widget at the left hand side of the parent Container.</li>
 * <li>Use <code>CENTER</code> to place a Widget in the middle of the parent Container.</li>
 * </ul>
 * @see com.simplemonkey.layout.BorderLayout
 * @see org.fenggui.Container
 * 
 * @todo make this BorderLayoutData class an enum! #
 * 
 * @author Johannes Schaback
 */
public class BorderLayoutData implements ILayoutData {

    public BorderLayoutData(int v) {
        value = v;
    }

    protected static final int NORTH_VALUE = 0;

    protected static final int WEST_VALUE = 1;

    protected static final int EAST_VALUE = 2;

    protected static final int SOUTH_VALUE = 3;

    protected static final int CENTER_VALUE = 4;

    public static final BorderLayoutData NORTH = new BorderLayoutData(NORTH_VALUE);

    public static final BorderLayoutData WEST = new BorderLayoutData(WEST_VALUE);

    public static final BorderLayoutData SOUTH = new BorderLayoutData(SOUTH_VALUE);

    public static final BorderLayoutData CENTER = new BorderLayoutData(CENTER_VALUE);

    public static final BorderLayoutData EAST = new BorderLayoutData(EAST_VALUE);

    private int value = NORTH_VALUE;

    public int getValue() {
        return value;
    }
}
