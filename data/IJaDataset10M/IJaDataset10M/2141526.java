package org.xteam.box2text.old;

import java.util.List;
import org.xteam.box2text.Frame;
import org.xteam.box2text.Point;
import org.xteam.box2text.Text;
import org.xteam.box2text.TextStruct;

public abstract class Box {

    /**
	 * Compute the text representation of this box hierarchy
	 * 
	 * @return the result text
	 */
    public Text text() {
        return text(new Frame());
    }

    /**
	 * Compute the text representation of this box hierarchy
	 * 
	 * @param rightMargin
	 * @return the result text
	 */
    public Text text(int rightMargin) {
        Frame f = new Frame();
        f = f.setRightMargin(rightMargin);
        return text(f);
    }

    private Text text(Frame f) {
        TextStruct ts = ((IPlaceableBox) normalizeGroups(true).get(0)).place(f, new TextStruct(new Text(), f.point()), OperatorKinds.V);
        return ts.text();
    }

    /**
	 * @return true if this box is empty
	 */
    public abstract boolean isEmpty();

    /**
	 * Remove vertical box from the bow hierarchy.
	 * 
	 * @param removeRow if true R boxes are also removed
	 * @return the new box hierarchy
	 */
    protected Box removeVertical(boolean removeRow) {
        return this;
    }

    /**
	 * Reduce the group operators (G and SL).
	 * 
	 * @param splice false to keep group structure
	 * 
	 * @return the list of boxes resulting from the reduction
	 */
    public abstract List<Box> normalizeGroups(boolean splice);

    /**
	 * Used by I and WD operator. didn't find any definition,
	 * which certainly means that it can contains only one element
	 * at maximum.
	 *  
	 * @param boxes
	 * @param frame
	 * @return
	 */
    protected static int width(List<Box> boxes, Frame frame) {
        if (boxes.isEmpty()) return 0;
        if (boxes.size() > 1) throw new RuntimeException("operator should only contain one element");
        return ((IPlaceableBox) boxes.get(0)).width(frame);
    }

    /**
	 * Used by I and WD operator. didn't find any definition,
	 * which certainly means that it can contains only one element
	 * at maximum.
	 *  
	 * @param boxes
	 * @param frame
	 * @return
	 */
    protected static Point length(List<Box> boxes, Frame frame, int context) {
        if (boxes.isEmpty()) return new Point(0, 0);
        if (boxes.size() > 1) throw new RuntimeException("operator should only contain one element");
        return ((IPlaceableBox) boxes.get(0)).length(frame, context);
    }
}
