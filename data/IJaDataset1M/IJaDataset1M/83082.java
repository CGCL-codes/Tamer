package roboResearch.gui.results;

import roboResearch.engine.*;

/**
 * @author Eric Simonton
 */
public class ScoreListener {

    private final Score score;

    public ScoreListener(Score score) {
        this.score = score;
        score.addListener(this);
    }

    public void scoreChanged(Score source, double oldScore, double newScore) {
    }

    public void sampleAdded(Score source, double score) {
    }

    public void sampleRemoved(Score source, double score) {
    }

    public void sampleChanged(Score source, double oldScore, double newScore) {
    }

    public void dispose() {
        score.removeListener(this);
    }
}
