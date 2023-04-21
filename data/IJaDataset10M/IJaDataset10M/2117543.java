package drools_persistence_facade;

import org.apache.log4j.Logger;
import org.drools.RuleBase;
import org.drools.RuleBaseConfiguration;
import org.drools.RuleBaseFactory;
import drools_persistence.DroolPersistence;

/**
 * persistent rule base factory, same role (same methods) than
 * {@link RuleBaseFactory} but returns persistent rule base<br>
 * 
 * @author luc peuvrier
 * 
 */
public class PRuleBaseFactory {

    private static final Logger _log = Logger.getLogger(PRuleBaseFactory.class);

    private static final DroolPersistence droolPersistence = DroolPersistence.getInstance();

    public static RuleBase newRuleBase() {
        return persistRuleBase(RuleBaseFactory.newRuleBase());
    }

    public static RuleBase newRuleBase(final String rulebaseId) {
        return persistRuleBase(RuleBaseFactory.newRuleBase(rulebaseId));
    }

    public static RuleBase newRuleBase(final RuleBaseConfiguration config) {
        return persistRuleBase(RuleBaseFactory.newRuleBase(config));
    }

    public static RuleBase newRuleBase(final String rulebaseId, final RuleBaseConfiguration config) {
        return persistRuleBase(RuleBaseFactory.newRuleBase(rulebaseId, config));
    }

    public static RuleBase newRuleBase(final String rulebaseId, final int type) {
        return persistRuleBase(RuleBaseFactory.newRuleBase(rulebaseId, type));
    }

    public static RuleBase newRuleBase(final int type) {
        return persistRuleBase(RuleBaseFactory.newRuleBase(type));
    }

    public static RuleBase newRuleBase(final int type, final RuleBaseConfiguration config) {
        return persistRuleBase(RuleBaseFactory.newRuleBase(type, config));
    }

    public static RuleBase newRuleBase(final String rulebaseId, final int type, final RuleBaseConfiguration config) {
        return persistRuleBase(RuleBaseFactory.newRuleBase(rulebaseId, type, config));
    }

    public static RuleBase exixtingRuleBase(final String rulebaseId) {
        return droolPersistence.getRuleBase(rulebaseId);
    }

    private static RuleBase persistRuleBase(final RuleBase newRuleBase) {
        if (_log.isInfoEnabled()) {
            _log.info("new persistent rule base: " + newRuleBase.getClass());
        }
        droolPersistence.addRuleBase(newRuleBase);
        return newRuleBase;
    }
}
