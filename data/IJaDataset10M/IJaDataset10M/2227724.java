package org.activebpel.rt.bpel.def.activity;

import org.activebpel.rt.bpel.def.AeActivityDef;
import org.activebpel.rt.bpel.def.visitors.IAeDefVisitor;

/**
 * Definition for bpel exit activity.
 */
public class AeActivityExitDef extends AeActivityDef {

    /**
    * Default constructor
    */
    public AeActivityExitDef() {
        super();
    }

    /**
    * @see org.activebpel.rt.bpel.def.AeActivityDef#accept(org.activebpel.rt.bpel.def.visitors.IAeDefVisitor)
    */
    public void accept(IAeDefVisitor aVisitor) {
        aVisitor.visit(this);
    }
}
