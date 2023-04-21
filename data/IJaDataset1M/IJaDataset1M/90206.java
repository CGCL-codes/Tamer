package org.apache.fop.fo.flow;

import org.apache.fop.fo.*;
import org.apache.fop.fo.pagination.*;
import org.apache.fop.layout.Area;
import org.apache.fop.layout.BodyAreaContainer;
import org.apache.fop.apps.FOPException;
import java.util.ArrayList;

public abstract class AbstractFlow extends FObj {

    /**
     * PageSequence container
     */
    protected PageSequence pageSequence;

    /**
     * Area in which we lay out our kids
     */
    private Area area;

    /**
     * ArrayList to store snapshot
     */
    private ArrayList markerSnapshot;

    /**
     * flow-name attribute
     */
    protected String _flowName;

    /**
     * Content-width of current column area during layout
     */
    private int contentWidth;

    private int _status = Status.AREA_FULL_NONE;

    protected AbstractFlow(FObj parent, PropertyList propertyList, String systemId, int line, int column) throws FOPException {
        super(parent, propertyList, systemId, line, column);
        if (parent.getName().equals("fo:page-sequence")) {
            this.pageSequence = (PageSequence) parent;
        } else {
            throw new FOPException("flow must be child of page-sequence, not " + parent.getName(), systemId, line, column);
        }
    }

    public String getFlowName() {
        return _flowName;
    }

    public int layout(Area area) throws FOPException {
        return layout(area, null);
    }

    public int layout(Area area, Region region) throws FOPException {
        if (this.marker == START) {
            this.marker = 0;
        }
        BodyAreaContainer bac = (BodyAreaContainer) area;
        boolean prevChildMustKeepWithNext = false;
        ArrayList pageMarker = this.getMarkerSnapshot(new ArrayList());
        int numChildren = this.children.size();
        if (numChildren == 0) {
            throw new FOPException("fo:flow must contain block-level children", systemId, line, column);
        }
        for (int i = this.marker; i < numChildren; i++) {
            FObj fo = (FObj) children.get(i);
            if (bac.isBalancingRequired(fo)) {
                bac.resetSpanArea();
                this.rollback(markerSnapshot);
                i = this.marker - 1;
                continue;
            }
            Area currentArea = bac.getNextArea(fo);
            currentArea.setIDReferences(bac.getIDReferences());
            if (bac.isNewSpanArea()) {
                this.marker = i;
                markerSnapshot = this.getMarkerSnapshot(new ArrayList());
            }
            setContentWidth(currentArea.getContentWidth());
            _status = fo.layout(currentArea);
            if (Status.isIncomplete(_status)) {
                if ((prevChildMustKeepWithNext) && (Status.laidOutNone(_status))) {
                    this.marker = i - 1;
                    FObj prevChild = (FObj) children.get(this.marker);
                    prevChild.removeAreas();
                    prevChild.resetMarker();
                    prevChild.removeID(area.getIDReferences());
                    _status = Status.AREA_FULL_SOME;
                    return _status;
                }
                if (bac.isLastColumn()) if (_status == Status.FORCE_COLUMN_BREAK) {
                    this.marker = i;
                    _status = Status.FORCE_PAGE_BREAK;
                    return _status;
                } else {
                    this.marker = i;
                    return _status;
                } else {
                    if (Status.isPageBreak(_status)) {
                        this.marker = i;
                        return _status;
                    }
                    ((org.apache.fop.layout.ColumnArea) currentArea).incrementSpanIndex();
                    i--;
                }
            }
            if (_status == Status.KEEP_WITH_NEXT) {
                prevChildMustKeepWithNext = true;
            } else {
                prevChildMustKeepWithNext = false;
            }
        }
        return _status;
    }

    protected void setContentWidth(int contentWidth) {
        this.contentWidth = contentWidth;
    }

    /**
     * Return the content width of this flow (really of the region
     * in which it is flowing).
     */
    public int getContentWidth() {
        return this.contentWidth;
    }

    public int getStatus() {
        return _status;
    }

    public boolean generatesReferenceAreas() {
        return true;
    }
}
