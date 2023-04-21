package net.sf.yahoocsv.test.logic;

import java.util.List;
import net.sf.yahoocsv.domain.PriceBar;
import net.sf.yahoocsv.logic.PriceLogic;
import net.sf.yahoocsv.test.TestContext;

public class PriceLogicTest extends TestContext {

    private PriceLogic priceLogic;

    public void setUp() {
        priceLogic = (PriceLogic) appCtx.getBean(PriceLogic.class.getName());
    }

    public void tearDown() {
        priceLogic = null;
    }

    public void testGetPriceDao() {
        assertNotNull(priceLogic);
    }

    public void testGetPrice() {
        List<PriceBar> priceBars = priceLogic.getPrices("A");
        assertNotNull(priceBars);
        priceBars = priceLogic.getPrices("A");
        assertNotNull(priceBars);
        for (PriceBar p : priceBars) {
            System.out.println(p.hashCode() + "\t" + p);
        }
    }

    public void testGetPriceDateRange() {
        List<PriceBar> priceBars = priceLogic.getPrices("A");
        assertNotNull(priceBars);
        int middle = priceBars.size() / 2;
        priceBars = priceLogic.getPrices("A", priceBars.get(middle).getDate(), priceBars.get(10).getDate());
        assertNotNull(priceBars);
        priceBars = priceLogic.getPrices("A");
        assertNotNull(priceBars);
        for (PriceBar p : priceBars) {
            System.out.println(p.hashCode() + "\t" + p);
        }
    }
}
