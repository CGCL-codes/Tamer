package net.sourceforge.xconf.toolbox.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * OR predicate.
 * 
 * @author Tom Czarniecki
 */
public class OrRule implements Rule {

    private List rules;

    public OrRule(List rules) {
        this.rules = rules;
    }

    public OrRule(Rule one, Rule two) {
        rules = new ArrayList(2);
        rules.add(one);
        rules.add(two);
    }

    /**
     * Returns <code>true</code> if any rule in the list evaluates to <code>true</code>.
     * Iteration stops on the first rule that evaluates to <code>true</code>.
     * Empty lists evaluate to <code>false</code>.
     */
    public boolean evaluate(Object context) {
        for (Iterator itr = rules.iterator(); itr.hasNext(); ) {
            Rule predicate = (Rule) itr.next();
            if (predicate.evaluate(context)) {
                return true;
            }
        }
        return false;
    }
}
