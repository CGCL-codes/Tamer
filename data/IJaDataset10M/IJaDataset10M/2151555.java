package org.sf.jspread.ordermanager.rv.dfm;

import java.util.*;
import com.tibco.tibrv.*;
import org.sf.jspread.objects.*;

public class CancelAckHandler implements DFMHandlerInterface {

    Spread spread;

    public CancelAckHandler(Spread spread) {
        this.spread = spread;
    }

    public void handle(TibrvMsg _msg) {
        try {
            String OrderID = (String) _msg.get("OrderID-0").toString();
            StringTokenizer stTkz = new StringTokenizer(OrderID, "-");
            String SpreadIDPart = stTkz.nextToken();
            String OrderIDPart = stTkz.nextToken();
            SpreadOrder order = spread.getOrder(OrderID);
            if (order.getStock().equals(spread.getBuySideStock())) {
                spread.BUY_REJECT_COUNT = 0;
                spread.getBaikaiDataHolder().our_buy_order_qty = 0;
                spread.getBaikaiDataHolder().our_buy_order_px = 0;
                if (SpreadManager.getSelectedSpread() == spread) {
                    spread.getBaikaiDataHolder().paintBaikai();
                }
            } else {
                spread.SELL_REJECT_COUNT = 0;
                spread.getBaikaiDataHolder().our_sell_order_qty = 0;
                spread.getBaikaiDataHolder().our_sell_order_px = 0;
                if (SpreadManager.getSelectedSpread() == spread) {
                    spread.getBaikaiDataHolder().paintBaikai();
                }
            }
            order.setStatus(SpreadManager.ORDER_STATUS_CANCELED);
            spread.getLog().info("OrderID " + OrderID + " ,Canceled in Market");
            if (spread.getOpenBuyOrder().length() < 5 && spread.getOpenSellOrder().length() < 5 && spread.getStatus().equals(SpreadManager.SPREAD_STATUS_PAUSING)) {
                spread.setStatus(SpreadManager.SPREAD_STATUS_PAUSED);
            }
            if (SpreadManager.getSelectedSpread().equals(spread)) {
                spread.getOTableModel().setOrders(spread.getThisSpreadsOrders());
            }
            spread.getSTableModel().updateSpread(spread);
            spread.checkSpread();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
