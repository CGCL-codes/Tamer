package net.sourceforge.srr.testutilities.testapplication.ruleset;

import net.sourceforge.srr.rule.assertion.AssertionComponentHandlerI;
import net.sourceforge.srr.rule.persistence.digester.RuleSetFactoryBase;
import net.sourceforge.srr.rule.ruleset.RuleSet;
import org.xml.sax.Attributes;

public class RuleSetFactoryDPV extends RuleSetFactoryBase {

    @Override
    public Object createObject(Attributes saxAttribs) throws Exception {
        RuleSet ruleSet = getRuleSetWithAttributes(saxAttribs);
        ruleSet.setAssertionComponentHandler(assertionComponentHandler);
        return ruleSet;
    }

    private AssertionComponentHandlerI assertionComponentHandler = null;

    public void setAssertionComponentHandler(AssertionComponentHandlerI handler) {
        this.assertionComponentHandler = handler;
    }
}
