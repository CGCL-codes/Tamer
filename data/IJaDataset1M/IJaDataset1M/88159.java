package net.sourceforge.dawnlite.control.subtitles;

import java.util.Vector;
import net.sourceforge.dawnlite.control.tts.TextDurationEstimator;
import net.sourceforge.dawnlite.tools.Tools;

/**
 * @author reiterer
 *
 */
public class TextDurationToSubtitlesConverter {

    TextDurationEstimator estimator;

    /**
	 * @return the estimator
	 */
    public TextDurationEstimator getEstimator() {
        return estimator;
    }

    /**
	 * @param estimator the estimator to set
	 */
    public void setEstimator(TextDurationEstimator estimator) {
        this.estimator = estimator;
    }

    public Subtitles generateSegments(String text, int[] segmentStartIndices) {
        int[] segmentStartTimes = new int[segmentStartIndices.length];
        int[] segmentEndTimes = new int[segmentStartIndices.length];
        segmentStartTimes[0] = 0;
        for (int i = 1; i < segmentStartIndices.length; i++) {
            int millis = (int) estimator.estimate(text, 0, segmentStartIndices[i]);
            segmentStartTimes[i] = millis;
            segmentEndTimes[i - 1] = millis;
        }
        segmentEndTimes[segmentStartIndices.length - 1] = (int) estimator.estimate(text, 0, text.length());
        Subtitles sub = new Subtitles(text, segmentStartIndices, segmentStartTimes, segmentEndTimes, null);
        return sub;
    }

    public Subtitles generateSegmentsForWords(String text) {
        char[] separators = new char[] { ' ', '\n' };
        Vector<Integer> indices = new Vector<Integer>();
        indices.add(new Integer(0));
        int index = -1;
        while (true) {
            index = Tools.indexOfAny(text, separators, index);
            index++;
            if (index >= text.length()) {
                break;
            }
            if (index > 0) {
                indices.add(new Integer(index));
            } else break;
        }
        int[] result = new int[indices.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = indices.elementAt(i).intValue();
        }
        return generateSegments(text, result);
    }
}
