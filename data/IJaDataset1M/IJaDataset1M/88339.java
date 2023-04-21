package com.san.stock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import junit.framework.TestCase;

public class StockCallTest extends TestCase {

    private static Log logger = LogFactory.getLog(StockCallTest.class);

    public void testGetStatus() {
        logger.info(StockCall.Status.BUY.toString());
    }
}
