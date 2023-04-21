package org.formaria.swing.layout;

/**
 * Constraint specification for the GuidLayout. The constraints are references to the guide to which an individual edge of a component binds
 * <p> Copyright (c) Formaria Ltd., 2008</p>
 * <p> $Revision: 1.2 $</p>
 * <p> License: see License.txt</p>
 */
public class GuideConstraint {

    /**
   * the left guide id
   */
    public int left;

    /**
   * the right guide id
   */
    public int right;

    /**
   * the top guide id
   */
    public int top;

    /**
   * the bottom guide id
   */
    public int bottom;

    /**
   * Creates a new instance of GuideConstraint
   * @param l left or x constraint/guide id
   * @param t top or y constraint/guide id
   * @param r right
   * @param b bottom
   */
    public GuideConstraint(int l, int t, int r, int b) {
        left = l;
        top = t;
        right = r;
        bottom = b;
    }

    /**
   * the guide constraint as a string
   * @return the constraints as a string value
   */
    public String toString() {
        return "" + left + "," + top + "," + right + "," + bottom;
    }
}
