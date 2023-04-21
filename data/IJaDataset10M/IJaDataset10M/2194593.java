package org.nakedobjects.noa.reflect.checks;

import org.nakedobjects.noa.reflect.NakedObjectActionInstance;

/**
 * Adapter class for a check against a {@link NakedObjectActionInstance}.
 * 
 */
public abstract class AbstractCheckNakedObjectActionInstance extends AbstractCheck {

    public AbstractCheckNakedObjectActionInstance(final NakedObjectActionInstance nakedObjectActionInstance) {
        this.nakedObjectActionInstance = nakedObjectActionInstance;
    }

    private final NakedObjectActionInstance nakedObjectActionInstance;

    public NakedObjectActionInstance getNakedObjectActionInstance() {
        return nakedObjectActionInstance;
    }
}
