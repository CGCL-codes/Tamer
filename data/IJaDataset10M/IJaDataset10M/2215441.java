package org.sf.jspread.orderfilters;

import org.sf.jspread.objects.*;

public class ShortSellFilter {

    SpreadOrder buyOrder;

    SpreadOrder sellOrder;

    Spread spread;

    SpreadOrder[] spreadOrders = new SpreadOrder[2];

    public SpreadOrder[] validate(SpreadOrder _buyOrder, SpreadOrder _sellOrder) {
        buyOrder = _buyOrder;
        sellOrder = _sellOrder;
        if ((buyOrder == null) || (sellOrder == null)) {
            return null;
        } else {
            spread = SpreadManager.getSpread(sellOrder.getSpreadID());
            if (sellOrder.getSide().equals("5")) {
                if (spread.getSellSideTickDir() == -1) {
                    if (sellOrder.getOrderPx() >= spread.getSellSideLastPx()) {
                        spread.getLog().info("[ShortSellFilter] " + sellOrder.getStock() + " Order Blocked, Stock DownTicking and Order Price " + sellOrder.getOrderPx() + " is >= Last Price " + spread.getSellSideLastPx());
                        sellOrder = null;
                    }
                } else {
                    if (sellOrder.getOrderPx() > spread.getSellSideLastPx()) {
                        spread.getLog().info("[ShortSellFilter] " + sellOrder.getStock() + " Order Blocked, Stock UpTicking and Order Price " + sellOrder.getOrderPx() + " is > Last Price " + spread.getSellSideLastPx());
                        sellOrder = null;
                    }
                }
            }
            spreadOrders[0] = buyOrder;
            spreadOrders[1] = sellOrder;
            return spreadOrders;
        }
    }
}
