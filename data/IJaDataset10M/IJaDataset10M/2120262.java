package toxtree.plugins.kroes.rules;

import cramer2.rules.RuleUnchargedOrganophosphates;

/**
 * Is the compound an organophosphate?
 * @author nina
 *
 */
public class KroesRule6 extends RuleUnchargedOrganophosphates {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1776897873563751556L;

    public KroesRule6() {
        setID("Q6");
        setTitle("Is the compound an organophosphate?");
        setExplanation(getTitle());
    }
}
