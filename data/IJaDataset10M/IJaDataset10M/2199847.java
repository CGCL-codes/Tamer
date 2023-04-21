package org.activebpel.rt.bpel.def.activity.support;

import org.activebpel.rt.bpel.def.visitors.IAeDefVisitor;

/**
 * A wrapper class for the condition and activity child defs of the if activity.  This wrapper def
 * is used to make the modelling of bpel 1.1 and 2.0 processes a bit more unified.
 */
public class AeIfDef extends AeElseIfDef {

    /**
    * Default c'tor.
    */
    public AeIfDef() {
        super();
    }

    /**
    * @see org.activebpel.rt.bpel.def.activity.support.AeElseDef#accept(org.activebpel.rt.bpel.def.visitors.IAeDefVisitor)
    */
    public void accept(IAeDefVisitor aVisitor) {
        aVisitor.visit(this);
    }
}
