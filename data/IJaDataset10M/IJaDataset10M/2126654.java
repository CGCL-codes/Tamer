package org.mitre.midiki.agent;

import org.mitre.midiki.state.InfoState;
import org.mitre.midiki.state.InfoStateEvent;
import org.mitre.midiki.state.InfoListener;
import java.util.logging.*;

/**
 * Specifies a <code>Rule</code> to be evaluated when an attribute
 * in the information state changes.
 *
 * @author <a href="mailto:cburke@mitre.org">Carl Burke</a>
 * @version 1.0
 * @see InfoListener
 * @since 1.0
 */
public class RuleBasedInfoListener implements InfoListener {

    private static Logger logger = Logger.getLogger("org.mitre.midiki.agent.RuleBasedInfoListener");

    /**
     * The <code>Rule</code> to be evaluated when the attribute changes.
     * {transient=false, volatile=false}
     */
    private Rule rule;

    /**
     * Responds to a change in an attribute. This class of listeners
     * ignores the new attribute value, delegating the task of evaluation
     * to a <code>Rule</code>. The evaluation need not use the triggering
     * attribute, and may be as complex as desired.<p>
     *
     * @param event the current <code>InfoState</code>
     */
    public final void infoChanged(InfoStateEvent event) {
        logger.entering(getClass().getName(), "infoChanged", event.getAttribute());
        rule.evaluate(event.getInfoState());
        logger.exiting(getClass().getName(), "infoChanged", event.getAttribute());
    }

    /**
     * Creates a new <code>RuleBasedInfoListener</code> instance.
     *
     */
    public RuleBasedInfoListener() {
    }

    /**
     * Creates a new <code>RuleBasedInfoListener</code> instance
     * which will evaluate the specified <code>Rule</code> when notified.
     *
     * @param triggerRule an arbitrary <code>Rule</code>
     */
    public RuleBasedInfoListener(Rule triggerRule) {
        rule = triggerRule;
    }
}
