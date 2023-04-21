package jpatch.control.edit;

import jpatch.entity.*;

/**
 * @author sascha
 *
 */
public class CompoundAttachControlPoints extends JPatchCompoundEdit {

    public CompoundAttachControlPoints(OLDControlPoint cpA, OLDControlPoint cpB) {
        addEdit(new CompoundReplaceControlPointInEntities(cpA, cpB));
        addEdit(new AtomicAttachControlPoints(cpA, cpB));
    }
}
