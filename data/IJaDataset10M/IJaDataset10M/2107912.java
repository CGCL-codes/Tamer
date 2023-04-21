package org.dbe.composer.wfengine.bpel.def.activity.support;

import org.dbe.composer.wfengine.bpel.def.SdlBaseDef;
import org.dbe.composer.wfengine.bpel.def.visitors.ISdlDefVisitor;

/**
 * Holds individual copy operations of an assign activity
 */
public class SdlAssignCopyDef extends SdlBaseDef {

    private SdlAssignFromDef mFrom;

    private SdlAssignToDef mTo;

    /**
    * Default constructor
    */
    public SdlAssignCopyDef() {
    }

    /**
    * Accessor method to obtain the From assignment part of the Copy activity.
    *
    * @return the From assignment object
    */
    public final SdlAssignFromDef getFrom() {
        return mFrom;
    }

    /**
    * Mutator method to set the From assignment part of the Copy activity.
    *
    * @param aFrom the From part of the Copy activity
    */
    public final void setFrom(SdlAssignFromDef aFrom) {
        mFrom = aFrom;
    }

    /**
    * Accessor method to obtain the To assignment part of the Copy activity.
    *
    * @return the To assignment object
    */
    public final SdlAssignToDef getTo() {
        return mTo;
    }

    /**
    * Mutator method to set the To assignment part of the Copy activity.
    *
    * @param aFrom the To part of the Copy activity
    */
    public final void setTo(SdlAssignToDef aTo) {
        mTo = aTo;
    }

    public void accept(ISdlDefVisitor aVisitor) {
        aVisitor.visit(this);
    }
}
