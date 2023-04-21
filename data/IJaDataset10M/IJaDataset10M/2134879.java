package no.balder.rules;

import java.util.ArrayList;
import java.util.List;
import no.balder.rules.predicate.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple implementation of a business rule machinery, which purpose is to fire
 * rules whenever a previously declared event has been received.
 * <p>
 * The term "fire" means that the predicate of the action will be evaluated, and
 * if the result is true, the associated action is executed.
 * 
 * @author steinar
 * 
 */
class SimpleBusinessRuleMachine implements BusinessRuleMachine {

    private static final Logger log = LoggerFactory.getLogger(SimpleBusinessRuleMachine.class);

    /** Holds our predicate visitor */
    BusinessEventPredicateVisitor predicateVisitor = new SimplePredicateVisitor();

    /** Business events to which we should react */
    List<Class<? extends BusinessEvent>> businessEvents = new ArrayList<Class<? extends BusinessEvent>>();

    /** The rules to be fired in succession */
    List<BusinessRule> businessRules = new ArrayList<BusinessRule>();

    private int rulesFired = 0;

    public void addBusinessEvent(Class<? extends BusinessEvent> class1) {
        businessEvents.add(class1);
    }

    public void addBusinessRule(BusinessRule businessRule) {
        businessRules.add(businessRule);
    }

    public void postEvent(BusinessEvent businessEvent) throws BusinessRuleException {
        if (businessEvents.contains(businessEvent.getClass())) {
            log.debug("Firing rules for event " + businessEvent);
            for (BusinessRule rule : businessRules) {
                fire(businessEvent, rule);
                rulesFired++;
            }
        }
    }

    /**
	 * Number of rules fired
	 * 
	 * @return value of the property <code>rulesFired</code>
	 */
    public int getRulesFired() {
        return rulesFired;
    }

    public void setPredicateVisitor(BusinessEventPredicateVisitor predicateVisitor) {
        this.predicateVisitor = predicateVisitor;
    }

    /**
	 * Evaluates the predicate for the given business rule and executes the
	 * associated action if, and only if, the predicate evaluates to true.
	 * 
	 * @param businessEvent
	 *            event causing this rule to be inspected
	 * @param businessRule
	 *            the rule to be evaluated
	 * @throws BusinessRuleException
	 */
    void fire(BusinessEvent businessEvent, BusinessRule businessRule) throws BusinessRuleException {
        BusinessEventPredicateVisitor visitor = createVisitorForEvent(businessEvent);
        assert businessRule != null : "businessRule required";
        assert businessRule.getPredicate() != null : "businessRule " + businessRule.getName() + " requires a root predicate";
        try {
            boolean resultOfPredicateEvaluation = businessRule.getPredicate().accept(visitor);
            if (resultOfPredicateEvaluation) {
                log.debug("\tpredicate " + businessRule.getPredicate().getClass().getSimpleName() + " evaluated to true, executing action ==> " + businessRule.getBusinessAction().getClass().getSimpleName());
                businessRule.getBusinessAction().execute(businessEvent);
            } else {
                log.debug(businessRule.getPredicate().getClass().getSimpleName() + " evaluated to false, action skipped");
            }
        } catch (RuntimeException e) {
            log.error(businessRule.getName() + " threw rutnime exception " + e.getClass().getSimpleName(), e);
            throw e;
        }
    }

    protected BusinessEventPredicateVisitor createVisitorForEvent(BusinessEvent businessEvent) {
        predicateVisitor.setBusinessEvent(businessEvent);
        return predicateVisitor;
    }

    /**
     * Provides textual information on the configuration of the rule machine.
     *
     * @return  provides textual information on the configuration of the rule machine.
     */
    public String configurationInformation() {
        StringBuilder sb = new StringBuilder("\nBusiness rules config report:\n\n");
        sb.append("Machine name: ").append(SimpleBusinessRuleMachine.class.getSimpleName()).append('\n');
        sb.append("\n").append("Events which will cause evaluation of rules (order is not important):\n");
        for (Class<? extends BusinessEvent> businessEvent : businessEvents) {
            sb.append("\t").append(businessEvent.getSimpleName()).append("\n");
        }
        sb.append("\nBusiness rules to be evaluated in order:");
        int ruleNo = 1;
        for (BusinessRule businessRule : businessRules) {
            sb.append("\n\n\t§").append(ruleNo++).append(' ').append(businessRule.getName()).append(" - ").append(businessRule.getDescription()).append('\n');
            Predicate rootPredicate = businessRule.getPredicate();
            sb.append("\t\tWhen predicate: ").append(rootPredicate.getClass().getSimpleName()).append('\n');
            sb.append("\t\tExecute action: ").append(businessRule.getBusinessAction().getClass().getSimpleName());
        }
        return sb.toString();
    }
}
