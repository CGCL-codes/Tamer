package net.sourceforge.retriever.sample;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import net.sourceforge.retriever.collector.Collector;
import net.sourceforge.retriever.collector.CollectorEvent;
import net.sourceforge.retriever.collector.CollectorListener;
import net.sourceforge.retriever.collector.Resource;
import net.sourceforge.retriever.scheduler.Scheduler;

/**
 * This sample shows how to schedule Retriver.
 * 
 * We want here to execute Retriever 5 times, with 10 seconds between each execution.
 * We also say that the first execution will delay 30 seconds, counting from the time the sample
 * starts running.
 */
public class SchedulingRetriever {

    public static void main(final String[] args) throws IOException {
        System.out.println("Program started executing on " + new Date(System.currentTimeMillis()));
        final Collector collector = new Collector("http://www.google.com/");
        collector.setMaximumNumberOfResourcesToCollect(10);
        collector.addCollectorListener(new CollectorListener() {

            public void onStart() {
                System.out.println("Retriever started executing on " + new Date(System.currentTimeMillis()));
            }

            public void onCollect(CollectorEvent event) {
                final List<Resource> collectedResources = event.getCollectedResources();
                for (Resource collectedResource : collectedResources) {
                    System.out.println(collectedResource.getURL());
                }
            }

            public void onFinish() {
                System.out.println("Retriever finished executing on " + new Date(System.currentTimeMillis()) + "\n");
            }
        });
        final Scheduler scheduler = new Scheduler(collector);
        scheduler.setIntervalBetweenRepetitions(10000);
        scheduler.setNumberOfRepetitions(5);
        scheduler.startTime(new Date(System.currentTimeMillis() + 30000));
        scheduler.run();
    }
}
