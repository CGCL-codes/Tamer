package ch.exm.storm.test;

import org.junit.Test;
import ch.exm.storm.query.QueryPlan;

public class QueryPlanTest extends HibernateQueryTest {

    @Test
    public void testEqual() {
        QueryPlan queryPlan = getTestEqualQuery().getQueryPlan();
        System.out.println(queryPlan);
    }

    @Test
    public void testNot() {
        QueryPlan queryPlan = getTestNotQuery().getQueryPlan();
        System.out.println(queryPlan);
    }

    @Test
    public void testAnd() {
        QueryPlan queryPlan = getTestAndQuery().getQueryPlan();
        System.out.println(queryPlan);
    }

    @Test
    public void testNonRelationalCondition() {
        QueryPlan queryPlan = getTestNonRelationalConditionQuery().getQueryPlan();
        System.out.println(queryPlan);
    }

    @Test
    public void testSelect() {
        QueryPlan queryPlan = getTestSelectQuery().getQueryPlan();
        System.out.println(queryPlan);
    }

    @Test
    public void testSelectSum() {
        QueryPlan queryPlan = getTestSelectSumQuery().getQueryPlan();
        System.out.println(queryPlan);
    }

    @Test
    public void testIn() {
        QueryPlan queryPlan = getTestInQuery().getQueryPlan();
        System.out.println(queryPlan);
    }

    @Test
    public void testBetween() {
        QueryPlan queryPlan = getTestBetweenQuery().getQueryPlan();
        System.out.println(queryPlan);
    }

    @Test
    public void testSelectionOnNonRelationalQuery() {
        QueryPlan queryPlan = getTestSelectionOnNonRelationalQuery().getQueryPlan();
        System.out.println(queryPlan);
    }

    @Test
    public void testAggregateOnNonRelationalQuery() {
        QueryPlan queryPlan = getTestAggregateOnNonRelationalQuery().getQueryPlan();
        System.out.println(queryPlan);
    }
}
