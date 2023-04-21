package au.edu.diasb.annotation.danno.model;

/**
 * Objects that implement this interface may be used to filter the RDF statements
 * in a collection as they are being extracted; e.g. using 
 * {@link AnnoteaTypeFactory#createAnnoteaObject(java.io.InputStream, RDFStatementFilter)}.
 * 
 * @author scrawley
 */
public interface RDFStatementFilter {

    /**
     * Filter a supplied RDF statement, according to rules implemented
     * by this filter object.  An implementation of this method can do four
     * things:
     * <ol>
     * <li>It can return the supplied statement as-is.</li>
     * <li>It can create and return a new statement based on the supplied statement
     *     with the subject, predicate and/or object replaced with new values.</li>
     * <li>It can return {@code null} which will cause the statement and any descendant
     *     blank-nodes to be left out if the object being extracted.</li>
     * <li>It can throw {@link AnnoteaTypeException} to signal that the object should
     *     be rejected.</li>
     * </ol>
     * The @link AnnoteaTypeFactory} methods that use RDFStatementFilter objects 
     * currently do not allow a filter to turn a non-blank-node into a blank-node,
     * or change a blank-node id in either the 'subject' or 'object' position.
     * 
     * @param statement the statement to be filtered.
     * @param context the container to be used as context when evaluating guards
     * @return the original statement, a modified statement or {@code null}
     * @throws AnnoteaTypeException this is thrown to signal that the object being 
     *     extracted should be rejected as invalid.
     */
    RDFStatement filter(RDFStatement statement, RDFContainer context) throws AnnoteaTypeException;
}
