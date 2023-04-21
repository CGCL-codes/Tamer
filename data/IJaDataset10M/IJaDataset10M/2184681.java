package org.exteca.pattern;

import org.exteca.language.Spans;
import org.exteca.pattern.rules.AndRule;
import org.exteca.pattern.rules.ConceptRule;
import org.exteca.pattern.rules.LeafRule;
import org.exteca.pattern.rules.MatchRule;
import org.exteca.pattern.rules.OrRule;
import org.exteca.pattern.rules.ProbabilityRule;
import org.exteca.pattern.rules.SequenceRule;

/**
 * Factory for creation of all types of rules.
 * 
 * @author Mauro Talevi
 */
public class RuleFactory {

    private RuleNetwork network;

    /**
	 * Creates a RuleFactory
	 * 
	 * @param network the RuleNetwork
	 */
    public RuleFactory(RuleNetwork network) {
        this.network = network;
    }

    public NodeRule createAndRule(int weight) {
        return new AndRule(network, weight);
    }

    public NodeRule createConceptRule(int weight) {
        return new ConceptRule(network, weight);
    }

    public Rule createLeafRule(int weight) {
        return new LeafRule(network, weight);
    }

    public NodeRule createMatchRule(int weight, Spans spans) {
        return new MatchRule(network, weight, spans);
    }

    public NodeRule createOrRule(int weight) {
        return new OrRule(network, weight);
    }

    public NodeRule createProbabilityRule(int weight) {
        return new ProbabilityRule(network, weight);
    }

    public NodeRule createSequenceRule(int weight) {
        return new SequenceRule(network, weight);
    }
}
