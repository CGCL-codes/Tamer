package structure.positionModel.squarifiedTreemaps;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import structure.drawElement.DrawElement;
import structure.drawElement.RectangleElement;

/**
 * <p>
 * This Class stores a row of {@link DrawElement}s. With this it can calculate
 * if it is good to add a element to this row or not. This will be decided with
 * the worst ratio. If this shrinks the element should be added.
 * </p>
 * <p>
 * It can also layout the row. Meaning it stores the positions in the
 * {@link RectangleElement} from this row. And returns the new bounding, which
 * is the old bounding without the row.
 * </p>
 */
public class MapRow {

    /** The fields for store the axis of this row. */
    public static final int X_AXIS = 0;

    public static final int Y_AXIS = 1;

    /** All elements who are stored in this row. */
    private List<RectangleElement> members;

    /** The sum of all member lengths. */
    private long totalLength = 0;

    /** The rectangle in which this element is positioned. */
    private Rectangle2D.Float bounds;

    /** The smaller side of the bounding. Can be height or width. */
    private float fixedSide;

    /** The axis of this row. Determined by the sides of bounding */
    private int axis;

    /**
	 * A factor with which the length of a member must be multiplied to get the
	 * corrected area
	 */
    private float areaFactor;

    /**
	 * Creates a new {@link MapRow} with the given bounding and areaFactor.
	 * 
	 * @param bounding
	 *            the bounding in which the row should be set.<br>
	 *            Must not be null.
	 * @param areaFactor
	 *            the area factor.<br>
	 *            Must be greater then 0.
	 */
    public MapRow(Rectangle2D.Float bounding, float areaFactor) {
        assert bounding != null : "bounding is null";
        assert areaFactor > 0 : "areaFactor must be greater then 0: " + areaFactor;
        this.bounds = bounding;
        this.areaFactor = areaFactor;
        if (bounding.height <= bounding.width) axis = Y_AXIS; else axis = X_AXIS;
        fixedSide = Math.min(bounding.height, bounding.width);
        members = new ArrayList<RectangleElement>();
    }

    /**
	 * Adds a element to this row.
	 * 
	 * @param element
	 *            the element to be added
	 */
    public void addElement(DrawElement element) {
        assert element != null : "element is null";
        members.add((RectangleElement) element);
        totalLength += element.getElement().getLength();
    }

    /**
	 * @return the areaFactor
	 */
    public float getAreaFactor() {
        return areaFactor;
    }

    /**
	 * @return the axis
	 */
    public int getAxis() {
        return axis;
    }

    /**
	 * @return the members of this row
	 */
    public Set<RectangleElement> getMembers() {
        return new HashSet<RectangleElement>(members);
    }

    /**
	 * Returns the worst ratio of all member and if given the new Element. The
	 * single ratios are calculated in <i>getRatio(long, long)</i>
	 * 
	 * @param newElement
	 *            the element which is checked to be added.
	 * @return the worst ratio
	 */
    private float getWorstRatio(DrawElement newElement) {
        float worstRatio = 0;
        long totalLength = this.totalLength;
        if (newElement != null) {
            totalLength += newElement.getElement().getLength();
            worstRatio = Math.max(worstRatio, getRatio(newElement.getElement().getLength(), totalLength));
        }
        for (RectangleElement member : members) worstRatio = Math.max(worstRatio, getRatio(member.getLength(), totalLength));
        return worstRatio;
    }

    /**
	 * Calculate the ratio of a given length by the given total length of the
	 * row.
	 */
    private float getRatio(long length, long totalLength) {
        assert length > 0 : "length must be greater than 0: " + length;
        float oneRatio = totalLength * totalLength * areaFactor / (fixedSide * fixedSide * length);
        return Math.max(oneRatio, 1 / oneRatio);
    }

    /**
	 * This method set the positions for all members. And returns the new
	 * bounding for the rest elements.
	 * 
	 * @return The new bounding for the rest of elements.
	 */
    public Rectangle2D.Float layout() {
        if (axis == Y_AXIS) return layoutY(); else return layoutX();
    }

    /**
	 * Layout the row if the axis is a x axis
	 * 
	 * @return The new bounding for the rest of elements.
	 */
    private Rectangle2D.Float layoutX() {
        float leftWidth = bounds.width;
        float rowHeight = (float) totalLength * areaFactor / bounds.width;
        for (RectangleElement member : members) {
            float x = bounds.x + bounds.width - leftWidth;
            float y = bounds.y + bounds.height - rowHeight;
            float width = (float) member.getLength() * bounds.width / (float) totalLength;
            float height = rowHeight;
            member.setPosition(new Rectangle2D.Float(x, y, width, height));
            leftWidth -= width;
        }
        return new Rectangle2D.Float(bounds.x, bounds.y, bounds.width, bounds.height - rowHeight);
    }

    /**
	 * Layout the row if axis is an y axis.
	 * 
	 * @return The new bounding for the rest of elements.
	 */
    private Rectangle2D.Float layoutY() {
        float leftHeigth = bounds.height;
        float rowWidth = (float) totalLength * areaFactor / bounds.height;
        for (RectangleElement member : members) {
            float width = rowWidth;
            float height = (float) member.getLength() * bounds.height / (float) totalLength;
            float x = bounds.x;
            float y = bounds.y + leftHeigth - height;
            member.setPosition(new Rectangle2D.Float(x, y, width, height));
            leftHeigth -= height;
        }
        return new Rectangle2D.Float(bounds.x + rowWidth, bounds.y, bounds.width - rowWidth, bounds.height);
    }

    /**
	 * Returns whether a given element should be added to the row or not. This
	 * depends on the worst ratio, if this gets better with the new element it
	 * should be added if it get worse then not.
	 * 
	 * @return true if it should be added. False else.
	 */
    public boolean shouldAddElement(DrawElement element) {
        assert element != null : "element is null";
        if (totalLength == 0) return true;
        float rWithout = getWorstRatio(null);
        float rWith = getWorstRatio(element);
        if (rWith <= rWithout) return true; else return false;
    }
}
