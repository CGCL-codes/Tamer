package net.sf.eBus.client;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.eBus.util.logging.PatternFormatter;
import net.sf.eBus.messages.EMessage;
import net.sf.eBus.messages.EMessageKey;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author charlesr
 */
public class EReplierTest implements EReplier {

    /**
     * Creates a new EReplierTest instance.
     */
    public EReplierTest() {
    }

    /**
     * Configure logging.
     * @throws Exception
     * if anything goes wrong.
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        final ConsoleHandler handler = new ConsoleHandler();
        Logger parent;
        Logger logger;
        handler.setLevel(Level.OFF);
        handler.setFormatter(new PatternFormatter("%d{MM/dd/yyyy HH:mm:ss.SSS} %m%n"));
        logger = Logger.getLogger(EReplierTest.class.getName());
        parent = logger.getParent();
        parent.addHandler(handler);
        parent.setLevel(Level.OFF);
        logger = Logger.getLogger("net.sf.eBus.messages.LayoutParser");
        logger.setParent(parent);
        logger.setLevel(Level.OFF);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Override
    public void request(final EMessage msg, final EAdvertisement requestAd, final ERequest request) {
    }

    @Override
    public void cancelRequest(final ERequest request) {
    }

    /**
     * Have the replier send an invalid advertisement to eBus.
     */
    @Test
    public void invalidAd() {
        EAdvertisement adId = null;
        Throwable caughtex = null;
        try {
            adId = ERequest.advertise(BAD_KEY, null, this);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNotNull("no exception caught", caughtex);
        assertTrue("wrong exception caught", caughtex instanceof IllegalArgumentException);
        assertNull("invalid adID", adId);
        return;
    }

    /**
     * Have test replier make a valid advertisement.
     */
    @Test
    public void validAd() {
        EAdvertisement adId = null;
        Throwable caughtex = null;
        try {
            adId = ERequest.advertise(GOOD_KEY, null, this);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("invalid adId", adId);
        return;
    }

    @Test
    public void validAdWithCondition() {
        EAdvertisement adId = null;
        Throwable caughtex = null;
        final ECondition condition = new ECondition() {

            @Override
            public boolean test(EMessage msg) {
                return (((NewOrderRequest) msg).exchange.equals("ABC"));
            }
        };
        try {
            adId = ERequest.advertise(GOOD_KEY, condition, this);
        } catch (Exception jex) {
            caughtex = jex;
        }
        assertNull("exception caught", caughtex);
        assertNotNull("invalid adId", adId);
        return;
    }

    static final EMessageKey GOOD_KEY = new EMessageKey(NewOrderRequest.class, ".+");

    static final EMessageKey BAD_KEY = new EMessageKey(OrderReportMessage.class, ".+");
}
