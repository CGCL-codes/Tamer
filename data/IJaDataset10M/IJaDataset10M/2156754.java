package org.nexopenframework.core.concurrent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;
import org.nexopenframework.core.TimeoutException;

public class TimeoutExecutorTest {

    final TimeoutExecutor executor = new TimeoutExecutor();

    @Test
    public void executeCallable() {
        final Callable<Date> currentDate = new Callable<Date>() {

            public Date call() throws Exception {
                TimeUnit.MILLISECONDS.sleep(977);
                return new Date();
            }
        };
        final Date date = this.executor.execute(currentDate);
        assertNotNull(date);
    }

    @Test
    public void executeRunnable() {
        final Runnable runnable = new Runnable() {

            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(977);
                } catch (final InterruptedException e) {
                }
            }
        };
        this.executor.execute(runnable);
    }

    @Test(expected = TimeoutException.class)
    public void executeCallableWithTimeout() {
        final Callable<Date> currentDate = new Callable<Date>() {

            public Date call() throws Exception {
                TimeUnit.SECONDS.sleep(5);
                return new Date();
            }
        };
        this.executor.execute(currentDate);
        fail("Unexpected behavior");
    }

    @Before
    public void init() {
        executor.setTimeout(1000);
        executor.setTimeUnit(TimeUnit.MILLISECONDS);
    }
}
