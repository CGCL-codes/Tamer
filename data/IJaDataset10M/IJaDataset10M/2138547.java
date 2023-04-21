package net.sourceforge.customercare.server.testing;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;
import junit.framework.TestCase;
import net.sourceforge.customercare.server.entities.EntityLogic;
import net.sourceforge.customercare.server.entities.event.Event;
import net.sourceforge.customercare.server.entities.event.EventCore;
import net.sourceforge.customercare.server.entities.event.EventLogic;
import net.sourceforge.customercare.server.exceptions.CustomerCareException;

/**
 * tests event logic
 */
public class EventLogicTest extends TestCase {

    private Connection cnn;

    private EntityLogic logic;

    public void setUp() throws Exception {
        cnn = TestConnector.getConnection();
        logic = new EventLogic(new EventCore(cnn));
    }

    public void testCreateSaveGetRemove() throws Exception {
        Event evt1 = (Event) logic.create();
        assertEquals(1, evt1.getId().intValue());
        evt1.setTitle("JUnit title");
        evt1.setMemo("JUnit memo");
        evt1.setCllId(10);
        evt1.setUsrId(20);
        evt1.setOpenDate("10.01.2000");
        logic.save(evt1);
        evt1 = (Event) logic.get(evt1.getId());
        assertEquals(1, evt1.getChgctr().intValue());
        assertEquals(1, evt1.getId().intValue());
        assertEquals("JUnit title", evt1.getTitle());
        assertEquals("JUnit memo", evt1.getMemo());
        assertEquals(10, evt1.getCllId().intValue());
        assertEquals(20, evt1.getUsrId().intValue());
        assertEquals("10.01.2000", evt1.getOpenDate());
        evt1.setChgctr(evt1.getChgctr() - 1);
        try {
            logic.save(evt1);
            fail("CustomerCareException should be thrown");
        } catch (CustomerCareException ccEx) {
        }
        evt1 = (Event) logic.get(evt1.getId());
        evt1.setTitle("JUnit title changed");
        evt1.setMemo("JUnit memo changed");
        evt1.setCllId(11);
        evt1.setUsrId(21);
        evt1.setOpenDate("11.01.2000");
        logic.save(evt1);
        evt1 = (Event) logic.get(evt1.getId());
        assertEquals(2, evt1.getChgctr().intValue());
        assertEquals(1, evt1.getId().intValue());
        assertEquals("JUnit title changed", evt1.getTitle());
        assertEquals("JUnit memo changed", evt1.getMemo());
        assertEquals(11, evt1.getCllId().intValue());
        assertEquals(21, evt1.getUsrId().intValue());
        assertEquals("11.01.2000", evt1.getOpenDate());
        try {
            logic.get(2);
            fail("CustomerCareException should be thrown");
        } catch (CustomerCareException ccEx) {
        }
        Event evt2 = (Event) logic.create();
        assertEquals(2, evt2.getId().intValue());
        evt2.setTitle("JUnit 2 title");
        evt2.setMemo("JUnit 2 memo");
        evt2.setCllId(30);
        evt2.setUsrId(40);
        evt2.setOpenDate("20.01.2000");
        logic.save(evt2);
        evt2 = (Event) logic.get(evt2.getId());
        assertEquals(1, evt2.getChgctr().intValue());
        assertEquals(2, evt2.getId().intValue());
        assertEquals("JUnit 2 title", evt2.getTitle());
        assertEquals("JUnit 2 memo", evt2.getMemo());
        assertEquals(30, evt2.getCllId().intValue());
        assertEquals(40, evt2.getUsrId().intValue());
        assertEquals("20.01.2000", evt2.getOpenDate());
        Iterator itr = logic.getAll();
        Event tmp;
        if (itr.hasNext()) {
            tmp = (Event) itr.next();
            assertEquals(1, tmp.getId().intValue());
            assertEquals("JUnit title changed", tmp.getTitle());
            assertEquals("JUnit memo changed", tmp.getMemo());
            assertEquals(11, tmp.getCllId().intValue());
            assertEquals(21, tmp.getUsrId().intValue());
            if (itr.hasNext()) {
                tmp = (Event) itr.next();
                assertEquals(2, tmp.getId().intValue());
                assertEquals("JUnit 2 title", tmp.getTitle());
                assertEquals("JUnit 2 memo", tmp.getMemo());
                assertEquals(30, tmp.getCllId().intValue());
                assertEquals(40, tmp.getUsrId().intValue());
            } else fail("no second entry found");
        } else fail("no entries found");
        logic.remove(evt1.getId());
        try {
            logic.get(evt1.getId());
            fail("CustomerCareException should be thrown");
        } catch (CustomerCareException ccEx) {
        }
        logic.remove(evt2.getId());
        try {
            logic.get(evt2.getId());
            fail("CustomerCareException should be thrown");
        } catch (CustomerCareException ccEx) {
        }
    }

    public void tearDown() throws Exception {
        Statement statement = cnn.createStatement();
        statement.execute("DELETE FROM tbl_event;");
        cnn.close();
    }
}
