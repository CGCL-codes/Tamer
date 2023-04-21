package com.jawise.serviceadapter.test.svc.rpc;

import java.util.Map;
import org.apache.log4j.Logger;
import com.jawise.serviceadapter.test.svc.GUID;

public class YComputerPartsSupplier {

    private static final Logger logger = Logger.getLogger(YComputerPartsSupplier.class);

    public String checkHeartBeat(String req) {
        logger.debug("procesing request");
        return "call success";
    }

    public String processPurchaseOrder(String id, String password, String poid, String email, String orderdate, String contact, String contactphone, Object[] items) {
        if ("xcomputerparts".equals(id)) {
            return processPurchaseOrder(poid, email, contact, contactphone, orderdate, items);
        }
        return "Invalid user id";
    }

    public double calculateValueAddedTax(double total, String countrycode) {
        if (total <= 0) {
            throw new IllegalArgumentException("total value must be > 0");
        }
        if (countrycode != null && countrycode.length() > 2) {
            throw new IllegalArgumentException("country code format invalid");
        }
        double tax = total * 0.10;
        if ("TI".equals(countrycode)) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }
        return tax;
    }

    public double calculateValueAddedTax(double total, String countrycode, String catagory) {
        return calculateValueAddedTax(total, countrycode);
    }

    @SuppressWarnings("unchecked")
    private String processPurchaseOrder(String poid, String email, String orderdate, String contactphone, String contact, Object[] oitems) {
        logger.debug("poid - " + poid);
        logger.debug("email - " + email);
        logger.debug("orderdate - " + orderdate);
        logger.debug("contactphone - " + contactphone);
        logger.debug("item size - " + oitems.length);
        for (int i = 0; i < oitems.length; i++) {
            Map item = (Map) oitems[i];
            String no = (String) item.get("no");
            String description = (String) item.get("description");
            int count = (Integer) item.get("count");
            String price = (String) item.get("price");
            logger.debug("item id - " + no);
            logger.debug("item details - " + description);
            logger.debug("item count - " + count);
            logger.debug("item price - " + price);
        }
        return new GUID().toString();
    }
}
