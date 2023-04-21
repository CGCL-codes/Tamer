package de.grogra.graph.impl;

import de.grogra.graph.Cache;
import de.grogra.graph.ContextDependent;
import de.grogra.graph.GraphState;
import de.grogra.persistence.ShareableBase;

public abstract class ContextDependentBase extends ShareableBase implements ContextDependent {

    public void writeStamp(Cache.Entry cache, GraphState gs) {
        cache.write(System.identityHashCode(this));
        cache.write(getStamp());
    }
}
