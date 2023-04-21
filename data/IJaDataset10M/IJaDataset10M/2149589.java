package org.cleartk.classifier;

import java.util.List;

/**
 * <br>
 * Copyright (c) 2009, Regents of the University of Colorado <br>
 * All rights reserved.
 * <p>
 */
public interface SequenceClassifier<OUTCOME_TYPE> {

    /**
   * Classifies a sequence of feature lists.
   * 
   * @param features
   *          a list of features for each member in the sequence
   * @return a list of the classifications made.
   */
    public List<OUTCOME_TYPE> classify(List<List<Feature>> features) throws CleartkProcessingException;

    /**
   * Get the N best sequence classifications along with their scores.
   * 
   * @param features
   *          a list of features for each member in the sequence
   * @param maxResults
   *          the maximum number of classifications to return.
   * @return a sorted list of the best N sequence classifications with their scores.
   */
    public List<ScoredOutcome<List<OUTCOME_TYPE>>> score(List<List<Feature>> features, int maxResults) throws CleartkProcessingException;
}
