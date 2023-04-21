package net.stickycode.scheduled.single;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.stickycode.scheduled.Schedule;
import net.stickycode.scheduled.ScheduledRunnable;
import net.stickycode.scheduled.ScheduledRunnableRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SchedulingSystemTest {

    public static class ScheduleTestObject implements ScheduledRunnable {

        int counter = 0;

        public void run() {
            counter++;
        }

        @Override
        public Schedule getSchedule() {
            return new Schedule() {

                @Override
                public TimeUnit getUnits() {
                    return TimeUnit.SECONDS;
                }

                @Override
                public long getPeriod() {
                    return 1;
                }

                @Override
                public long getInitialDelay() {
                    return 0;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        }
    }

    @Mock
    ScheduledRunnableRepository repository;

    @InjectMocks
    SingleThreadPoolSchedulingSystem system = new SingleThreadPoolSchedulingSystem();

    @Test
    public void runit() throws InterruptedException {
        ScheduleTestObject runnable = new ScheduleTestObject();
        assertThat(runnable.counter).isEqualTo(0);
        when(repository.iterator()).thenReturn(iterator(runnable));
        assertThat(runnable.counter).isEqualTo(0);
        system.start();
        Thread.sleep(500);
        assertThat(runnable.counter).isEqualTo(1);
        Thread.sleep(1000);
        assertThat(runnable.counter).isEqualTo(2);
        system.stop();
        assertThat(runnable.counter).isEqualTo(2);
    }

    private Iterator<ScheduledRunnable> iterator(ScheduleTestObject runnable) {
        List<ScheduledRunnable> list = new LinkedList<ScheduledRunnable>();
        list.add(runnable);
        return list.iterator();
    }
}
