package gate.mimir.search.query;

import gate.mimir.Constraint;
import gate.mimir.ConstraintType;
import gate.mimir.IndexConfig;
import gate.mimir.IndexConfig.SemanticIndexerConfig;
import gate.mimir.SemanticAnnotationHelper;
import gate.mimir.index.Mention;
import gate.mimir.search.QueryEngine;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import it.unimi.dsi.fastutil.objects.ReferenceSets;
import it.unimi.dsi.mg4j.index.Index;
import it.unimi.dsi.mg4j.search.visitor.DocumentIteratorVisitor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * A query for the annotations index. 
 */
public class AnnotationQuery implements QueryNode {

    private static final long serialVersionUID = 5996543707885867821L;

    public static class AnnotationQueryExecutor extends AbstractQueryExecutor {

        /**
     * @param engine
     * @param query
     */
        public AnnotationQueryExecutor(AnnotationQuery query, QueryEngine engine) throws IOException {
            super(engine, query);
            this.query = query;
            buildQuery();
        }

        /**
     * The query being executed.
     */
        private AnnotationQuery query;

        /**
     * Logger for this class.
     */
        private static Logger logger = Logger.getLogger(AnnotationQueryExecutor.class);

        /**
     * The underlying OrQuery executor that actually does the work.
     */
        private QueryExecutor underlyingExecutor;

        private transient boolean isInDocumentMode;

        /**
     * Build the underlying OrQuery executor that this annotation query uses.
     */
        protected void buildQuery() throws IOException {
            SemanticAnnotationHelper helper = getAnnotationHelper(engine.getIndexConfig());
            isInDocumentMode = (helper.getMode() == SemanticAnnotationHelper.Mode.DOCUMENT);
            long start = System.currentTimeMillis();
            List<Mention> mentions = helper.getMentions(query.getAnnotationType(), query.getConstraints(), engine);
            logger.debug(mentions.size() + " mentions obtained in " + (System.currentTimeMillis() - start) + " ms");
            if (mentions.size() > 0) {
                QueryNode[] disjuncts = new QueryNode[mentions.size()];
                int index = 0;
                for (Mention m : mentions) {
                    disjuncts[index] = new TermQuery(query.annotationType, m.getUri(), m.getLength());
                    index++;
                }
                QueryNode underlyingQuery = new OrQuery(disjuncts);
                underlyingExecutor = underlyingQuery.getQueryExecutor(engine);
            } else {
                latestDocument = -1;
            }
        }

        /**
     * Get the {@link SemanticAnnotationHelper} corresponding to this query's
     * annotation type.
     * @param indexConfig the index configuration
     * @throws IllegalArgumentException if the annotation helper for this
     *         type is not a {@link PlainAnnotationHelper}.
     */
        protected SemanticAnnotationHelper getAnnotationHelper(IndexConfig indexConfig) {
            for (SemanticIndexerConfig semConfig : indexConfig.getSemanticIndexers()) {
                for (int i = 0; i < semConfig.getAnnotationTypes().length; i++) {
                    if (query.getAnnotationType().equals(semConfig.getAnnotationTypes()[i])) {
                        return semConfig.getHelpers()[i];
                    }
                }
            }
            throw new IllegalArgumentException("Semantic annotation type \"" + query.getAnnotationType() + "\" not known to this query engine.");
        }

        public void close() throws IOException {
            super.close();
            if (underlyingExecutor != null) underlyingExecutor.close();
        }

        public int getLatestDocument() {
            if (closed || latestDocument == -1) return -1;
            return underlyingExecutor.getLatestDocument();
        }

        public int nextDocument(int greaterThan) throws IOException {
            if (closed || latestDocument == -1) return -1;
            return latestDocument = underlyingExecutor.nextDocument(greaterThan);
        }

        public Binding nextHit() throws IOException {
            if (closed || latestDocument == -1) return null;
            Binding underlyingHit = underlyingExecutor.nextHit();
            if (underlyingHit == null) return null;
            int doc = underlyingHit.getDocumentId();
            if (isInDocumentMode) {
                return new Binding(query, doc, 0, engine.getDocumentSizes().getInt(doc), underlyingHit.getContainedBindings());
            } else {
                return new Binding(query, doc, underlyingHit.getTermPosition(), underlyingHit.getLength(), underlyingHit.getContainedBindings());
            }
        }

        @Override
        public ReferenceSet<Index> indices() {
            if (underlyingExecutor != null) {
                return underlyingExecutor.indices();
            } else {
                return ReferenceSets.EMPTY_SET;
            }
        }

        public <T> T accept(final DocumentIteratorVisitor<T> visitor) throws IOException {
            if (underlyingExecutor == null) return null;
            if (!visitor.visitPre(this)) return null;
            final T[] a = visitor.newArray(1);
            if (a == null) {
                if (underlyingExecutor.accept(visitor) == null) return null;
            } else {
                if ((a[0] = underlyingExecutor.accept(visitor)) == null) return null;
            }
            return visitor.visitPost(this, a);
        }

        public <T> T acceptOnTruePaths(final DocumentIteratorVisitor<T> visitor) throws IOException {
            if (underlyingExecutor == null) return null;
            if (!visitor.visitPre(this)) return null;
            final T[] a = visitor.newArray(1);
            if (a == null) {
                if (underlyingExecutor.acceptOnTruePaths(visitor) == null) return null;
            } else {
                if ((a[0] = underlyingExecutor.acceptOnTruePaths(visitor)) == null) return null;
            }
            return visitor.visitPost(this, a);
        }
    }

    /**
   * Constructs a new {@link AnnotationQuery}.
   * 
   * Convenience variant of {@link #AnnotationQuery(String, List)} 
   * for cases where all predicates are of type 
   * {@link SemanticAnnotationHelper.ConstraintType#EQ}.
   * 
   * @param annotationType the desired annotation type, for the annotations to 
   * be matched.
   * @param featureConstraints the constraints over the features of the 
   * annotations to be found. This is represented as a {@link Map} from feature
   * name (a {@link String}) to feature value (also a {@link String}).
   * 
   * @see AnnotationQuery#AnnotationQuery(String, List)  
   */
    public AnnotationQuery(String annotationType, Map<String, String> featureConstraints) {
        if (featureConstraints == null) {
            featureConstraints = new HashMap<String, String>();
        }
        this.annotationType = annotationType;
        this.constraints = new ArrayList<Constraint>(featureConstraints.size());
        for (Map.Entry<String, String> entry : featureConstraints.entrySet()) {
            this.constraints.add(new Constraint(ConstraintType.EQ, entry.getKey(), entry.getValue()));
        }
    }

    /**
   * Constructs a new Annotation Query.
   *  
   * @param annotationType the type of annotation being sought.
   * @param constraints a list of constraints placed on the feature values. An 
   * empty constraints list will make no requests regarding the feature values,
   * hence it will match all annotations of the right type. 
   */
    public AnnotationQuery(String annotationType, List<Constraint> constraints) {
        this.annotationType = annotationType;
        this.constraints = constraints == null ? new ArrayList<Constraint>() : constraints;
    }

    public QueryExecutor getQueryExecutor(QueryEngine engine) throws IOException {
        return new AnnotationQueryExecutor(this, engine);
    }

    /**
   * Gets the annotation type for this query. 
   * @return the annotationType
   */
    public String getAnnotationType() {
        return annotationType;
    }

    /**
   * Gets the feature constraints, represented as a {@link Map} from 
   * feature name (a {@link String}) to feature value (also a {@link String}). 
   * @return the featureConstraints
   */
    public List<Constraint> getConstraints() {
        return constraints;
    }

    /**
   * The annotation type for this query.
   */
    private String annotationType;

    /**
   * The constrains over the annotation features.
   */
    private List<Constraint> constraints;

    public String toString() {
        return "Annotation ( type = " + annotationType + ", features=" + (constraints != null ? constraints.toString() : "[]") + ")";
    }
}
