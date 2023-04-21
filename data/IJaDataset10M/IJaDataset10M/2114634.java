package rafa.math.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Filter which deletes values from incoming lists.
 * @author rafa
 */
public class DeletionFilter extends IndelFilter {

    /**
     * Creates a deletion filter.
     * @param mutationTargeter the object who decides the indexes to be
     *      deleted.
     */
    public DeletionFilter(MutationTargeter mutationTargeter) {
        setMutationTargeter(mutationTargeter);
    }

    /**
     * "Deletes" values from the original list.
     * @param numbers The original list of numbers.
     * @return a new list with some values deleted. The original list is not
     *      changed.
     */
    public List<Number> filter(List<? extends Number> numbers) {
        List<Number> filtered = new ArrayList<Number>(numbers);
        List<Integer> delIndexes = mutationTargeter.getMutationTargets(numbers);
        if (delIndexes != null && !delIndexes.isEmpty()) {
            Collections.sort(delIndexes, Collections.reverseOrder());
            for (Integer delIndex : delIndexes) {
                filtered.remove(delIndex.intValue());
            }
        }
        return filtered;
    }
}
