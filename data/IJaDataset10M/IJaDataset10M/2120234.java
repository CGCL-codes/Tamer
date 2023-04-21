package quickfix.fix41;

import quickfix.Message;

public class MessageFactory implements quickfix.MessageFactory {

    public Message create(String beginString, String msgType) {
        if ("0".equals(msgType)) {
            return new quickfix.fix41.Heartbeat();
        }
        if ("A".equals(msgType)) {
            return new quickfix.fix41.Logon();
        }
        if ("1".equals(msgType)) {
            return new quickfix.fix41.TestRequest();
        }
        if ("2".equals(msgType)) {
            return new quickfix.fix41.ResendRequest();
        }
        if ("3".equals(msgType)) {
            return new quickfix.fix41.Reject();
        }
        if ("4".equals(msgType)) {
            return new quickfix.fix41.SequenceReset();
        }
        if ("5".equals(msgType)) {
            return new quickfix.fix41.Logout();
        }
        if ("7".equals(msgType)) {
            return new quickfix.fix41.Advertisement();
        }
        if ("6".equals(msgType)) {
            return new quickfix.fix41.IndicationofInterest();
        }
        if ("B".equals(msgType)) {
            return new quickfix.fix41.News();
        }
        if ("C".equals(msgType)) {
            return new quickfix.fix41.Email();
        }
        if ("R".equals(msgType)) {
            return new quickfix.fix41.QuoteRequest();
        }
        if ("S".equals(msgType)) {
            return new quickfix.fix41.Quote();
        }
        if ("D".equals(msgType)) {
            return new quickfix.fix41.NewOrderSingle();
        }
        if ("8".equals(msgType)) {
            return new quickfix.fix41.ExecutionReport();
        }
        if ("Q".equals(msgType)) {
            return new quickfix.fix41.DontKnowTrade();
        }
        if ("G".equals(msgType)) {
            return new quickfix.fix41.OrderCancelReplaceRequest();
        }
        if ("F".equals(msgType)) {
            return new quickfix.fix41.OrderCancelRequest();
        }
        if ("9".equals(msgType)) {
            return new quickfix.fix41.OrderCancelReject();
        }
        if ("H".equals(msgType)) {
            return new quickfix.fix41.OrderStatusRequest();
        }
        if ("J".equals(msgType)) {
            return new quickfix.fix41.Allocation();
        }
        if ("P".equals(msgType)) {
            return new quickfix.fix41.AllocationACK();
        }
        if ("T".equals(msgType)) {
            return new quickfix.fix41.SettlementInstructions();
        }
        if ("E".equals(msgType)) {
            return new quickfix.fix41.NewOrderList();
        }
        if ("N".equals(msgType)) {
            return new quickfix.fix41.ListStatus();
        }
        if ("L".equals(msgType)) {
            return new quickfix.fix41.ListExecute();
        }
        if ("K".equals(msgType)) {
            return new quickfix.fix41.ListCancelRequest();
        }
        if ("M".equals(msgType)) {
            return new quickfix.fix41.ListStatusRequest();
        }
        return new quickfix.fix41.Message();
    }
}
