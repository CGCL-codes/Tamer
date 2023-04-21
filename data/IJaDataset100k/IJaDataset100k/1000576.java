package org.jazzteam.gurudev.model.rule;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.jazzteam.gurudev.model.rule.context.Context;
import org.junit.Test;

/**
 * @author zmicer
 * 
 */
public class RuleTest {

    @Test
    public void testSimpleRuleFlow() {
        DummyRuleManager manager = new DummyRuleManager();
        ExtendibleThing thing = new ExtendibleThing();
        ExtensionPoint ep = new ExtensionPoint();
        ep.setType(ExtensionPointType.Skill);
        ep.setContext(new Context(thing));
        thing.setRuleManager(manager);
        thing.setEp(ep);
        final List<ExtensionPointType> ept = new ArrayList<ExtensionPointType>();
        ept.add(ExtensionPointType.Skill);
        final Rule rule = new SkillDummyRule();
        rule.setEpt(ept);
        manager.addRule(rule);
        thing.setInitiator(10);
        Assert.assertEquals(0, thing.getToCheck());
        thing.setInitiator(150);
        Assert.assertEquals(200, thing.getToCheck());
    }
}
