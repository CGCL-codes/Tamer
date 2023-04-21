package edu.ucla.sspace.evaluation;

/**
 * A report of the performance of a {@link SemanticSpace} on a particular
 * {@link WordSimilarityEvaluation} test.
 *
 * @author David Jurgens
 */
public interface WordSimilarityReport {

    /**
     * Returns the total number of word pairs.
     */
    int numberOfWordPairs();

    /**
     * Returns the correlation between the similarity judgemnts from a {@link
     * SemanticSpace} similarity and the provided human similarity judgements.
     */
    double correlation();

    /**
     * Returns the number of questions for which a {@link SemanticSpace}
     * could not give an answer due to missing word vectors.
     */
    int unanswerableQuestions();
}
