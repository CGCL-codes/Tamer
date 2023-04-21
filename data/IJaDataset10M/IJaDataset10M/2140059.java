package org.databene.commons.mutator;

import org.databene.commons.Mutator;

/**
 * Wraps a Mutator.<br/>
 * <br/>
 * Created: 25.01.2008 13:33:37
 * @author Volker Bergmann
 * @since 0.3.0
 */
public abstract class MutatorWrapper implements Mutator {

    protected Mutator realMutator;

    public MutatorWrapper(Mutator realMutator) {
        this.realMutator = realMutator;
    }
}
