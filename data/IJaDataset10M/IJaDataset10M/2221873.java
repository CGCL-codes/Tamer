package org.olga.rebus.tests;

import org.olga.rebus.model.AbstractRelation;
import org.olga.rebus.model.Rebus;
import org.olga.rebus.model.RebusFactory;
import org.olga.test.ITest;

public class Test33 implements ITest {

    public String name() {
        return "33. Method PowerRelation.getThird()";
    }

    public boolean run() {
        AbstractRelation relation = RebusFactory.instance().createRelation(Rebus.POWER_RELATION, null, null, null);
        int n = relation.getThird(AbstractRelation.RESULT, 2, 3);
        return (n == 8);
    }
}
