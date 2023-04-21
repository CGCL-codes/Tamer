package org.dbe.composer.wfengine.bpel.def.activity.support;

import org.dbe.composer.wfengine.bpel.def.visitors.ISdlDefVisitor;

/**
 * Models the <code>to</code> element in a copy operation. Broke this out as its
 * own class since we want to visit it and to avoid any confusion with the <code>from</code>
 * portion of the copy.
 */
public class SdlAssignToDef extends SdlAssignVarDef {

    /**
     * Default constructor
     */
    public SdlAssignToDef() {
    }

    public void accept(ISdlDefVisitor aVisitor) {
        aVisitor.visit(this);
    }
}
