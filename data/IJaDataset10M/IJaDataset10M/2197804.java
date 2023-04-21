package org.apache.fop.fo.flow;

import org.apache.fop.fo.*;
import org.apache.fop.layout.*;
import org.apache.fop.apps.FOPException;

public class ListItemBody extends FObj {

    public static class Maker extends FObj.Maker {

        public FObj make(FObj parent, PropertyList propertyList, String systemId, int line, int column) throws FOPException {
            return new ListItemBody(parent, propertyList, systemId, line, column);
        }
    }

    public static FObj.Maker maker() {
        return new ListItemBody.Maker();
    }

    public ListItemBody(FObj parent, PropertyList propertyList, String systemId, int line, int column) {
        super(parent, propertyList, systemId, line, column);
    }

    public String getName() {
        return "fo:list-item-body";
    }

    public int layout(Area area) throws FOPException {
        if (this.marker == START) {
            AccessibilityProps mAccProps = propMgr.getAccessibilityProps();
            this.marker = 0;
            String id = this.properties.get("id").getString();
            try {
                area.getIDReferences().initializeID(id, area);
            } catch (FOPException e) {
                if (!e.isLocationSet()) {
                    e.setLocation(systemId, line, column);
                }
                throw e;
            }
        }
        int numChildren = this.children.size();
        for (int i = this.marker; i < numChildren; i++) {
            FObj fo = (FObj) children.get(i);
            int status;
            if (Status.isIncomplete((status = fo.layout(area)))) {
                this.marker = i;
                if ((i == 0) && (status == Status.AREA_FULL_NONE)) {
                    return Status.AREA_FULL_NONE;
                } else {
                    return Status.AREA_FULL_SOME;
                }
            }
        }
        return Status.OK;
    }
}
