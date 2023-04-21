package org.apache.fop.fo.flow;

import org.apache.fop.fo.*;
import org.apache.fop.layout.*;
import org.apache.fop.apps.FOPException;

/**
 */
public class TableAndCaption extends ToBeImplementedElement {

    public static class Maker extends FObj.Maker {

        public FObj make(FObj parent, PropertyList propertyList, String systemId, int line, int column) throws FOPException {
            return new TableAndCaption(parent, propertyList, systemId, line, column);
        }
    }

    public static FObj.Maker maker() {
        return new TableAndCaption.Maker();
    }

    protected TableAndCaption(FObj parent, PropertyList propertyList, String systemId, int line, int column) throws FOPException {
        super(parent, propertyList, systemId, line, column);
    }

    public String getName() {
        return "fo:table-and-caption";
    }

    public int layout(Area area) throws FOPException {
        AccessibilityProps mAccProps = propMgr.getAccessibilityProps();
        AuralProps mAurProps = propMgr.getAuralProps();
        BorderAndPadding bap = propMgr.getBorderAndPadding();
        BackgroundProps bProps = propMgr.getBackgroundProps();
        MarginProps mProps = propMgr.getMarginProps();
        RelativePositionProps mRelProps = propMgr.getRelativePositionProps();
        return super.layout(area);
    }
}
