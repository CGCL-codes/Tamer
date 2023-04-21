package ch.olsen.products.util.database.test;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;
import junit.framework.Assert;
import junit.framework.TestCase;
import ch.olsen.products.util.database.BidAsk;
import ch.olsen.products.util.database.Tick;
import ch.olsen.products.util.database.Tick.InstrumentException;
import ch.olsen.products.util.logging.DateFormat;

/**
 * JUnit TestCase for Tick
 *
 * @author loic
 */
public class TickTest extends TestCase {

    String eur = "EUR";

    String usd = "USD";

    String eurDashUsd = "EUR-USD";

    String eurUnderscoreUsd = "EUR_USD";

    double dBid = 1.10;

    double dAsk = 1.11;

    String oandaFxpStamp = "1099389237";

    String oandaFxpFormattedStamp = "2004-11-02 09:53:57";

    String oandaFxpUnformattedLocalTimeStamp = "Tue Nov 02 10:53:57 CET 2004";

    String oandaFxpString = "EUR/USD " + oandaFxpStamp + " 1.10 1.11 UBS2";

    String olsenString = oandaFxpFormattedStamp + " EUR-USD 1.10 1.11";

    public final void testEurUsd(Tick tick) {
        Assert.assertEquals(eur, tick.getPer());
        Assert.assertEquals(usd, tick.getExpr());
    }

    public final void testBidAsk110111(Tick tick) {
        Assert.assertEquals(dBid, tick.getBidAsk().bid);
        Assert.assertEquals(dAsk, tick.getBidAsk().ask);
    }

    public final void testTick() {
        Tick tick = null;
        tick = new Tick();
        Assert.assertNotNull(tick);
        Assert.assertNull(tick.getPer());
        Assert.assertNull(tick.getExpr());
        Assert.assertEquals(tick.getBid(), 0d);
        Assert.assertEquals(tick.getAsk(), 0d);
    }

    public final void testTickStringStringdoubledouble() {
        Tick tick = new Tick("EUR", "USD", dBid, dAsk);
        testEurUsd(tick);
    }

    public final void testParseOanda() {
        try {
            Tick tick = new Tick().parseOanda(oandaFxpString);
            testEurUsd(tick);
            testBidAsk110111(tick);
        } catch (InstrumentException e) {
            Assert.fail("unknown currency " + e.getMessage());
        }
    }

    public final void testParseOlsen() {
        Tick tick = null;
        try {
            tick = new Tick().parseOlsen(olsenString);
        } catch (ParseException e) {
            System.err.println("could not parse tick: " + olsenString);
        }
        testEurUsd(tick);
        testBidAsk110111(tick);
    }

    public final void testParsePerExprString() {
        Tick tick = new Tick().parsePerExpr("EUR-USD");
        testEurUsd(tick);
    }

    public final void testParsePerExprStringdoubledouble() {
        Tick tick = new Tick().parsePerExpr("EUR-USD", dBid, dAsk);
        testEurUsd(tick);
        testBidAsk110111(tick);
    }

    public final void testToString() {
        Tick tick;
        try {
            tick = new Tick().parseOanda(oandaFxpString);
            Assert.assertEquals(tick.toString(), oandaFxpFormattedStamp + " " + eurDashUsd + " 1.1 1.11 from: UBS2");
            tick = new Tick().parsePerExpr(eurDashUsd, dBid, dAsk);
            Assert.assertEquals(tick.toString(), new DateFormat().format(new Date()) + " " + eurDashUsd + " 1.1 1.11");
        } catch (InstrumentException e) {
            Assert.fail("unknown currency " + e.getMessage());
        }
    }

    public final void testGetAsk() {
        Tick tick = new Tick().parsePerExpr(eurDashUsd, dBid, dAsk);
        Assert.assertEquals(dAsk, tick.getAsk());
    }

    public final void testGetBid() {
        Tick tick = new Tick().parsePerExpr(eurDashUsd, dBid, dAsk);
        Assert.assertEquals(dBid, tick.getBid());
    }

    public final void testGetBidAsk() {
        Tick tick = new Tick().parsePerExpr(eurDashUsd, dBid, dAsk);
        @SuppressWarnings("unused") BidAsk ba = tick.getBidAsk();
        Assert.assertTrue(tick.getBidAsk().equals(new BidAsk(dBid, dAsk)));
    }

    public final void testGetPer() {
        Tick tick = new Tick().parsePerExpr(eurDashUsd, dBid, dAsk);
        Assert.assertEquals(eurUnderscoreUsd, tick.getPerExpr());
    }

    public final void testGetExpr() {
        Tick tick = new Tick().parsePerExpr(eurDashUsd, dBid, dAsk);
        Assert.assertEquals(eur, tick.getPer());
    }

    public final void testGetSource() {
        Tick tick = new Tick().parsePerExpr(eurDashUsd, dBid, dAsk);
        Assert.assertEquals(eur, tick.getPer());
    }

    public final void testGetStamp() {
        Tick tick;
        try {
            tick = new Tick().parseOanda(oandaFxpString);
            Assert.assertEquals(oandaFxpUnformattedLocalTimeStamp, tick.getStamp().toString());
            tick = new Tick().parsePerExpr(eurDashUsd, dBid, dAsk);
            Assert.assertEquals(new Date(), tick.getStamp());
        } catch (InstrumentException e) {
            Assert.fail(e.getMessage());
        }
    }

    public final void testGetFormattedStamp() {
        Tick tick;
        try {
            tick = new Tick().parseOanda(oandaFxpString);
            Assert.assertEquals(oandaFxpFormattedStamp, tick.getFormattedStamp());
            tick = new Tick().parsePerExpr(eurDashUsd, dBid, dAsk);
            Assert.assertEquals(new DateFormat().format(new Date()), tick.getFormattedStamp());
        } catch (InstrumentException e) {
            Assert.fail(e.getMessage());
        }
    }

    public final void testGetPerExpr() {
        Tick tick = new Tick();
        Assert.assertEquals("null_null", tick.getPerExpr());
        tick = new Tick().parsePerExpr(eurDashUsd, dBid, dAsk);
        Assert.assertEquals(eurUnderscoreUsd, tick.getPerExpr());
    }

    public final void testGetTimeZone() {
        Assert.assertEquals(Tick.getTimeZone(), TimeZone.getTimeZone("0 : 0"));
    }
}
