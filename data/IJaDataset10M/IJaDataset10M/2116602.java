package org.paraj.mapreduce.executors;

import org.paraj.mapreduce.Task;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.UUID;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.Binder;
import com.google.inject.Injector;

/**
 * Created by IntelliJ IDEA.
 * User: piotrga
 * Date: Nov 16, 2008
 * Time: 11:00:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class LocalMutithreadedDistributor implements Distributor {

    ScheduledExecutorService exec = Executors.newScheduledThreadPool(5);

    private ResultService resultService;

    public void schedule(Runnable runnable) {
        injector().injectMembers(runnable);
        exec.schedule(new SafeRunnable(runnable), 0, TimeUnit.SECONDS);
    }

    private Injector injector() {
        return Guice.createInjector(new Module() {

            public void configure(Binder binder) {
                binder.bind(ResultService.class).toInstance(resultService);
                binder.bind(Distributor.class).toInstance(LocalMutithreadedDistributor.this);
            }
        });
    }

    public <R> UUID scheduleTask(final UUID parentId, Task<R> task) {
        final UUID uid = UUID.randomUUID();
        resultService.startingTask(parentId, uid);
        schedule(new MultithreadedTaskExecutor.RunTask(parentId, uid, task));
        return uid;
    }

    public void setResultService(ResultService resultService) {
        this.resultService = resultService;
    }

    private static class SafeRunnable implements Runnable {

        private final Runnable runnable;

        public SafeRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        public void run() {
            try {
                runnable.run();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
