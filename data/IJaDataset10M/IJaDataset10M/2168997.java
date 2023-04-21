package org.rascalli.mbe;

import java.util.List;
import org.openrdf.OpenRDFException;
import org.openrdf.model.URI;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQueryResultHandler;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

/**
 * <p>
 * Extracts the text value of the rascalli:Utterance from the repository. If
 * there are more than one utterances, an arbitrary one is extracted. If there
 * is no utterance, {@link #getUtterance()} returns null.
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: $<br/> $Date: $<br/> $Revision: $
 * </p>
 * 
 * @author Christian Schollum
 */
public class AnswerCandidateExtractor implements TupleQueryResultHandler {

    private static final String ANSWER_CANDIDATE_CLASS_TYPE = "AnswerCandidateClassType";

    private static final String TEXT_VALUE = "TextValue";

    private static final String QUERY = "SELECT " + ANSWER_CANDIDATE_CLASS_TYPE + ", " + TEXT_VALUE + " FROM " + "{AnswerCandidate} rdf:type {r:AnswerCandidate};" + "[r:hasAnswerCandidateClass {AnswerCandidateClass} rdf:type {" + ANSWER_CANDIDATE_CLASS_TYPE + "}]," + "[{AnswerCandidate} r:literalStringValue {LiteralString}, {LiteralString} rdf:type {r:StringLiteral}; r:textValue {" + TEXT_VALUE + "}]" + RdfData.NAMESPACE_CLAUSE;

    /**
     * @throws RepositoryException
     * @throws MalformedQueryException
     * @throws TupleQueryResultHandlerException
     * @throws QueryEvaluationException
     * 
     */
    public static AnswerCandidateExtractor extractAnswerCandidate(Repository repository) throws RdfException {
        try {
            RepositoryConnection con = repository.getConnection();
            try {
                final AnswerCandidateExtractor extractor = new AnswerCandidateExtractor();
                con.prepareTupleQuery(QueryLanguage.SERQL, QUERY).evaluate(extractor);
                return extractor;
            } finally {
                con.close();
            }
        } catch (OpenRDFException e) {
            throw new RdfException(e);
        }
    }

    private String textValue = null;

    private String answerCandidateClass = null;

    public void endQueryResult() throws TupleQueryResultHandlerException {
    }

    public void handleSolution(BindingSet bindingSet) throws TupleQueryResultHandlerException {
        Binding binding = bindingSet.getBinding(TEXT_VALUE);
        if (binding != null) {
            textValue = binding.getValue().stringValue();
        }
        binding = bindingSet.getBinding(ANSWER_CANDIDATE_CLASS_TYPE);
        if (binding != null) {
            answerCandidateClass = ((URI) binding.getValue()).getLocalName();
        }
    }

    public void startQueryResult(List<String> bindingNames) throws TupleQueryResultHandlerException {
    }

    /**
     * @return the textValue
     */
    public String getTextValue() {
        return textValue;
    }

    /**
     * @return the answerCandidateClass
     */
    public String getAnswerCandidateClass() {
        return answerCandidateClass;
    }

    public boolean hasAnswerCandidate() {
        return textValue != null || answerCandidateClass != null;
    }
}
