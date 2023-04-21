package org.phramer.mert.strategy;

import org.phramer.*;
import org.phramer.mert.evaluation.*;
import org.phramer.mert.item.*;

public interface SearchLambdaStrategy {

    public double[] getNewLambdas(int iteration, Evaluator e, ReferenceWithHypotheses[] f, double[] intialLambda, double[] bestPrevLambda, int maxLambdaAlter) throws PhramerException;

    public Object getLastSearchInfo();
}
