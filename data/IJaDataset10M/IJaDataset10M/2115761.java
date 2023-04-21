package net.derquinse.common.collect;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Map;
import com.google.common.collect.ForwardingMap;
import com.google.common.collect.ImmutableMap;

/**
 * An immutable and thread-safe implementation of HierarchyMap.
 * @author Andres Rodriguez
 * @param <K> Type of the keys.
 * @param <V> Type of the values.
 */
public abstract class ImmutableHierarchyMap<K, V> extends ForwardingMap<K, V> implements HierarchyMap<K, V> {

    /**
	 * Hierachy map preconditions.
	 * @param map Backing map.
	 * @param hierarchy Backing hierachy.
	 */
    static void check(Map<?, ?> map, Hierarchy<?> hierarchy) {
        checkNotNull(map, "The backing map must be provided.");
        checkNotNull(hierarchy, "The backing hierarchy must be provided.");
        checkArgument(map.keySet().equals(hierarchy.elementSet()), "Inconsistent map and hierarchy");
    }

    /**
	 * Returns an immutable hierarchy map backed by a map and a hierarchy.
	 * @param map Backing map.
	 * @param hierarchy Backing hierachy.
	 */
    public static <K, V> ImmutableHierarchyMap<K, V> of(Map<? extends K, ? extends V> map, Hierarchy<? extends K> hierarchy) {
        check(map, hierarchy);
        if (map.isEmpty()) {
            return of();
        }
        return new RegularImmutableHierarchyMap<K, V>(ImmutableMap.copyOf(map), ImmutableHierarchy.copyOf(hierarchy));
    }

    /**
	 * Returns an empty immutable hierarchy map.
	 */
    @SuppressWarnings("unchecked")
    public static <K, V> ImmutableHierarchyMap<K, V> of() {
        return (ImmutableHierarchyMap<K, V>) EmptyImmutableHierarchyMap.INSTANCE;
    }

    /** Default constructor. */
    ImmutableHierarchyMap() {
    }

    public V apply(K key) {
        final V value = get(checkNotNull(key));
        checkArgument(value != null);
        return value;
    }
}
