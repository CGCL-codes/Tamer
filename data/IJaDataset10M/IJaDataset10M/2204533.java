package org.modss.facilitator.model.v1;

import org.modss.facilitator.util.description.Describable;
import org.modss.facilitator.util.collection.tree.RankingNode;

/**
 * This interface represents a single result node in a result set.
 * Currently the single algorithm used in the facilitator creates a
 * result which is a tree.  Every node (both leaf and non-leaf) in the tree
 * has an attribute reference which is a result node.
 * <p>
 * Each result node contains a reference to a {@link BaseCriteria} and an
 * array of {@link ResultEntry}s.  The number of result entries matches the
 * number of {@link Alternative}s.  Each result entry contains an alternative
 * and a {@link Result}.  The {@link Result} contains a minimum and maximum
 * value.
 *
 * @author mag@netstorm.net.au
 */
public interface ResultNode extends Describable {

    /**
     * Obtain the criteria associated with this node.
     * If the node is associated with a non-leaf then the criteria will
     * be a {@link CompositeCriteria}, otherwise it will be a
     * {@link BaseCriteria}.
     *
     * @return the criteria associated with this result node.
     */
    public Criteria getCriteria();

    /**
     * Obtain the array of result entries associated with this result entry.
     *
     * @return an array of result entries.
     */
    public ResultEntry[] getResults();

    /**
     * This interface represents a single result entry.
     * A result entry has an {@link Alternative} and a result reference.
     */
    public static interface ResultEntry {

        /**
         * Obtain the {@link Alternative} associated with this result entry.
         *
         * @return an alternative.
         */
        public Alternative getAlternative();

        /**
         * Obtain the result associated with this result entry.
         *
         * @return a reference which contains the actual results.
         */
        public Result getResult();
    }

    /**
     * This interface represents the actual values associated with a result
     * entry.
     */
    public static interface Result {

        /**
         * Obtain the minimum result value.
         */
        public double getMin();

        /**
         * Obtain the maximum result value.
         */
        public double getMax();
    }
}
