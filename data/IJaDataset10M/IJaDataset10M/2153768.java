package lp.samples;

import lp.order.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: LinkPoint International</p>
 * @author Sergey Chudnovsky
 * @version 1.0
 */
public class FORCED_TICKET extends JLinkPointSample {

    public FORCED_TICKET() {
    }

    protected String getOrderXML() {
        LPOrderPart order = LPOrderFactory.createOrderPart("order");
        LPOrderPart op = LPOrderFactory.createOrderPart();
        op.put("ordertype", "POSTAUTH");
        order.addPart("orderoptions", op);
        op.clear();
        op.put("configfile", configfile);
        order.addPart("merchantinfo", op);
        op.clear();
        op.put("reference_number", "NEW987654");
        order.addPart("transactiondetails", op);
        op.clear();
        op.put("cardnumber", "4111-1111-1111-1111");
        op.put("cardexpmonth", "03");
        op.put("cardexpyear", "05");
        order.addPart("creditcard", op);
        op.clear();
        op.put("chargetotal", "12.99");
        order.addPart("payment", op);
        return order.toXML();
    }
}
