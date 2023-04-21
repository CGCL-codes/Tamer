package sf.pnr.base;

import junit.framework.Assert;
import junit.framework.TestCase;
import sf.pnr.alg.TranspositionTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 */
public class MateTest extends TestCase {

    public void testAll() throws IOException {
        final List<String> testFiles = new ArrayList<String>();
        testFiles.add("mates.epd");
        final long maxNodeCount = Long.parseLong(System.getProperty("searchTest.maxNodeCount", "1000000000"));
        final MateTask task = new MateTask(maxNodeCount);
        final long startTime = System.currentTimeMillis();
        new EpdProcessor().process(testFiles, task);
        final long endTime = System.currentTimeMillis();
        final long totalNodeCount = task.getTotalNodeCount();
        final long expectedNodeCount = Long.parseLong(System.getProperty("searchTest.expectedNodeCount", "-1"));
        if (expectedNodeCount >= 0) {
            assertTrue("too high total node count: " + totalNodeCount + " vs " + expectedNodeCount, totalNodeCount < expectedNodeCount * 1.1);
            assertTrue("too low total node count: " + totalNodeCount + " vs " + expectedNodeCount, totalNodeCount > expectedNodeCount * 0.9);
        }
        final double processTime = ((double) (endTime - startTime)) / 1000;
        System.out.printf("Solved %d mates, in %.2f seconds. Total node count: %d (%.1f nodes/s), test expected: %d," + " previous best: %d\r\n", task.getTestCount(), processTime, totalNodeCount, ((double) totalNodeCount) / processTime, task.getTotalExpectedNodeCount(), expectedNodeCount);
    }

    private static class MateTask implements EpdProcessorTask {

        private final Engine engine = new Engine();

        private final long maxNodeCount;

        private long totalNodeCount = 0;

        private long testCount = 0;

        private long totalExpectedNodeCount = 0;

        public MateTask(final long maxNodeCount) {
            this.maxNodeCount = maxNodeCount;
        }

        @Override
        public void run(final String fileName, final Board board, final Map<String, String> commands) {
            final int depth;
            if (commands.containsKey("acd")) {
                depth = Integer.parseInt(commands.get("acd"));
            } else {
                depth = Integer.parseInt(commands.get("dm")) * 2;
            }
            final String nodeCountStr = commands.get("acn");
            final int expectedNodeCount = Integer.parseInt(nodeCountStr);
            if (expectedNodeCount > maxNodeCount) {
                return;
            }
            engine.clear();
            long result = engine.search(board, depth + 1, 0);
            final int value = Engine.getValueFromSearchResult(result);
            if (value <= Evaluation.VAL_MATE_THRESHOLD) {
                final String fen = StringUtils.toFen(board);
                System.out.printf("Failed to find mate at the expected depth (%d), retrying with one ply more. FEN: %s\r\n", depth + 1, fen);
                final long engineNodeCount = engine.getNodeCount();
                if (expectedNodeCount * 10 < engineNodeCount) {
                    System.out.println(StringUtils.toFen(board) + ": " + engineNodeCount + " vs " + expectedNodeCount);
                }
                totalNodeCount += engineNodeCount;
                engine.clear();
                result = engine.search(board, depth + 2, 0);
                final int value2 = Engine.getValueFromSearchResult(result);
                Assert.assertTrue(fen + ": " + value2, value2 > Evaluation.VAL_MATE_THRESHOLD);
            }
            final int[] bestLine = engine.getBestLine(board);
            final String engineBestMove = StringUtils.toShort(board, bestLine[0]);
            assertEquals(bestLine[0], Engine.getMoveFromSearchResult(result));
            final String[] bestMoves = commands.get("bm").split("/");
            final String pvFirst = commands.get("pv").split(" ")[0];
            if (!containsString(bestMoves, engineBestMove) && !pvFirst.equals(engineBestMove)) {
                System.out.printf("Failed to find the best move for %s : %s vs %s, pv: %s\r\n", StringUtils.toFen(board), engineBestMove, Arrays.toString(bestMoves), pvFirst);
            }
            final long engineNodeCount = engine.getNodeCount();
            if (expectedNodeCount * 50 < engineNodeCount) {
                System.out.println(StringUtils.toFen(board) + ": " + engineNodeCount + " vs " + expectedNodeCount);
            }
            totalNodeCount += engineNodeCount;
            totalExpectedNodeCount += expectedNodeCount;
            testCount++;
        }

        @Override
        public void completed() {
        }

        private boolean containsString(final String[] strings, final String str) {
            for (String string : strings) {
                if (str.equals(string)) {
                    return true;
                }
            }
            return false;
        }

        public long getTotalNodeCount() {
            return totalNodeCount;
        }

        public long getTotalExpectedNodeCount() {
            return totalExpectedNodeCount;
        }

        public long getTestCount() {
            return testCount;
        }
    }
}
