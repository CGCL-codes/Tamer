package lpcforsos.evaluation.results.bipartitioning;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import lpcforsos.evaluation.Configuration;

public class ZeroPointLocator implements IZeroPointLocator {

    private IZeroPointScoringFunction scoringFunction;

    public ZeroPointLocator(IZeroPointScoringFunction scoringFunction) {
        this.scoringFunction = scoringFunction;
    }

    public ZeroPointLocator() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        this((IZeroPointScoringFunction) Class.forName(Configuration.getInstance().zeroPointLocatorScoringFunction).newInstance());
    }

    private static Logger logger = Logger.getLogger(ZeroPointLocator.class.getName());

    static {
        logger.setLevel(Level.FINER);
    }

    public int predictRelevantLabelCount(double[] originalVotes) {
        double[] sortedVotes = originalVotes.clone();
        Arrays.sort(sortedVotes);
        logger.finer("Processing " + Arrays.toString(sortedVotes));
        double bestScore = Double.MAX_VALUE;
        int result = 0;
        for (int i = 0; i < sortedVotes.length; i++) {
            double score = scoringFunction.score(sortedVotes, i);
            if (score < bestScore) {
                bestScore = score;
                result = i;
            }
            logger.finest(i + " scored " + score);
        }
        logger.finer("Best: " + result + " (" + bestScore + ")");
        return result;
    }
}
