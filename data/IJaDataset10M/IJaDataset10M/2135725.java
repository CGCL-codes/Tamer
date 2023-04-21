package org.databene.contiperf;

/**
 * Executes the actual performance tests using an {@link ArgumentsProvider} 
 * to create arguments and an {@link Invoker} to call the target code.<br/><br/>
 * Created: 22.10.2009 06:30:28
 * @since 1.0
 * @author Volker Bergmann
 */
public class PerfTestRunner {

    private ExecutionConfig config;

    private ArgumentsProvider argsProvider;

    private PerformanceTracker tracker;

    public PerfTestRunner(ExecutionConfig config, PerformanceTracker tracker, ArgumentsProvider argsProvider) {
        this.config = config;
        this.tracker = tracker;
        this.argsProvider = argsProvider;
    }

    public void run() throws Exception {
        int duration = config.getDuration();
        if (duration >= 0) runWithDuration(duration); else runWithCount(config.getInvocations());
    }

    private void runWithDuration(int duration) throws Exception {
        long start = System.currentTimeMillis();
        do {
            tracker.invoke(argsProvider.next());
        } while ((int) (System.currentTimeMillis() - start) < duration);
    }

    private void runWithCount(int invocations) throws Exception {
        for (int i = 0; i < invocations; i++) tracker.invoke(argsProvider.next());
    }
}
