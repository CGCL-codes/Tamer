package org.open18.comparator;

import java.util.Comparator;
import org.open18.model.Score;

public class ScoreComparator implements Comparator<Score> {

    public int compare(Score a, Score b) {
        return Integer.valueOf(a.getHole().getNumber()).compareTo(Integer.valueOf(b.getHole().getNumber()));
    }
}
