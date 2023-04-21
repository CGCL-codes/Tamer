package de.ulrich_fuchs.jtypeset.stream;

import java.util.LinkedList;
import java.util.List;
import de.ulrich_fuchs.jtypeset.Page;
import de.ulrich_fuchs.jtypeset.PdfPage;
import de.ulrich_fuchs.jtypeset.Splitpoint;

/**
 *  Implements a float, that is anchored within the paragraph
 *
 * @author  ulrich
 */
public abstract class FloatCluster extends ParagraphComponent {

    /** Name for the cluster, used for debugging */
    protected String name;

    public EAlign alignment;

    protected ESize maxSize;

    protected ESize minSize;

    public double fixedWidth;

    protected ESize[] discreeteSizes;

    protected boolean widthShrinkableWithoutHeightChange;

    public enum EAlign {

        LEFT, RIGHT, LINE, INSIDE, EDGE
    }

    public enum ESize {

        PAGE, HALFPAGE, QUARTER, LARGE, MEDIUM, SMALL, MINI, FIXEDWIDTH;

        public static ESize[] sizeTestSequence = new ESize[] { PAGE, HALFPAGE, QUARTER, LARGE, MEDIUM, SMALL, MINI, FIXEDWIDTH };

        public static int compareSize(ESize e1, ESize e2) {
            return e2.compareTo(e1);
        }
    }

    public FloatCluster(ElementStream substream) {
        super();
        setSubstream(substream);
        setMaxSize(ESize.MINI);
        setMinSize(ESize.MINI);
        alignment = EAlign.RIGHT;
        widthShrinkableWithoutHeightChange = false;
    }

    /** Indicates if the float is to be placed at the left or at the right side
     *  of the current paragraph or below the currentParagraph*/
    public final EAlign getAlignment() {
        return alignment;
    }

    /** Indicates the max size of the flow object */
    public final ESize getMaxSize() {
        return maxSize;
    }

    /** Indicates the min size of the flow object */
    public final ESize getMinSize() {
        return minSize;
    }

    /** Indicates if the Flow can be split among multiple pages */
    public boolean canBeSplit() {
        return false;
    }

    /** Indicates if the float can be split vertically. The method getSplitpoints(..)
     *  must return a number of valid splidpoints. the content between the splitpoints
     *  should be small enough to fit on a page - if the content between two splitpoints
     *  is too large to fit on a page, the rendering will fail.
     **/
    public boolean isSplittable() {
        return false;
    }

    /** Indicates if the flow should only be splitted if there no other possibility (like when
     *  its longer than a single page)
     */
    public final boolean shouldNotBeSplitted() {
        return true;
    }

    /** Indicates the width in pt (which points?), the
     *  behaviour for Flow Objects which do not return FIXEDWIDTH as
     *  min and max size is undefined
     */
    public double getWidth() {
        return fixedWidth;
    }

    /** Calculates the length of the float, if it is scaled at the given width
     */
    public abstract double getHeight(double width, Page metricsPage);

    /** Calculates the length of the between the given splitpoints, if it is scaled at the given width
     *  Always returns zero in this class, since isSplittable() always returns false;
     */
    public double getHeight(double width, Splitpoint fromSp, Splitpoint toSp, Page metricsPage) {
        return 0;
    }

    /**
	 * @return Returns the widthShrinkableWithoutHeightChange.
	 */
    public final boolean widthShrinkableWithoutHeightChange() {
        return widthShrinkableWithoutHeightChange;
    }

    /** Must be overridden if widthShrinkableWithoutHeightChange() returns true */
    public double getUsedWidth(double width, Page metricsPage) {
        return 20;
    }

    /**
	 * @param widthShrinkableWithoutHeightChange The widthShrinkableWithoutHeightChange to set.
	 */
    public final void setWidthShrinkableWithoutHeightChange(boolean widthShrinkableWithoutHeightChange) {
        this.widthShrinkableWithoutHeightChange = widthShrinkableWithoutHeightChange;
    }

    public final void setMaxSize(ESize maxSize) {
        this.maxSize = maxSize;
    }

    public final void setMinSize(ESize minSize) {
        this.minSize = minSize;
    }

    public final String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final ESize[] getDiscreeteSizes() {
        return discreeteSizes;
    }

    public final void setDiscreeteSizes(ESize[] discreeteSizes) {
        this.discreeteSizes = discreeteSizes;
    }

    /** Returns all splitpoints after the given splitpoint (all splitpoints, if fromSp is null),
     *  Returns an empty list, if no splitpoints exist
     * @param fromSp
     * @return
     */
    public List<Splitpoint> getSplitpoints(Splitpoint fromSp) {
        return new LinkedList<Splitpoint>();
    }
}
