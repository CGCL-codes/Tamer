package com.google.typography.font.tools.subsetter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Stuart Gill
 */
public abstract class TableSubsetterImpl implements TableSubsetter {

    protected final Set<Integer> tags;

    protected TableSubsetterImpl(Integer... tags) {
        Set<Integer> temp = new HashSet<Integer>(tags.length);
        for (Integer tag : tags) {
            temp.add(tag);
        }
        this.tags = Collections.unmodifiableSet(temp);
    }

    @Override
    public boolean tagHandled(int tag) {
        return this.tags.contains(tag);
    }

    @Override
    public Set<Integer> tagsHandled() {
        return this.tags;
    }
}
