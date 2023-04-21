package org.ncgr.cmtv.isys;

import org.ncgr.isys.system.IsysObject;
import org.ncgr.isys.system.IsysAttribute;
import org.ncgr.isys.system.MutableIsysObject;

/**
 * A little ruse used by the viewer to allow the IsysObject representations 
 * of data items to be augmented with context-sensitive attributes, e.g.
 * the maximum value of the attribute in the most recently selected range.
 */
class AugmentedIsysObject extends MutableIsysObject {

    private IsysObject augmentee;

    AugmentedIsysObject(IsysObject augmentee, IsysAttribute[] augmentingAttrs) {
        super(augmentee.getAttributes(), augmentee.getEquivalenceAttributes());
        this.augmentee = augmentee;
        for (int i = 0; i < augmentingAttrs.length; i++) {
            addAttribute(augmentingAttrs[i]);
        }
    }

    public boolean equals(Object o) {
        if (o == augmentee) {
            return true;
        }
        return super.equals(o);
    }

    public int hashCode() {
        return augmentee.hashCode();
    }
}
