package org.mitre.midiki.agent;

import org.mitre.midiki.state.ImmutableInfoState;
import org.mitre.midiki.state.InfoState;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.logging.*;

/**
 * Encapsulates a collection of related, mutually-exclusive
 * <code>Rule</code>s. Any single <code>RuleSet</code> should
 * contain <code>Rule</code>s which all potentially apply in
 * a selected situation, but which may be executed independently
 * of each other. [see the simple grounding module from the
 * godis sample.]<p>
 * Some subclasses of RuleSet may also perform optimizations
 * upon the component rules, such as construction of a Rete network.
 *
 * @author <a href="mailto:cburke@mitre.org">Carl Burke</a>
 * @version 1.0
 * @since 1.0
 * @see Rule
 */
public class RuleSet extends LinkedList implements Rule {

    private static Logger logger = Logger.getLogger("org.mitre.midiki.agent.RuleSet");

    /**
     * Evaluates the component <code>Rule</code>s, returning <code>true</code>
     * if any of them succeeds. The order of evaluation is not specified;
     * any implementation depending on a specific order of evaluation
     * for component <code>Rule</code>s is erroneous.
     *
     * @param infoState an <code>ImmutableInfoState</code> value
     * @return a <code>boolean</code> value
     */
    public final boolean evaluate(InfoState infoState) {
        logger.entering(getClass().getName(), "evaluate");
        Iterator it = iterator();
        while (it.hasNext()) {
            Rule r = (Rule) it.next();
            if (r.evaluate(infoState)) {
                logger.logp(Level.FINER, getClass().getName(), "evaluate", "succeeded");
                return (r.succeeded() && execute(infoState));
            }
        }
        logger.logp(Level.FINER, getClass().getName(), "evaluate", "failed");
        return false;
    }

    /**
     * Returns <code>true</code> if the rule executed successfully.
     *
     * @return a <code>boolean</code> value
     */
    public boolean succeeded() {
        return true;
    }

    /**
     * Specifies actions to be taken upon success. Since the evaluation
     * method for a <code>RuleSet</code> is fixed, clients cannot override
     * the <code>evaluate</code> method to specify actions to be taken
     * upon success. Instead, clients should override this method, which
     * is called whenever one of the the component <code>Rule</code>s
     * succeeds. The return value of this method will be reported as the
     * success or failure of the <code>RuleSet</code> evaluation.<p>
     * The baseline dialogue manager, inspired by Godis, never overrides
     * this method.<p>
     *
     * @param infoState the current mutable <code>InfoState</code>
     * @return <code>true</code> if the actions succeed (default)
     */
    protected boolean execute(InfoState infoState) {
        return true;
    }

    /**
     * Creates a new <code>RuleSet</code> instance with no components.
     *
     */
    public RuleSet() {
        super();
    }

    /**
     * Creates a new <code>RuleSet</code> instance from an existing
     * <code>Collection</code>. The <code>Collection</code> is stored
     * in <code>theRules</code>, and should not be modified after the
     * <code>RuleSet</code> is created.<p>
     *
     * @param rules a <code>Collection</code> of <code>Rule</code>s
     */
    public RuleSet(Collection rules) {
        super(rules);
    }

    /** Returns a description of the information state structure
     *  expected and required by this RuleSet. At present, this is
     *  likely to be only those elements the designer expects to be
     *  unique or unusual, buried in the component Rules. It may
     *  migrate from a recommendation to a requirement in future
     *  versions. It may also migrate from RuleSet to Rule, so that
     *  metadata may be obtained from non-aggregated Rules.
     *  
     * @return a <code>Collection</code> of <code>CellFeature</code>s
     */
    public Collection metadata() {
        return null;
    }
}
