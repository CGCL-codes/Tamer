package org.personalsmartspace.lm.mining.bayesian.impl.bayesianLibrary.bayesianLearner.exceptions;

/**
 * @author robert_p
 *
 */
public class NodeValueIndexNotInNodeRangeException extends Exception {

    public NodeValueIndexNotInNodeRangeException(String msg) {
        super(msg);
    }

    public NodeValueIndexNotInNodeRangeException(RangeValueNotApplicableException e) {
        super(e);
    }
}
