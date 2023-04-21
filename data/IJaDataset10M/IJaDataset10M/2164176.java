package maze.commons.examples.auction.common.bidding;

import java.math.BigDecimal;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public interface ActualFundsInfo {

    BigDecimal getBalance();

    BigDecimal getReserved();

    BigDecimal getAvailable();
}
