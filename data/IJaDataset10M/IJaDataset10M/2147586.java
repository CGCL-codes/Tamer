package gate.mimir.search.query;

import gate.mimir.search.QueryEngine;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import it.unimi.dsi.mg4j.index.Index;
import it.unimi.dsi.mg4j.search.visitor.DocumentIteratorVisitor;
import java.io.IOException;
import java.util.*;

/**
 * A query node that wraps another query node and adds a gap with a specified
 * length (as number of terms) at the end of every result.
 */
public class GapQuery implements QueryNode {

    private static final long serialVersionUID = 223755948497895844L;

    public static class GapQueryExecutor extends AbstractQueryExecutor {

        /**
     * The query to be executed.
     */
        protected GapQuery gapQuery;

        /**
     * The executor of the query wrapped by this gap query.
     */
        protected QueryExecutor wrappedExecutor;

        /**
     * @param queryNode
     * @throws IOException 
     */
        public GapQueryExecutor(GapQuery queryNode, QueryEngine engine) throws IOException {
            super(engine, queryNode);
            this.gapQuery = queryNode;
            wrappedExecutor = queryNode.getWrappedQuery().getQueryExecutor(engine);
        }

        public void close() throws IOException {
            super.close();
            wrappedExecutor.close();
        }

        public int getLatestDocument() {
            return wrappedExecutor.getLatestDocument();
        }

        public int nextDocument(int from) throws IOException {
            if (closed) return -1;
            return wrappedExecutor.nextDocument(from);
        }

        public Binding nextHit() throws IOException {
            if (closed) return null;
            Binding wrappedBinding = wrappedExecutor.nextHit();
            if (wrappedBinding != null) {
                Binding[] containedBindings = null;
                if (engine.isSubBindingsEnabled()) {
                    Binding[] subBindings = wrappedBinding.getContainedBindings();
                    containedBindings = subBindings == null ? new Binding[1] : new Binding[subBindings.length + 1];
                    if (subBindings != null) {
                        System.arraycopy(subBindings, 0, containedBindings, 1, subBindings.length);
                        wrappedBinding.setContainedBindings(null);
                    }
                    containedBindings[0] = wrappedBinding;
                }
                return new Binding(gapQuery, wrappedBinding.getDocumentId(), wrappedBinding.getTermPosition(), wrappedBinding.getLength() + gapQuery.getGap(), containedBindings);
            } else {
                return null;
            }
        }

        @Override
        public ReferenceSet<Index> indices() {
            return wrappedExecutor.indices();
        }

        public <T> T accept(final DocumentIteratorVisitor<T> visitor) throws IOException {
            if (!visitor.visitPre(this)) return null;
            final T[] a = visitor.newArray(1);
            if (a == null) {
                if (wrappedExecutor.accept(visitor) == null) return null;
            } else {
                if ((a[0] = wrappedExecutor.accept(visitor)) == null) return null;
            }
            return visitor.visitPost(this, a);
        }

        public <T> T acceptOnTruePaths(final DocumentIteratorVisitor<T> visitor) throws IOException {
            if (!visitor.visitPre(this)) return null;
            final T[] a = visitor.newArray(1);
            if (a == null) {
                if (wrappedExecutor.acceptOnTruePaths(visitor) == null) return null;
            } else {
                if ((a[0] = wrappedExecutor.acceptOnTruePaths(visitor)) == null) return null;
            }
            return visitor.visitPost(this, a);
        }
    }

    /**
   * The length of the gap to be added at the end of all results.
   */
    protected int gap;

    /**
   * The query node wrapped by this {@link GapQuery}.
   */
    protected QueryNode wrappedQuery;

    /**
   * Constructs a new {@link GapQuery}
   * @param wrappedQuery the query node wrapped by this {@link GapQuery}
   * @param gap the length of the gap (i.e. the number of terms to be added to 
   * the length of all results). 
   */
    public GapQuery(QueryNode wrappedQuery, int gap) {
        this.wrappedQuery = wrappedQuery;
        this.gap = gap;
    }

    /**
   * Gets the gap length.
   * @return the gap
   */
    public int getGap() {
        return gap;
    }

    /**
   * Gets the wrapped query.
   * @return the wrappedQuery
   */
    public QueryNode getWrappedQuery() {
        return wrappedQuery;
    }

    public QueryExecutor getQueryExecutor(QueryEngine engine) throws IOException {
        return new GapQueryExecutor(this, engine);
    }
}
