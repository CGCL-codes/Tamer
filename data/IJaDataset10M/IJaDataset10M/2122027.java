package sample.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import syndicus.domain.Cost;
import syndicus.domain.CostCatalog;
import syndicus.domain.Propertie;
import syndicus.domain.PropertieType;
import syndicus.domain.Residential;
import test.dao.ResisdentialTestDAOimpl;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class CostTest {

    Residential main = new Residential();

    Residential resA = new Residential();

    Residential resB = new Residential();

    Propertie propertie1 = new Propertie();

    Propertie propertie2 = new Propertie();

    CostCatalog catalog1 = new CostCatalog();

    CostCatalog catalog2 = new CostCatalog();

    Cost cost1 = new Cost();

    Cost cost2 = new Cost();

    Cost splittedCost1 = new Cost();

    PropertieType propertieType1 = new PropertieType();

    PropertieType propertieType2 = new PropertieType();

    @Before
    public void init() {
        resA.setMain(main);
        resB.setMain(main);
        catalog1.setYearly(true);
        cost1.setAmount(100);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2009, 1, 1);
        cost1.setInsertDate(calendar.getTime());
        ;
        cost1.setCostCatalog(catalog1);
        cost1.setResidential(main);
        cost2.setAmount(1000);
        cost2.setInsertDate(calendar.getTime());
        cost2.setCostCatalog(catalog2);
        cost2.setResidential(main);
        splittedCost1.setAmount(1000);
        splittedCost1.setInsertDate(calendar.getTime());
        splittedCost1.setCostCatalog(catalog2);
        splittedCost1.setResidential(main);
        splittedCost1.setUseSplitRule(true);
        propertie1.setResidentie(resA);
        propertie1.setThousands(60);
        propertie2.setResidentie(resB);
        propertie2.setThousands(40);
        ResisdentialTestDAOimpl.add(main, resA);
        ResisdentialTestDAOimpl.add(main, resB);
        ResisdentialTestDAOimpl.add(resA, propertie1);
        ResisdentialTestDAOimpl.add(resB, propertie2);
    }

    @Test
    public void testInYear() {
    }

    @Test
    public void testProcentYearlyCosts() {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(0);
        cal1.set(2009, 2, 1);
        Date from = cal1.getTime();
        cost1.setResidential(resA);
        cost1.setInsertDate(from);
        cost1.setAmount(1200);
        cost1.setCostCatalog(catalog1);
        catalog1.setYearly(true);
        ResisdentialTestDAOimpl.costs.add(cost1);
        propertie1.setResidentie(resA);
        Collection<Cost> costs = propertie1.getCosts(2009, resA.getFrom(2009), resA.getTo(2009));
        Assert.assertNotSame(0, costs.size());
        Cost cost = costs.iterator().next();
        Assert.assertTrue(cost.getCostCatalog().isYearly());
        Assert.assertEquals(1200, cost.getAmount().getValue(), .001);
        costs = propertie1.getCosts(catalog1, 2009, 2);
        resA.setStartCalendarYear(0);
        Assert.assertNotSame(0, costs.size());
        cost = costs.iterator().next();
        Assert.assertTrue(cost.getCostCatalog().isYearly());
        Assert.assertEquals(101.78, cost.getAmount().getValue(), .01);
    }

    @Test
    public void testPropertyType() {
    }

    @Test
    public void testSplit() {
        Assert.assertEquals(3, main.getSubResidentials().size());
        Assert.assertEquals(2, main.getAllProperties().size());
        Assert.assertEquals(100, main.getThousands(), 0.0);
        Assert.assertEquals(600, splittedCost1.getAmount(propertie1).getValue(), 0.0001);
        Assert.assertEquals(400, splittedCost1.getAmount(propertie2).getValue(), 0.0001);
    }
}
