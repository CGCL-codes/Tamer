package cspfj;

import java.util.HashMap;
import java.util.Map;
import cspfj.filter.AC3;
import cspfj.problem.Problem;

public final class DummySolver extends AbstractSolver {

    public DummySolver(final Problem prob) {
        super(prob);
    }

    public Map<String, Integer> nextSolution() {
        try {
            if (!preprocess(new AC3(problem))) {
                return null;
            }
        } catch (InterruptedException e) {
            throw new IllegalArgumentException("Unexpected interruption");
        }
        return new HashMap<String, Integer>();
    }

    public synchronized void collectStatistics() {
    }

    public String getXMLConfig() {
        return "\t\t\t<solver>" + this + "</solver>\n\t\t\t<prepro>" + getPreprocessor() + "</prepro>\n";
    }

    public String toString() {
        return "dummy";
    }

    @Override
    public void reset() {
    }
}
