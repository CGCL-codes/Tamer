package org.lokee.punchcard.config.criteria.preparedstatement;

import junit.framework.TestCase;
import org.lokee.punchcard.config.criteria.AndCriterion;
import org.lokee.punchcard.config.criteria.BetweenCriterion;
import org.lokee.punchcard.config.criteria.CriteriaConfig;
import org.lokee.punchcard.config.criteria.GroupCriterion;
import org.lokee.punchcard.config.criteria.KeyValueCriterion;
import org.lokee.punchcard.config.criteria.handler.preparedstatement.PreparedStatementCriteriaHandler;

public class PreparedStatementCriteriaHandlerTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(PreparedStatementCriteriaHandlerTest.class);
    }

    public void testHandleCriterion() {
        CriteriaConfig config = new CriteriaConfig();
        KeyValueCriterion kv = new KeyValueCriterion();
        kv.setKey("USER_ID");
        kv.setOperator("=");
        kv.setValue("clivens");
        BetweenCriterion bcriterion = new BetweenCriterion();
        bcriterion.setValue1("Jan-06-1999");
        bcriterion.setValue2("Jan-10-1999");
        AndCriterion andcriterion1 = new AndCriterion();
        AndCriterion andcriterion2 = new AndCriterion();
        andcriterion2.addCriterion(kv);
        andcriterion2.addCriterion(bcriterion);
        GroupCriterion gcriterion = new GroupCriterion();
        gcriterion.addCriterion(andcriterion2);
        config.addCriterion(kv);
        config.addCriterion(andcriterion1);
        config.addCriterion(gcriterion);
        PreparedStatementCriteriaHandler handler = new PreparedStatementCriteriaHandler();
        try {
            config.toSQL(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object[] valueObs = handler.getAllValueObjects();
        for (int i = 0; i < valueObs.length; i++) {
        }
    }
}
