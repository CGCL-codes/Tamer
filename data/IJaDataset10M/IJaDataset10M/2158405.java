package net.sf.myra.hantminer;

import net.sf.myra.datamining.Rule;
import net.sf.myra.framework.Cost;
import net.sf.myra.framework.Trail;

/**
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 2264 $ $Date:: 2010-02-04 15:00:24#$
 */
public class Pruner {

    /**
	 * The rule evaluator instance.
	 */
    private RuleEvaluator evaluator;

    /**
	 * Defautl constructor.
	 * 
	 * @param evaluator the rule evaluator instance used to evaluate the
	 *        candidate rules.
	 */
    public Pruner(RuleEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    /**
	 * Returns a (potentially) improved rule by pruning both antecedent and
	 * consequent of the specified rule.
	 * 
	 * @param rule the rule to be pruned.
	 * 
	 * @return a (potentially) improved rule by pruning both antecedent and
	 *         consequent of the specified rule.
	 */
    public HybridRule explore(HybridRule rule) {
        HybridRule rBest = rule;
        Cost qBest = evaluator.evaluate(rBest);
        rule = rule.clone();
        do {
            HybridRule candidate = prune(rule, evaluator.evaluate(rule));
            Cost cost = evaluator.evaluate(candidate);
            if (cost.compareTo(qBest) > -1) {
                rBest = candidate;
                qBest = cost;
            } else {
                break;
            }
            if (!rule.isEmpty()) {
                rule.getAntecedent().remove(rule.getAntecedent().getLast());
            }
        } while (rule.getAntecedent().getSize() > 0);
        rBest.setCost(qBest);
        return rBest;
    }

    /**
	 * Returns a (potentially) better rule by removing one class label at a
	 * time from the rule consequent.
	 * 
	 * @param candidate the candidate rule to be pruned.
	 *
	 * @return a (potentially) better rule by removing one class label at a
	 * time from the rule consequent.
	 */
    private HybridRule prune(HybridRule candidate, Cost cost) {
        candidate = candidate.clone();
        Rule antecedent = candidate.getAntecedent();
        Trail consequent = candidate.getConsequent().clone();
        while (consequent.getSize() > 2) {
            consequent.remove(consequent.getLast());
            Rule clone = antecedent.clone();
            HybridRule r = new HybridRule(clone, consequent.clone());
            Cost c = evaluator.evaluate(r);
            if (c.compareTo(cost) > 0) {
                candidate = r;
                cost = c;
            }
        }
        return candidate;
    }

    public RuleEvaluator getEvaluator() {
        return evaluator;
    }
}
