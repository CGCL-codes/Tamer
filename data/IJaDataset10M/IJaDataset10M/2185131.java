package osmosis;

import java.util.Date;
import gnu.trove.TLongObjectHashMap;
import gnu.trove.TLongObjectIterator;
import org.openstreetmap.osmosis.core.domain.v0_6.Entity;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;

/**
 * Miscellaneous utility functions for entities and collections of them.
 * 
 * @author Sebastian Kuerten (sebastian.kuerten@fu-berlin.de)
 * 
 */
public class EntityUtil {

    /**
	 * Return only those entities that have the given tag.
	 * 
	 * @param <V>
	 *            implicit type argument (inferred by argument).
	 * @param original
	 *            the set of elements to filter.
	 * @param key
	 *            the key of the tag.
	 * @param val
	 *            the value of the tag.
	 * @return the entities that have the tag (key, value).
	 */
    public static <V extends Entity> TLongObjectHashMap<V> filter(TLongObjectHashMap<V> original, String key, String val) {
        TLongObjectHashMap<V> selected = new TLongObjectHashMap<V>();
        TLongObjectIterator<V> iterator = original.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            V entity = iterator.value();
            if (entityHasTag(entity, key, val)) {
                selected.put(iterator.key(), entity);
            }
        }
        return selected;
    }

    private static boolean entityHasTag(Entity entity, String key, String val) {
        for (Tag tag : entity.getTags()) {
            if (!tag.getKey().equals(key)) continue;
            if (!tag.getValue().equals(val)) continue;
            return true;
        }
        return false;
    }

    /**
	 * Put in <code>dest</code> all the values that are in <code>src</code>, possibly
	 * overwriting values that have already been mapped to be an index.
	 * 
	 * @param <V>
	 *            implicit type argument (inferred by argument).
	 * @param dest
	 *            the set of entities to merge into.
	 * @param src
	 *            the set of entities to merge into <code>dest</code>.
	 */
    public static <V extends Entity> void mergeInto(TLongObjectHashMap<V> dest, TLongObjectHashMap<V> src) {
        TLongObjectIterator<V> iterator = src.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            dest.put(iterator.key(), iterator.value());
        }
    }

    /**
	 * @param n
	 *            an entity to clear
	 * @return the cleared entity
	 */
    public static Node clearTags(Node n) {
        Node node = new Node(n.getId(), n.getVersion(), n.getTimestamp(), n.getUser(), n.getChangesetId(), n.getLongitude(), n.getLatitude());
        return node;
    }

    private static Date nullDate = new Date();

    /**
	 * @param n
	 *            an entity to clear
	 * @return the cleared entity
	 */
    public static Node clearMetadata(Node n) {
        Node node = new Node(n.getId(), -1, nullDate, OsmUser.NONE, -1, n.getLongitude(), n.getLatitude());
        return node;
    }
}
