package org.jquantlib.pricingengines.results;

import org.jquantlib.instruments.NewInstrument;
import org.jquantlib.pricingengines.PricingEngine;
import org.jquantlib.pricingengines.arguments.Arguments;

/**
 * This class keeps additional Greeks and other {@link Results} calculated by a {@link PricingEngine}
 * <p>
 * In mathematical finance, the Greeks are the quantities representing the market sensitivities of derivatives such as options. Each
 * "Greek" measures a different aspect of the risk in an option position, and corresponds to a parameter on which the value of an
 * instrument or portfolio of financial instruments is dependent. The name is used because the parameters are often denoted by Greek
 * letters.
 * 
 * @note Public fields as this class works pretty much as Data Transfer Objects
 * 
 * @see Greeks
 * @see Results
 * @see NewInstrument
 * @see PricingEngine
 * @see Arguments
 * @see <a href="http://en.wikipedia.org/wiki/Greeks_(finance)">Greeks</a>
 * 
 * @author Richard Gomes
 */
public class MoreGreeks extends Greeks {

    /**
     * Probability that an Option expires <i>in-the-money</i>
     * 
     * @see <a href="http://www.optiontradingpedia.com/in_the_money_options.htm">In The Money Options</a>
     */
    public double itmCashProbability;

    public double deltaForward;

    public double elasticity;

    public double thetaPerDay;

    public double strikeSensitivity;

    public MoreGreeks() {
        super();
    }

    @Override
    public void reset() {
        super.reset();
        itmCashProbability = deltaForward = elasticity = thetaPerDay = strikeSensitivity = Double.NaN;
    }
}
