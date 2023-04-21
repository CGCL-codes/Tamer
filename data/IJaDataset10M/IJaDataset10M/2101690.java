package de.ulrich_fuchs.jtypeset;

import de.ulrich_fuchs.jtypeset.stream.BorderData;

/** A Flow context with a single column and an infinite height,
 *  Content can be rendered to this context to determine which
 *  height it will need for a given width. (using getCurrentPosition().y)
 * @author ulrich
 *
 */
public class MeasureFlowContext extends MultiColumnFlowContext {

    double width;

    /** Creates a new MeasureFlowContext with border, margin and padding set to zero */
    public MeasureFlowContext(LayoutingContext lc, double width) {
        super(lc, 0, 0, width, Double.POSITIVE_INFINITY, 1);
        setDebugName("Measure context");
        initColumnsNewPage(0, 0, width, Double.POSITIVE_INFINITY, new BorderData(), null);
    }
}
