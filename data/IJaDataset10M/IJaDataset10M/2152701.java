package maze.commons.examples.auction.common.bidding;

import java.math.BigDecimal;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public interface LotEventsListener {

    void notifyLotRemoved(int lotUniqNum);

    void notifySell(int lotUniqNum, BigDecimal price);
}
