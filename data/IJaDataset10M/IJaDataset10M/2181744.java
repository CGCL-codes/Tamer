package org.jcvi.tasker;

import org.jcvi.glk.task.TaskFeatureType;

/**
 * A <code>BasicFeatureTypeLookup</code> is a simple implementation of a
 * {@link TaskFeatureTypeLookup} which generates local, volatile instances for any
 * {@link TaskFeatureType}s which must be resolved internally.  No support is given for any
 * persistence systems.  This makes this lookup suitable mostly for standalone use, without
 * any backing database.
 *
 * @author jsitz@jcvi.org
 */
public class BasicFeatureTypeLookup extends AbstractFeatureTypeLookup {

    /**
     * Constructs a new <code>BasicFeatureTypeLookup</code>.
     */
    public BasicFeatureTypeLookup() {
        super();
    }

    @Override
    protected TaskFeatureType buildTypeInstance(String name) {
        return new TaskFeatureType(name);
    }
}
