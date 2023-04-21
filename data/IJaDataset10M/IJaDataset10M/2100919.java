package org.hibernate.search.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.hibernate.annotations.common.AssertionFailure;

/**
 * A Filter capable of chaining other filters, so that it's
 * possible to apply several filters on a Query.
 * <p>The resulting filter will only enable result Documents
 * if no filter removed it.</p>
 *
 * @author Emmanuel Bernard
 * @author Sanne Grinovero
 * @author Hardy Ferentschik
 */
public class ChainedFilter extends Filter {

    private static final long serialVersionUID = -6153052295766531920L;

    private final List<Filter> chainedFilters = new ArrayList<Filter>();

    /**
	 * Add the specified filter to the chain of filters
	 *
	 * @param filter the filter to add to the filter chain. Cannot be {@code null}.
	 */
    public void addFilter(Filter filter) {
        if (filter == null) {
            throw new IllegalArgumentException("The specified filter cannot be null");
        }
        this.chainedFilters.add(filter);
    }

    /**
	 * Returns the specified filter from the current filter chain.
	 *
	 * @param filter the filter to remove form the chaim
	 *
	 * @return {@code true} if this chained filter contained the specified filter, {@code false} otherwise.
	 */
    public boolean removeFilter(Filter filter) {
        return this.chainedFilters.remove(filter);
    }

    public boolean isEmpty() {
        return chainedFilters.size() == 0;
    }

    @Override
    public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
        int size = chainedFilters.size();
        if (size == 0) {
            throw new AssertionFailure("No filters to chain");
        } else if (size == 1) {
            return chainedFilters.get(0).getDocIdSet(reader);
        } else {
            List<DocIdSet> subSets = new ArrayList<DocIdSet>(size);
            for (Filter f : chainedFilters) {
                subSets.add(f.getDocIdSet(reader));
            }
            subSets = FilterOptimizationHelper.mergeByBitAnds(subSets);
            if (subSets.size() == 1) {
                return subSets.get(0);
            }
            return new AndDocIdSet(subSets, reader.maxDoc());
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ChainedFilter");
        sb.append("{chainedFilters=").append(chainedFilters);
        sb.append('}');
        return sb.toString();
    }
}
