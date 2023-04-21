package org.kumenya.util;

import java.util.AbstractMap;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class ImmutableListMap extends AbstractMap {

    final Set listSet;

    public ImmutableListMap(List list) {
        this.listSet = new ImmutableListSet(list);
    }

    public Set entrySet() {
        return listSet;
    }
}
