package com.bluebrim.layout.impl.server.manager;

import org.w3c.dom.Node;
import com.bluebrim.base.shared.debug.CoAssertion;
import com.bluebrim.layout.impl.shared.*;
import com.bluebrim.layout.impl.shared.CoAbsoluteWidthSpecIF;
import com.bluebrim.layout.impl.shared.CoPageItemStringResources;
import com.bluebrim.layout.shared.*;
import com.bluebrim.layout.shared.CoImmutableColumnGridIF;

/**
 * En CoWidthSpec specificerar ett sidelements bredd.
 */
public class CoAbsoluteWidthSpec extends CoAbsoluteSizeSpec implements CoAbsoluteWidthSpecIF {

    public static final String XML_TAG = "absolute-width-spec";

    public static com.bluebrim.xml.shared.CoXmlImportEnabledIF xmlCreateModel(Object superModel, Node node, com.bluebrim.xml.shared.CoXmlContext context) {
        return new CoAbsoluteWidthSpec(node, context);
    }

    public CoAbsoluteWidthSpec() {
        super();
    }

    public CoAbsoluteWidthSpec(double distance) {
        super(distance);
    }

    protected CoAbsoluteWidthSpec(Node node, com.bluebrim.xml.shared.CoXmlContext context) {
        super(node, context);
    }

    private double calcWidth(CoLayoutableIF layoutable) {
        if ((m_distance == 0) || !hasValidParent(layoutable, true)) {
            return layoutable.getInteriorWidth();
        } else {
            int numberOfColumns = (int) m_distance;
            CoImmutableColumnGridIF columnGrid = layoutable.getLayoutParent().getColumnGrid();
            return columnGrid.getWidthFor(numberOfColumns);
        }
    }

    public void configure(CoShapePageItemIF pi) {
        CoImmutableColumnGridIF g = pi.getColumnGrid();
        int i = (int) Math.round(pi.getCoShape().getWidth() / (g.getColumnWidth() + g.getColumnSpacing()));
        if (i == 0) i = 1;
        if (i > g.getColumnCount()) i = g.getColumnCount();
        m_distance = i;
    }

    public String getDescription() {
        return CoPageItemStringResources.getName(ABSOLUTE_WIDTH_SPEC);
    }

    public String getFactoryKey() {
        return ABSOLUTE_WIDTH_SPEC;
    }

    public String getXmlTag() {
        return XML_TAG;
    }

    public boolean isContentDependent() {
        return true;
    }

    public void setSizeBeforeLocation(CoLayoutableIF layoutable) {
        if (CoAssertion.PRE) {
            CoAssertion.preCondition((m_distance >= 0), "distance < 0 in CoAbsoluteWidthSpec.setSizeBeforeLocation");
        }
        setLayoutableWidth(layoutable, calcWidth(layoutable));
    }
}
