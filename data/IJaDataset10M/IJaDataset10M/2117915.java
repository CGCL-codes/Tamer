package personal.mhc.pardus.model.commodities;

import org.junit.Before;

/**
 * JUnit Test class for ExoticMatter.
 *
 * @author Mark Camp
 */
public class ExoticMatterTest extends CommodityImplTest {

    /**
     * Default Constructor.
     */
    public ExoticMatterTest() {
    }

    /**
     * Setup before each test.
     */
    @Before
    @Override
    public void setUp() {
        buy = 500;
        sell = 400;
        blackMarketBuy = 1000;
        blackMarketSell = 20;
        workerBonus = (float) 0.05;
        name = "Exotic Matter";
        comm = new ExoticMatter();
        comm.setMax(1);
    }
}
