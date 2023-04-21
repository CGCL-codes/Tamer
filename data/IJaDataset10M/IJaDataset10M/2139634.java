package org.sf.jspread.orderfilters;

import org.sf.jspread.objects.*;

public class UniversalFilter {

    SpreadOrder buyOrder;

    SpreadOrder sellOrder;

    Spread spread;

    SpreadOrder[] spreadOrders = new SpreadOrder[2];

    public SpreadOrder[] validate(SpreadOrder _buyOrder, SpreadOrder _sellOrder) {
        buyOrder = _buyOrder;
        sellOrder = _sellOrder;
        if ((buyOrder == null) || (sellOrder == null) || (hasOpenOrders() && !spread.getMode().equals(SpreadManager.SPREAD_MODE_PREEMPTIVE))) {
            return null;
        } else {
            buyOrder.setOrderPx(Math.round(buyOrder.getOrderPx()));
            sellOrder.setOrderPx(Math.round(sellOrder.getOrderPx()));
            spreadOrders[0] = buyOrder;
            spreadOrders[1] = sellOrder;
            return spreadOrders;
        }
    }

    private boolean hasOpenOrders() {
        spread = SpreadManager.getSpread(buyOrder.getSpreadID());
        for (int i = 0; i < spread.getThisSpreadsOrders().size(); i++) {
            SpreadOrder ord = (SpreadOrder) spread.getThisSpreadsOrders().elementAt(i);
            if (ord.getStatus().equals(SpreadManager.ORDER_STATUS_OPEN) || ord.getStatus().equals(SpreadManager.ORDER_STATUS_PFILLED)) {
                return true;
            }
        }
        return false;
    }
}
