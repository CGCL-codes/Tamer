package net.sourceforge.retriever.sample.speed;

import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.sourceforge.retriever.Retriever;
import net.sourceforge.retriever.analyzer.Analyzer;
import net.sourceforge.retriever.executor.ThreadExecutor;
import net.sourceforge.retriever.fetcher.Resource;

public class TestingCrawlingSpeed {

    public static void main(final String[] args) throws Exception {
        final Retriever crawler = new Retriever();
        crawler.setExecutor(new ThreadExecutor(100, 100, 60, TimeUnit.SECONDS, 1000));
        crawler.addSeed(new URL("http://www.yahoo.com/"));
        crawler.addSeed(new URL("http://www.google.com/"));
        crawler.addSeed(new URL("http://www.uol.com/"));
        crawler.setAnalyzer(new Analyzer() {

            private volatile int i = 0;

            private long startTime = System.currentTimeMillis();

            private int crawledResourcesSinceLastCalculation;

            public synchronized void analyze(final Resource resource, final Map<String, Object> additionalInfo) {
                crawledResourcesSinceLastCalculation++;
                if (crawledResourcesSinceLastCalculation >= 200) {
                    final long secondsElapsed = ((System.currentTimeMillis() - startTime) / 1000);
                    final String text = ++i + " " + crawledResourcesSinceLastCalculation / secondsElapsed + " resources/second.";
                    System.out.println(text);
                    crawledResourcesSinceLastCalculation = 0;
                    startTime = System.currentTimeMillis();
                }
            }
        });
        crawler.start();
    }
}
