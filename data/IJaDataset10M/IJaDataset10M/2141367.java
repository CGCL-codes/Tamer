package org.sf.jspread.orderfilters;

import org.sf.jspread.objects.*;

public class MinQtyFilter {

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
            spread = SpreadManager.getSpread(buyOrder.getSpreadID());
            if ((buyOrder.getOrderQty() == 0) || (spread.getBuySideMinQty() != 0) && (spread.getBuySideMinQty() > buyOrder.getOrderQty())) {
                spread.getLog().info("[MinQtyFilter] Order Blocked, " + buyOrder.getStock() + " Order Qty " + buyOrder.getOrderQty() + " < Min Qty " + spread.getBuySideMinQty());
                buyOrder = null;
            }
            if ((sellOrder.getOrderQty() == 0) || (spread.getSellSideMinQty() != 0) && (spread.getSellSideMinQty() > sellOrder.getOrderQty())) {
                spread.getLog().info("[MinQtyFilter] Order Blocked, " + sellOrder.getStock() + " Order Qty " + sellOrder.getOrderQty() + " < Min Qty " + spread.getSellSideMinQty());
                sellOrder = null;
            }
            spreadOrders[0] = buyOrder;
            spreadOrders[1] = sellOrder;
            return spreadOrders;
        }
    }
}
