package org.jpchart.indicator;

import org.jpchart.market.Market;
import java.math.BigDecimal;

/**
 *
 * @author cfelde
 */
public interface SimpleIndicator {

    /**
     * Returns the indicator value at given market.
     * If no indicator value is available, null is returned
     * 
     * @param market Current market
     * @return SimpleIndicator value or null
     */
    BigDecimal getValue(Market market);
}
