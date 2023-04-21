package org.activebpel.rt.bpel.def.activity.support;

import org.activebpel.rt.bpel.def.AeBaseDef;
import org.activebpel.rt.bpel.def.visitors.IAeDefVisitor;

/**
 * Models the extensibleAssign construct introduced in WS-BPEL 2.0.
 */
public class AeExtensibleAssignDef extends AeBaseDef {

    /**
    * Default c'tor.
    */
    public AeExtensibleAssignDef() {
        super();
    }

    /**
    * @see org.activebpel.rt.bpel.def.AeBaseDef#accept(org.activebpel.rt.bpel.def.visitors.IAeDefVisitor)
    */
    public void accept(IAeDefVisitor aVisitor) {
        aVisitor.visit(this);
    }
}
