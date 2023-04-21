package org.dbe.composer.wfengine.bpel.def;

import org.dbe.composer.wfengine.bpel.def.visitors.ISdlDefVisitor;

/**
 * Definition for bpel default fault handler activity.
 */
public class SdlDefaultFaultHandlerDef extends SdlBaseDef implements ISdlSingleActivityContainerDef {

    /** Activity to execute if this fault handler is called */
    private SdlActivityDef mActivity;

    /**
    * Default constructor
    */
    public SdlDefaultFaultHandlerDef() {
    }

    /**
    * Accessor method to obtain the activity to be executed upon for
    * the fault condition.
    *
    * @return the activity associated with the fault condition
    */
    public final SdlActivityDef getActivity() {
        return mActivity;
    }

    /**
    * Mutator method to set the activity which will be executed for
    * the fault condition.
    *
    * @param aActivity the activity to be executed
    */
    public final void setActivity(SdlActivityDef aActivity) {
        mActivity = aActivity;
    }

    public void accept(ISdlDefVisitor aVisitor) {
        aVisitor.visit(this);
    }
}
