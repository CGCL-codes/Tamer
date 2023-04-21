package cookxml.cookswing.helper;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import cookxml.core.interfaces.Helper;

/**
 * Helper class for constructing an EmptyBorder object.
 *
 * @cxhelper javax.swing.border.EmptyBorder
 * 
 * @author Heng Yuan
 * @version $Id: EmptyBorderHelper.java 215 2007-06-06 03:59:41Z coconut $
 * @since CookSwing 1.0
 */
public class EmptyBorderHelper implements Helper {

    /** The border insets top part. */
    public int top = -1;

    /** The border insets left part. */
    public int left = -1;

    /** The border insets bottom part. */
    public int bottom = -1;

    /** The border insets right part. */
    public int right = -1;

    /** The border insets. */
    public Insets insets;

    public Object getFinalObject() {
        if (top >= 0 && left >= 0 && bottom >= 0 && right >= 0) return BorderFactory.createEmptyBorder(top, left, bottom, right);
        if (insets != null) return new EmptyBorder(insets);
        return BorderFactory.createEmptyBorder();
    }

    /**
	 * Set the border insets.
	 *
	 * @param	insets
	 * 			the border insets.
	 */
    public void add(Insets insets) {
        this.insets = insets;
    }
}
