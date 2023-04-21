package com.mycila.inject.schedule;

import com.google.inject.AbstractModule;
import com.google.inject.Stage;
import com.mycila.inject.jsr250.Jsr250;
import com.mycila.inject.jsr250.Jsr250Injector;
import org.junit.Test;
import javax.inject.Singleton;

public class ScheduleTest {

    private static int count;

    @Test
    public void test() throws Exception {
        Jsr250Injector injector = Jsr250.createInjector(Stage.PRODUCTION, new SchedulingModule(), new AbstractModule() {

            @Override
            protected void configure() {
                bind(MyJob.class).in(Singleton.class);
            }
        });
        Thread.sleep(4000);
        injector.destroy();
    }

    @Cron("*/1 * * * * ? *")
    public static final class MyJob implements Runnable {

        @Override
        public void run() {
            count++;
            System.out.println("run: " + count);
            System.out.println("waiting 500ms...");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("interrupted !");
            }
            if (count > 2) {
                throw new RuntimeException("hello world");
            }
        }
    }
}
