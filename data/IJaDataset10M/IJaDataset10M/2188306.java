package net.obsearch.dimension;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import net.obsearch.Index;
import net.obsearch.OB;
import net.obsearch.exception.IllegalIdException;
import net.obsearch.exception.OBException;
import net.obsearch.exception.OBStorageException;
import net.obsearch.ob.OBShort;
import cern.colt.list.LongArrayList;

/**
 * AbstractDimension stores a dimension (the order it is and some abstract
 * primitive value).
 * 
 * @author Arnoldo Jose Muller Molina
 */
public abstract class AbstractDimension {

    /**
	 * The position of this dimension. Note that this value could be inferred
	 * from the position in the array. However, if we want to sort dimensions by
	 * their value then it is necessary to keep this value.
	 */
    private int order;

    protected AbstractDimension(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    /**
	 * Returns the max # of elements. if source != null then source.size()
	 * otherwise index.databaseSize();
	 * 
	 * @param source
	 *            The source of data (can be null)
	 * @param index
	 *            The underlying index.
	 * @return The max # of elements of source if source != null or of index if
	 *         source == null.
	 */
    public static int max(LongArrayList source, Index index) throws OBStorageException {
        int max;
        if (source == null) {
            max = (int) Math.min(index.databaseSize(), Integer.MAX_VALUE);
        } else {
            max = source.size();
        }
        return max;
    }

    protected static long mapId(long i, LongArrayList elements) {
        if (elements != null) {
            return elements.get((int) i);
        } else {
            return i;
        }
    }

    /**
	 * Selects k random elements from the given source.
	 * 
	 * @param k
	 *            number of elements to select
	 * @param r
	 *            Random object used to randomly select objects.
	 * @param source
	 *            The source of item ids.
	 * @param index
	 *            underlying index.
	 * @param will
	 *            not add pivots included in excludes.
	 * @param minDistance
	 *            The min distance required by the objects.
	 * @return The ids of selected objects.
	 */
    public static long[] select(int k, Random r, LongArrayList source, Index<OB> index, LongArrayList excludes) throws IllegalIdException, OBException, IllegalAccessException, InstantiationException {
        int max = max(source, index);
        long[] res = new long[k];
        int i = 0;
        List<Long> l = new LinkedList<Long>();
        while (i < res.length) {
            long id = mapId(r.nextInt(max), source);
            if ((excludes == null || !excludes.contains(id))) {
                res[i] = id;
                l.add(id);
            } else {
                l.remove(l.size() - 1);
                continue;
            }
            i++;
        }
        return res;
    }
}
