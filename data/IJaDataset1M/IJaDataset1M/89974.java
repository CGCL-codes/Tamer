package org.sf.jspread.ordermanager.rv.dtm;

import com.tibco.tibrv.*;
import org.sf.jspread.objects.*;
import org.sf.jspread.ordermanager.rv.*;

public class ChildOrderAmendPublisher {

    Spread spread;

    public ChildOrderAmendPublisher(Spread spread) {
        this.spread = spread;
    }

    public synchronized void publish(SpreadOrder order) {
        order.setStatus(spread.getOrder(order.getOrderID()).getStatus());
        spread.getLog().debug("Attempting to Amend Order " + order.getOrderID());
        if ((order.getApexOrdID() == null) || (order.getStatus().equals(SpreadManager.ORDER_STATUS_PAMEND))) {
            spread.getLog().debug("Order is not yet open in market,Status: " + order.getStatus() + " cannot amend");
        } else if (order.getStatus().equals(SpreadManager.ORDER_STATUS_OPEN) || order.getStatus().equals(SpreadManager.ORDER_STATUS_AMENDED) || order.getStatus().equals(SpreadManager.ORDER_STATUS_AMENDREJECTED) || order.getStatus().equals(SpreadManager.ORDER_STATUS_CANCELREJECTED) || order.getStatus().equals(SpreadManager.ORDER_STATUS_PFILLED)) {
            try {
                spread.getLog().info("Amended PreEmptive Buy Order, Sending to Market");
                order.setOrderTime(SpreadManager.getTimeStamp());
                order.setStatus(SpreadManager.ORDER_STATUS_PAMEND);
                TibrvMsg tibOrder = toTibrvMsg(order);
                tibOrder.update("MsgType", "G");
                tibOrder.update("SecondaryOrderID", order.getApexOrdID());
                tibOrder.add("MsgSeqNum", SpreadManager.getNextMsgSeqNum());
                MarketGateway.DataToMarket(tibOrder);
                spread.removeOrder(order.getOrderID());
                spread.addOrder(order);
                spread.getLog().info("Sent Amend Order " + order.getOrderID());
                spread.getLog().debug("Order: " + tibOrder.toString());
            } catch (Exception e) {
                spread.getLog().error(e.getMessage());
                e.printStackTrace(spread.getLog().getPrinter());
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        } else {
            spread.getLog().debug("Invalid Order State, Status: " + order.getStatus() + " cannot amend");
        }
    }

    private synchronized TibrvMsg toTibrvMsg(SpreadOrder order) {
        TibrvMsg tibOrder = new TibrvMsg();
        try {
            tibOrder.add("GMADataType", "DATA_TO_MARKET");
            tibOrder.add("OrderType", "2");
            tibOrder.add("MarketSecurityID", order.getStock());
            tibOrder.add("ExecInstType", 1);
            tibOrder.add("Price", order.getOrderPx());
            tibOrder.add("TimeInForce", "0");
            tibOrder.add("MsgSeqNum", SpreadManager.getNextMsgSeqNum());
            tibOrder.add("MsgType", "D");
            tibOrder.add("Side", order.getSide());
            tibOrder.add("OrderCapacity", SpreadManager.getSpread(order.getSpreadID()).getTradeAudit());
            tibOrder.add("OrderQty", order.getOrderQty());
            if (order.getSide().equals("1")) {
                tibOrder.add("ExDestination", SpreadManager.getSpread(order.getSpreadID()).getBuySideMarket());
                tibOrder.add("Account", SpreadManager.getSpread(order.getSpreadID()).getBuySideBook());
                tibOrder.add("SecondaryOrderID", SpreadManager.getSpread(order.getSpreadID()).getBuySideApexParentOrder());
            } else {
                tibOrder.add("ExDestination", SpreadManager.getSpread(order.getSpreadID()).getSellSideMarket());
                tibOrder.add("Account", SpreadManager.getSpread(order.getSpreadID()).getSellSideBook());
                tibOrder.add("SecondaryOrderID", SpreadManager.getSpread(order.getSpreadID()).getSellSideApexParentOrder());
            }
            tibOrder.add("ClOrdID", order.getOrderID());
            tibOrder.add("OrderID", order.getOrderID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return tibOrder;
    }
}
