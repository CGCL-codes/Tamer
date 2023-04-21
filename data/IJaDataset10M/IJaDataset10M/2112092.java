package net.sourceforge.olduvai.lrac.drawer;

import net.sourceforge.olduvai.accordiondrawer.AbstractRangeList;
import net.sourceforge.olduvai.accordiondrawer.DrawableRange;
import net.sourceforge.olduvai.accordiondrawer.SplitAxis;
import net.sourceforge.olduvai.lrac.genericdataservice.structure.SourceInterface;

/**
 * Represents a range of columns inside of a row.  
 * 
 * @author Peter McLachlan (spark343@cs.ubc.ca)
 */
public class RangeInRow extends DrawableRange {

    /**
	 * Source for which the range is valid
	 */
    SourceInterface source;

    /**
	 * Creates a new range of indices with a specified source. 
	 * @param s Source within which the range lies
	 * @param minimum 
	 * @param maximum
	 * @param group Group this range will be associated with
	 */
    public RangeInRow(SourceInterface s, int minimum, int maximum, AbstractRangeList group) {
        this.source = s;
        min = minimum;
        max = maximum;
        this.group = group;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
	 * Retrieves pointer to the Source object associated with this range
	 * @return
	 */
    public SourceInterface getSource() {
        return source;
    }

    /**
	 * Debugging tostring method
	 */
    public String toString() {
        return "(" + source + ":" + min + "->" + max + ")";
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof RangeInRow)) return false;
        RangeInRow cast = (RangeInRow) o;
        if (cast.source != source) return false;
        if (cast.min != min) return false;
        if (cast.max != max) return false;
        return true;
    }

    /**
	 * Return the size of this range in world space coordinates 
	 * 
	 * @param splitAxis
	 * @param frameNum
	 * @return
	 */
    public double getSize(SplitAxis splitAxis, int frameNum) {
        final int minCellSplit = min;
        final int maxCellSplit = max + 1;
        return splitAxis.getAbsoluteValue(maxCellSplit, frameNum) - splitAxis.getAbsoluteValue(minCellSplit, frameNum);
    }

    /**
	 * Swaps two sets of indices in the range.   
	 * 
	 * @param min1
	 * @param max1
	 * @param min2
	 * @param max2
	 */
    public void swapIndices(int min1, int max1, int min2, int max2) {
        if (min == min1 && max == max1) {
            min = min2;
            max = max2;
        } else if (min == min2 && max == max2) {
            min = min1;
            max = max1;
        }
    }

    public int compareTo(Object o) {
        if (!(o instanceof RangeInRow)) return -1;
        RangeInRow cr = (RangeInRow) o;
        if (source != cr.source) {
            AccordionLRACDrawer lrd = source.getLRD();
            SplitAxis sourceAxis = lrd.getSourceAxis();
            final int thisIndex = sourceAxis.getSplitIndex(source.getMinLine());
            final int otherIndex = sourceAxis.getSplitIndex(cr.source.getMinLine());
            if (thisIndex < otherIndex) return -1; else if (thisIndex > otherIndex) return 1;
        }
        if (min < cr.min) return -1; else if (min > cr.min) return 1;
        return 0;
    }
}

;
