package com.aliasi.classify;

/**
 * The {@code ConditionalClassifier} interface specifies a single method
 * for n-best classification with conditional category probabilities.
 * 
 * @author  Bob Carpenter
 * @version 3.9.1
 * @since   LingPipe3.9.1
 * @param <E> the type of objects being classified
 */
public interface ConditionalClassifier<E> extends ScoredClassifier<E> {

    /**
     * Returns the n-best conditional probability classification for
     * the specified input.
     *
     * @param input Object to classify.
     * @return Classification of object.
     */
    public ConditionalClassification classify(E input);
}
