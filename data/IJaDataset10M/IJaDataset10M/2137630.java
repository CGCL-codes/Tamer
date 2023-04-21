package de.skeptix.evomusic.ea.evaluation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import de.skeptix.evomusic.ea.song.Song;

/**
 * @author skchang
 *
 * Class that evaluates a Song object by delegating it to its IEvaluationFunction objects and returns the
 * sum of the achieved fitness scores.
 */
public class CompositeEvaluationFunction implements IEvaluationFunction {

    private Collection evaluationFunctions;

    public CompositeEvaluationFunction() {
        evaluationFunctions = new LinkedList();
    }

    public double evaluateSong(Song song) {
        double score = 0.0d;
        Iterator iterator = evaluationFunctions.iterator();
        while (iterator.hasNext()) score += ((IEvaluationFunction) iterator.next()).evaluateSong(song);
        return score;
    }

    public void addEvaluationFunction(IEvaluationFunction evaluationFunction) {
        evaluationFunctions.add(evaluationFunction);
    }
}
