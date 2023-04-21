package org.mmtk.plan.generational.immix;

import org.mmtk.plan.generational.GenConstraints;
import static org.mmtk.policy.immix.ImmixConstants.MAX_IMMIX_OBJECT_BYTES;
import org.vmmagic.pragma.*;

/**
 * This class and its subclasses communicate to the host VM/Runtime
 * any features of the selected plan that it needs to know.  This is
 * separate from the main Plan/PlanLocal class in order to bypass any
 * issues with ordering of static initialization.
 *
 */
@Uninterruptible
public class GenImmixConstraints extends GenConstraints {

    /** @return The specialized scan methods required */
    @Override
    public int numSpecializedScans() {
        return 2;
    }

    @Override
    public int maxNonLOSCopyBytes() {
        return MAX_IMMIX_OBJECT_BYTES;
    }
}
