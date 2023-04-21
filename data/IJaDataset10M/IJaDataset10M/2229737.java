package org.activebpel.rt.bpel.def.activity.support;

import org.activebpel.rt.bpel.def.AeNamedDef;
import org.activebpel.rt.bpel.def.visitors.IAeDefVisitor;

/**
 * Definition for link objects which are in support of the flow activity definition.
 */
public class AeLinkDef extends AeNamedDef {

    /**
    * Default constructor
    */
    public AeLinkDef() {
        super();
    }

    /**
    * @see org.activebpel.rt.bpel.def.AeBaseDef#accept(org.activebpel.rt.bpel.def.visitors.IAeDefVisitor)
    */
    public void accept(IAeDefVisitor aVisitor) {
        aVisitor.visit(this);
    }
}
