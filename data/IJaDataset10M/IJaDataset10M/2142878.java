package edu.cmu.ece.agora.pipeline.test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import edu.cmu.ece.agora.pipeline.FilterContext;
import edu.cmu.ece.agora.pipeline.InputStage;
import edu.cmu.ece.agora.pipeline.Pipe;
import edu.cmu.ece.agora.pipeline.SimpleFilter;
import edu.cmu.ece.agora.pipeline.UnidirectionalStage;
import edu.cmu.ece.agora.pipeline.pipes.DirectPipe;
import edu.cmu.ece.agora.pipeline.pipes.OrderedAsyncExecutorPipe;
import edu.cmu.ece.agora.pipeline.pipes.OrderedSyncExecutorPipe;
import edu.cmu.ece.agora.pipeline.pipes.UnorderedAsyncExecutorPipe;

public class SquaringParallelPipelineTest {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        InputStage<Integer> inputStage = new InputStage<Integer>();
        UnidirectionalStage<Integer, Integer> processingStage = new UnidirectionalStage<Integer, Integer>(new OrderedAsyncExecutorPipe<Integer, Integer>(Executors.newCachedThreadPool()), new ProcessingFilter());
        UnidirectionalStage<Integer, Void> outputStage = new UnidirectionalStage<Integer, Void>(new DirectPipe<Integer, Void>(), new OutputFilter());
        inputStage.attach(processingStage);
        processingStage.attach(outputStage);
        for (int i = 0; i < 10; i++) {
            System.out.println("INPUT: " + i);
            inputStage.feedFromExternalSource(null);
        }
    }

    private static class ProcessingFilter extends SimpleFilter<Integer, Integer> {

        @Override
        public void process(FilterContext<Integer> context, Integer input) {
            try {
                Thread.sleep(2000 + (long) ((Math.random() - 0.5) * 500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            context.emit(input * input);
        }
    }

    private static class OutputFilter extends SimpleFilter<Integer, Void> {

        @Override
        public void process(FilterContext<Void> context, Integer input) {
            System.out.println("OUTPUT: " + input);
            System.out.println();
        }
    }
}
