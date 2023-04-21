package net.taylor.workflow.service.timer;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import net.taylor.event.entity.Event;
import net.taylor.ioc.Inject;
import net.taylor.util.DateUtil;
import net.taylor.util.StringUtil;
import net.taylor.workflow.entity.TimerInfo;
import net.taylor.workflow.service.timer.TimerService;

/**
 * Test Client
 */
public class TimerTest extends TestCase {

    @Inject
    private static TimerService service;

    /**
	 * Set up the test case
	 */
    protected void setUp() throws Exception {
    }

    protected void validate(Collection list) {
        assertNotNull(list);
        assertTrue(list.size() > 0);
        Iterator i = list.iterator();
        while (i.hasNext()) {
            System.out.println("validate:" + StringUtil.xmlEncode(i.next()));
        }
    }

    public void testInitiate() throws Exception {
        TimerInfo t = createTimerInfo(DateUtil.addSeconds(5));
        System.out.println(t + ":" + t.getId());
        assertNotNull(t);
        t = service.initiate(t);
        System.out.println(t + ":" + t.getId());
        assertNotNull(t);
        assertEquals(TimerInfo.Status.INITIALIZED, t.getStatus());
        assertNotNull(t.getInitializationDate());
        List<TimerInfo> list = service.query(TimerInfo.Status.INITIALIZED);
        System.out.println("query:");
        validate(list);
        assertEquals(TimerInfo.Status.INITIALIZED, list.get(0).getStatus());
    }

    public void testCancel() throws Exception {
        TimerInfo t = createTimerInfo(DateUtil.addMinutes(5));
        t = service.initiate(t);
        t = service.cancel(t);
        System.out.println(t + ":" + t.getId());
        assertNotNull(t);
        assertEquals(TimerInfo.Status.CANCELLED, t.getStatus());
    }

    protected TimerInfo createTimerInfo(Date time) {
        TimerInfo t = new TimerInfo();
        t.setDescription("A Test TimerInfo");
        t.setName("Test Timer");
        t.setTime(time);
        t.setTrigger(new Event());
        return t;
    }
}
