package corina.cross;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import corina.Range;
import corina.Sample;
import corina.core.App;
import corina.logging.CorinaLog;

/**
 * <h2>Left to do</h2>
 * <ul>
 * <li>need interface method (like getHighScores())
 * <li>instead of recomputing each time, add all to list, and return only overlap>min when asked to (filter)
 * <li>document me -- gpl header, javadoc; views should watch for when corina.cross.overlap changes (prefsListener), and ask me to recompute
 * <li>use this in CrossdatePrinter, SignificantScoresView; get rid of highScores in Cross; have Crossdate.run() create a TopScores object (including compute())
 * <li>CrossdateWindow sorts the highScores list, but shouldn't (why does it?)
 * <li>do sorting here -- need a sortBy(??) method here; also, getSort(??)?
 * </ul>
 */
public class TopScores {

    private static final Log log = new CorinaLog(TopScores.class);

    private Cross c;

    private List highScores;

    public TopScores(Cross c) {
        this.c = c;
        compute();
    }

    public List getScores() {
        return highScores;
    }

    private void compute() {
        highScores = new ArrayList();
        Sample fixed = c.getFixed();
        Sample moving = c.getMoving();
        Range fixedRange = fixed.range;
        Range movingRange = moving.range.redateEndTo(fixedRange.getStart());
        final int minimumOverlap = c.getOverlap();
        int nr = 0;
        int length = c.getRange().span();
        for (int i = 0; i < length; i++) {
            if (fixedRange.overlap(movingRange) >= minimumOverlap) {
                float score = c.getScore(movingRange.getEnd());
                log.debug(movingRange.getEnd() + ":" + score);
                if (c.isSignificant(score, fixedRange.overlap(movingRange))) {
                    try {
                        nr++;
                        highScores.add(new HighScore(c, i, nr));
                    } catch (Exception e) {
                        log.error("trouble with bayes! -- " + e);
                        e.printStackTrace();
                    }
                }
            }
            movingRange = movingRange.redateBy(+1);
        }
    }
}
