package net.derquinse.common.collect;

import java.util.Map;
import com.google.common.collect.ImmutableMap;

/**
 * An empty immutable hierarchy map.
 * @author Andres Rodriguez
 * @param <K> Type of the keys.
 * @param <V> Type of the values.
 */
final class EmptyImmutableHierarchyMap extends ImmutableHierarchyMap<Object, Object> {

    static final EmptyImmutableHierarchyMap INSTANCE = new EmptyImmutableHierarchyMap();

    private EmptyImmutableHierarchyMap() {
    }

    @Override
    protected Map<Object, Object> delegate() {
        return ImmutableMap.of();
    }

    public Hierarchy<Object> keyHierarchy() {
        return ImmutableHierarchy.of();
    }
}
