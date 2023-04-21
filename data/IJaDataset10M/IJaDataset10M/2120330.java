package net.sf.crsx.generic;

import java.util.Map;
import net.sf.crsx.Match;
import net.sf.crsx.Sink;
import net.sf.crsx.Substitute;
import net.sf.crsx.Valuation;
import net.sf.crsx.Variable;
import net.sf.crsx.util.ExtensibleMap;
import net.sf.crsx.util.LinkedExtensibleMap;

/**
 * Generic implementation of a valuation.
 * 
 * @author <a href="http://www.research.ibm.com/people/k/krisrose">Kristoffer Rose</a>.
 * @version $Id: GenericValuation.java,v 1.8 2011/04/16 04:34:01 krisrose Exp $
 */
class GenericValuation implements Valuation {

    /** Rule of the valuation. */
    protected final GenericRule rule;

    /** Map from meta-variable names to substitutes, and of free variables. */
    protected final Match match;

    /** Renamings of bound variables (). */
    final Map<Variable, Variable> reuse;

    /**
	 * Construct valuation mapping patterns 
	 * @param rule to contract
	 * @param match of rule's pattern against a term
	 * @param reuse maps variables in contractum to the corresponding bound variable in redex to reuse
	 */
    GenericValuation(GenericRule rule, Match match, Map<Variable, Variable> reuse) {
        this.rule = rule;
        this.match = match;
        this.reuse = reuse;
    }

    public String name() {
        return rule.name;
    }

    public Substitute getSubstitute(String metaVariable) {
        return match.getSubstitute(metaVariable);
    }

    public Variable getVariable(Variable variable) {
        Variable v = reuse.get(variable);
        if (v == null) v = match.getVariable(variable);
        return v;
    }

    public Sink contract(Sink sink) {
        ExtensibleMap<Variable, Variable> mapped = LinkedExtensibleMap.EMPTY_RENAMING;
        for (Variable v : rule.fresh) {
            Variable freshV = sink.makeVariable(v.name(), v.promiscuous());
            mapped = mapped.extend(v, freshV);
        }
        return rule.contractum.contract(sink, this, mapped);
    }

    public boolean leave() {
        return rule.leaf;
    }

    public String toString() {
        return "Valuation [" + rule.name + "]: " + match + " --> " + rule.contractum;
    }
}
