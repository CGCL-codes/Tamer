package net.sf.adatagenerator.api;

import com.choicemaker.shared.api.CMPair;

/**
 * @deprecated Moved to the com.choicemaker.correlation package
 */
public interface CorrelationEvaluator<R> {

    String computeCorrelationSignature(CMPair<? extends R> pair) throws EvaluationException;
}
