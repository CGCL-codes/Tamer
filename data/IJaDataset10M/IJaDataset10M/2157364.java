package org.paraj.prodcons;

import org.paraj.sync.JobsBarrier;

public class FeedEngine<T> extends StatisticsTracingEngineImpl<T> {

    private JobsBarrier runningProducersBarrier;

    @Override
    public SimpleEngine<T> start() {
        runningProducersBarrier = new JobsBarrier(producers.size());
        return super.start();
    }

    @Override
    protected ProducerThread createProducerThread(Producer<T> p) {
        return new StatisticsTracingProduceThread(p) {

            public void run() {
                runningProducersBarrier.jobStarted();
                try {
                    super.run();
                } finally {
                    runningProducersBarrier.jobFinished();
                }
            }
        };
    }

    public void runUntilAllProcessed() {
        start();
        try {
            runningProducersBarrier.startAndWaitUntilAllJobsFinished();
            stopGracefully();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
