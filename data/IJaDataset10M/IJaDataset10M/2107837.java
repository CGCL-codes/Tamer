package net.sf.xworkquartz.lifecycle;

import java.util.Iterator;
import net.sf.xworkquartz.AbstractTest;
import net.sf.xworkquartz.dispatcher.impl.NameQuartzDispatcher;
import net.sf.xworkquartz.util.CountDownJobListener;
import net.sf.xworkquartz.util.SimpleAction;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import EDU.oswego.cs.dl.util.concurrent.CountDown;

/**
 *
 * @author <a href="mailto:stephan&#64;chaquotay.net">Stephan Mueller</a>
 * @version $Id: ExecutionLifecycleTest.java,v 1.1 2004/06/21 22:40:36 stephanmueller Exp $
 */
public class ExecutionLifecycleTest extends AbstractTest {

    private CountDown countdown;

    public void setUp() throws Exception {
        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.addGlobalJobListener(new ExecutionLifecycleListener());
        scheduler.start();
        countdown = new CountDown(2);
        scheduler.addGlobalJobListener(new CountDownJobListener(countdown));
        JobDetail detail = new JobDetail("test", "quartz", NameQuartzDispatcher.class);
        scheduler.scheduleJob(detail, trigger);
        SimpleAction.instances.clear();
    }

    public void tearDown() throws SchedulerException {
        if (!scheduler.isShutdown()) {
            scheduler.shutdown(true);
        }
    }

    public void testNotSameComponents() throws SchedulerException, InterruptedException {
        countdown.attempt(5000);
        assertEquals(2, SimpleAction.instances.size());
        Iterator iter = SimpleAction.instances.iterator();
        SimpleAction sa1 = (SimpleAction) iter.next();
        SimpleAction sa2 = (SimpleAction) iter.next();
        assertNotNull(sa1.executionComponent);
        assertNotSame(sa1.executionComponent, sa2.executionComponent);
        assertTrue(sa1.executionComponent.isInitialized);
        assertTrue(sa1.executionComponent.isDisposed);
    }
}
