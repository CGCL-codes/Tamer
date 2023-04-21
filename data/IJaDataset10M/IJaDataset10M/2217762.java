package SpecialCollections;

import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * The basic insertion and query operations supported by classes implementing spatial index algorithms.
 * <p>
 * A spatial index typically provides a primary filter for range rectangle queries. A secondary filter is required to test for exact intersection. Of
 * course, this secondary filter may consist of other tests besides intersection, such as testing other kinds of spatial relationships.
 * 
 * @version 1.5
 */
public interface SpatialIndex<T> {

    /**
	 * Adds a spatial item with an extent specified by the given {@link Rectangle2D} to the index
	 */
    void insert(Rectangle2D itemEnv, T item);

    /**
	 * Queries the index for all items whose extents intersect the given search {@link Rectangle2D} Note that some kinds of indexes may also return
	 * objects which do not in fact intersect the query envelope.
	 * 
	 * @param searchEnv the envelope to query for
	 * @return a list of the items found by the query
	 */
    List query(Rectangle2D searchEnv);

    /**
	 * Removes a single item from the tree.
	 * 
	 * @param itemEnv the Envelope of the item to remove
	 * @param item the item to remove
	 * @return <code>true</code> if the item was found
	 */
    boolean remove(Rectangle2D itemEnv, T item);
}
