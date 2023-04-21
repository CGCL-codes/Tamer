package org.personalsmartspace.lm.bayesian.test;

import org.personalsmartspace.lm.bayesian.impl.BayesianLearning;
import org.personalsmartspace.lm.bayesian.rule.BayesianRule;

public class LearnRule {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        BayesianLearning learner = new BayesianLearning();
        BayesianRule rule = (BayesianRule) learner.runLearning("", null, null).iterator().next();
        System.out.println(rule.getRule());
    }
}
