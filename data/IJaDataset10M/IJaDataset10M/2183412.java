package ch.olsen.products.util.options.test;

import org.junit.Test;
import ch.olsen.products.util.options.FxOption;
import ch.olsen.products.util.options.OptionFactory;

public class FxOptionImplTest {

    @Test
    public void testFxOptionCalculations() {
        double underlyingPrice = 1.62;
        double strikePrice = 1.65;
        double timeToExpiration = 0.09;
        double domesticIntRate = 0.0294;
        double foreignIntRate = 0.0327;
        double volatility = 0.18;
        FxOption fxOption = OptionFactory.newFxOption(underlyingPrice, strikePrice, timeToExpiration, domesticIntRate, foreignIntRate, volatility);
        System.out.println("c=" + fxOption.getCallPremium());
        System.out.println("p=" + fxOption.getPutPremium());
    }
}
