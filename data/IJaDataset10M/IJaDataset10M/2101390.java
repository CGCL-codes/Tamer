package org.archive.crawler.scope;

import org.archive.crawler.framework.CrawlScope;
import org.archive.processors.ProcessorURI;
import org.archive.processors.deciderules.DecideResult;
import org.archive.processors.deciderules.DecideRuleSequence;
import org.archive.state.Key;
import org.archive.state.StateProvider;

/**
 * DecidingScope: a Scope which makes its accept/reject decision based on 
 * whatever DecideRules have been set up inside it.
 * @author gojomo
 */
public class DecidingScope extends CrawlScope {

    private static final long serialVersionUID = 3L;

    public static final Key<DecideRuleSequence> DECIDE_RULES = Key.make(new DecideRuleSequence());

    public static final String ATTR_DECIDE_RULES = "decide-rules";

    public DecidingScope() {
        super();
    }

    @Override
    protected DecideResult innerDecide(ProcessorURI uri) {
        return uri.get(this, DECIDE_RULES).decisionFor(uri);
    }
}
