package sratworld;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ sratworld.base.TestActor.class, sratworld.base.TestUseCases.class, sratworld.base.TestItemFactory.class, sratworld.base.TestObserver.class, sratworld.base.TestArea.class, sratworld.meta.event.TestAttack.class, sratworld.meta.trade.TestTradeLoader.class, sratworld.meta.event.TestSellEvent.class, sratworld.meta.event.TestBuyEvent.class, sratworld.meta.event.BlackBoxTest.class })
public class TestAll {

    /** This wrapper is only required for running the old JUnit 3.8
   * graphical user interface on new JUnit 4.1 test cases  */
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(TestAll.class);
    }
}
