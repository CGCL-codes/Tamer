package nl.utwente.ewi.stream.network.processing;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.utwente.ewi.stream.network.TestHelper;
import nl.utwente.ewi.stream.network.attributes.StreamElement;
import nl.utwente.ewi.stream.network.attributes.bp.SlidingTupleBufferPredicate;
import nl.utwente.ewi.stream.network.attributes.triggers.TupleBasedTrigger;
import nl.utwente.ewi.stream.network.composites.Noop;
import nl.utwente.ewi.stream.network.sources.Dummy;
import nl.utwente.ewi.stream.network.values.IntegerValue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UniqueIdViolationTest {

    private Dummy src = null;

    private Noop noop = null;

    private Noop noop2 = null;

    private Noop noop3 = null;

    private static final int MAX = 99;

    private static Logger logger = Logger.getLogger(UniqueIdViolationTest.class.getName());

    @Before
    public void setUp() {
        try {
            TestHelper.setUp();
            src = new Dummy("test9_dummy", 10);
            TestHelper.sleep(1000);
            noop = new Noop("test9_noop", new TupleBasedTrigger(1), new SlidingTupleBufferPredicate(1), src);
            noop.getTriggerInstance().activate();
            TestHelper.sleep(1000);
            noop2 = new Noop("test9_noop2", new TupleBasedTrigger(1), new SlidingTupleBufferPredicate(1), src);
            noop2.getTriggerInstance().activate();
            noop3 = new Noop("test9_noop3", new TupleBasedTrigger(3), new SlidingTupleBufferPredicate(3), noop2);
            noop3.getTriggerInstance().activate();
            logger.info("setup complete.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "problem seting up test", e);
        }
    }

    @After
    public void tearDown() {
        src = null;
        noop = null;
        noop2 = null;
        noop3 = null;
        TestHelper.tearDown();
    }

    @Test
    public void testUniquIdViolationSingle() {
        int i;
        for (i = 0; i < MAX; i++) {
            try {
                src.read();
                TestHelper.sleep(200);
            } catch (InterruptedException e) {
                logger.info("Interrupted at iteration " + i);
                e.printStackTrace();
            }
        }
        logger.info("reading of data complete.");
        TestHelper.sleep(3000);
        List<StreamElement> parentElements = src.view.getViewData();
        List<StreamElement> noopElements = noop.view.getViewData();
        List<StreamElement> noop2Elements = noop2.view.getViewData();
        List<StreamElement> noop3Elements = noop3.view.getViewData();
        logger.info("data set sizes:   dummy:" + parentElements.size() + "    noop:" + noopElements.size());
        assertEquals(parentElements.size(), MAX);
        assertEquals(noopElements.size(), MAX);
        assertEquals(noop2Elements.size(), MAX);
        assertEquals(noop3Elements.size(), MAX);
        int length = Math.min(parentElements.size(), noopElements.size());
        for (i = 0; i < length; i++) {
            Map map = parentElements.get(i).getData();
            int src_channel = (Integer) ((IntegerValue) map.get("channel")).getValue();
            map = noop3Elements.get(i).getData();
            int noop3_channel = (Integer) ((IntegerValue) map.get("channel")).getValue();
            if (src_channel != noop3_channel) logger.info("deviating channel values at i: " + i);
        }
        assertArrayEquals(parentElements.toArray(new StreamElement[parentElements.size()]), noopElements.toArray(new StreamElement[noopElements.size()]));
        assertArrayEquals(parentElements.toArray(new StreamElement[parentElements.size()]), noop2Elements.toArray(new StreamElement[noop2Elements.size()]));
        assertArrayEquals(parentElements.toArray(new StreamElement[parentElements.size()]), noop3Elements.toArray(new StreamElement[noop3Elements.size()]));
    }
}
