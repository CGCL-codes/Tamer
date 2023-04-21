package net.krecan.watche.loganalyzer;

import java.io.InputStreamReader;
import org.junit.Test;

public class RealTest {

    @Test
    public void testIt() {
        DurationLogProcessor activeProcessor = new DurationLogProcessor("WindowActivated", "WindowDeactivated");
        DurationLogProcessor runProcessor = new DurationLogProcessor("WatchEStarted", "WatchEStopped");
        LogLoader loader = new LogLoader(new CompositeLogProcessor(activeProcessor, runProcessor));
        loader.loadLog(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("watche20080318123334.log")));
        System.out.println((double) activeProcessor.getTotalDuration() / 1000 / 60);
        System.out.println((double) activeProcessor.getLongestDuration() / 1000 / 60);
        System.out.println((double) runProcessor.getTotalDuration() / 1000 / 60);
        System.out.println((double) runProcessor.getLongestDuration() / 1000 / 60);
    }
}
