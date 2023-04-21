package net.ob3d.domainmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ScoreCard implements Serializable {

    private List<Score> scores = new ArrayList<Score>();

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public void addScore(Score score) {
        scores.add(score);
    }
}
