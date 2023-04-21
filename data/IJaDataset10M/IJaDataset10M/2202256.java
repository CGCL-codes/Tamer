package de.flingelli.scrum.datastructure;

import java.util.Date;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import de.flingelli.scrum.datastructure.Costs;
import de.flingelli.scrum.datastructure.Member;

public class TestCaseCosts {

    private static final String COST_CATEGORY_1 = "cat-1";

    private Costs mCosts = null;

    @Before
    public void init() {
        mCosts = new Costs("Euro");
        mCosts.addCategory(COST_CATEGORY_1, 10.0);
        mCosts.addCategory("cat-2", 20.0);
    }

    @Test
    public void testAddCategories() {
        Assert.assertEquals(2, mCosts.size());
        Assert.assertEquals(COST_CATEGORY_1, mCosts.getKey(0));
        Assert.assertEquals("cat-2", mCosts.getKey(1));
        Assert.assertEquals(10.0, mCosts.getValue(0));
        Assert.assertEquals(20.0, mCosts.getCosts("cat-2"));
    }

    @Test
    public void testDeleteCategory() {
        Assert.assertEquals(2, mCosts.size());
        mCosts.deleteCategory(COST_CATEGORY_1);
        Assert.assertEquals(1, mCosts.size());
        Assert.assertEquals("cat-2", mCosts.getKey(0));
    }

    @Test
    public void testModifyCategory() {
        Assert.assertEquals(10.0, mCosts.getValue(0));
        mCosts.modify(0, 50.0);
        Assert.assertEquals(50.0, mCosts.getValue(0));
        Assert.assertEquals(20.0, mCosts.getValue(1));
        mCosts.modify(1, 100.0);
        Assert.assertEquals(100.0, mCosts.getValue(1));
    }

    @Test
    public void testCurrency() {
        Assert.assertEquals("Euro", mCosts.getCurrency());
        mCosts.setCurrency("$");
        Assert.assertEquals("$", mCosts.getCurrency());
    }

    @Test
    public void testBudget() {
        mCosts.setBudget("10.000");
        Assert.assertEquals(true, mCosts.getBudget().equals("10.000"));
    }

    @Test
    public void testPlaningMap() {
        Member member = new Member("John", "Doe");
        mCosts.addPlaningItem(member, 100.0);
        Assert.assertEquals(100.0, mCosts.getPlaningItem(member), 0.0);
        Assert.assertEquals(1, mCosts.planingSize());
        member = new Member("Anna", "Meise");
        Assert.assertEquals(0.0, mCosts.getPlaningItem(member), 0.0);
        mCosts.addPlaningItem("id", 500.0);
        Assert.assertEquals(500.0, mCosts.getPlaningItem("id"), 0.0);
    }

    @Test
    public void testClone() {
        Costs clone = mCosts.clone();
        Assert.assertEquals(mCosts.getBudget(), clone.getBudget());
        Assert.assertEquals(mCosts.getCurrency(), clone.getCurrency());
        Assert.assertEquals(mCosts.getKey(0), clone.getKey(0));
    }

    @Test
    public void testCalculateCostsWithRemovedCostCategory() {
        Costs costs = mCosts.clone();
        Team team = new Team();
        Member member = new Member("Bob", "Miller");
        member.setCostCategory(COST_CATEGORY_1);
        team.addTeamMember(member);
        Product product = new Product();
        Backlog backlog = new Backlog();
        BacklogItem item = new BacklogItem("Item");
        Task task = new Task("Task");
        task.setEstimation(10.0);
        task.addWorkingUnit(new WorkingUnit(new Date(), 0.0, 10.0));
        task.setMember(member);
        item.addTask(task, "");
        backlog.addBacklogItem(item);
        product.setBacklog(backlog);
        product.setCosts(costs);
        product.setTeam(team);
        Assert.assertEquals(100.0, product.calculateCosts(member), 0.0);
        costs.deleteCategory(COST_CATEGORY_1);
        Assert.assertEquals(0, product.calculateCosts(member), 0.0);
    }
}
