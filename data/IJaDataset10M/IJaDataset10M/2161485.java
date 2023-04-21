package com.gapp.king.web.client;

import com.google.gwt.junit.client.GWTTestCase;

public class GappWebTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.gapp.king.web.Gappweb";
    }

    public void testSimple() {
        assertTrue(true);
    }

    public void testStockPrice() {
        String symbol = "XYZ";
        double price = 70.0;
        double change = 2.0;
        double changePercent = 100.0 * change / price;
        StockPrice stp = new StockPrice(symbol, price, change);
        assertNotNull(stp);
        assertEquals(symbol, stp.getSymbol());
        assertEquals(price, stp.getPrice(), 0.001);
        assertEquals(change, stp.getChange(), 0.001);
        assertEquals(changePercent, stp.getChangePercent(), 0.001);
    }
}
