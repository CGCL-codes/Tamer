package sf.pnr.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 */
public class StalemateTest {

    public static void main(String[] args) throws IOException {
        final List<String> testFiles = new ArrayList<String>();
        testFiles.add("stalemates.epd");
        final StalemateTask task = new StalemateTask(20);
        final long startTime = System.currentTimeMillis();
        new EpdProcessor().process(testFiles, task);
        final long endTime = System.currentTimeMillis();
        final long totalNodeCount = task.getTotalNodeCount();
        final long expectedNodeCount = Long.parseLong(System.getProperty("searchTest.expectedNodeCount", "-1"));
        if (expectedNodeCount >= 0) {
            assert totalNodeCount < expectedNodeCount * 1.1 : "too high total node count: " + totalNodeCount + " vs " + expectedNodeCount;
            assert totalNodeCount > expectedNodeCount * 0.9 : "too low total node count: " + totalNodeCount + " vs " + expectedNodeCount;
        }
        final double processTime = ((double) (endTime - startTime)) / 1000;
        System.out.printf("Solved %d stalemates, in %.2f seconds. Total node count: %d (%.1f nodes/s),  previous best: %d\r\n", task.getTestCount(), processTime, totalNodeCount, ((double) totalNodeCount) / processTime, expectedNodeCount);
    }

    private static class StalemateTask extends SearchTask {

        public StalemateTask(final int timeToSolve) {
            super(timeToSolve, true, 2000);
        }

        @Override
        protected boolean additionalChecks(final Engine engine, final Board board, final long result, final Map<String, String> commands) {
            final int value = Engine.getValueFromSearchResult(result);
            return (value < Evaluation.VAL_PAWN * 0.2) && (value > -Evaluation.VAL_PAWN * 0.2);
        }
    }
}
