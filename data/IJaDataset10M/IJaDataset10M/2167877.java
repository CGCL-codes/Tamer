package net.sf.metarbe.impl;

import java.util.Map;
import net.sf.metarbe.RuleContext;
import net.sf.metarbe.RuleContextValidation;
import net.sf.metarbe.RuleSessionRunnable;
import net.sf.metarbe.RuleSessionType;
import net.sf.metarbe.StatefulRuleSession;

public class RunnableStatefulSession extends RunnableStatelessSession implements StatefulRuleSession {

    public RunnableStatefulSession(RuleSessionRunnable runnable) {
        super(runnable);
    }

    public void addContextValidation(RuleContext aRuleContext, RuleContextValidation aValidation) {
        sessionSupport.addContextValidation(aRuleContext, aValidation);
    }

    public RuleContextValidation getContextValidation(RuleContext aRuleContext) {
        return sessionSupport.getContextValidation(aRuleContext);
    }

    public Map<RuleContext, RuleContextValidation> getContextValidations() {
        return sessionSupport.getContextValidations();
    }

    public void removeContextValidation(RuleContext aRuleContext) {
        sessionSupport.removeContextValidation(aRuleContext);
    }

    @Override
    public RuleSessionType getSessionType() {
        return RuleSessionType.STATEFUL_SESSION;
    }
}
