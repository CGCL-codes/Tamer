package uima.taes.interestingness;

import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;
import uima.taes.interestingness.graphHelpers.SimpleDirectedGraphHelper;
import edu.uci.ics.jung.algorithms.importance.PageRankWithPriors;
import edu.uci.ics.jung.graph.impl.DirectedSparseGraph;

public class DirectedGraphRank extends GraphRank {

    protected void initializeDataStructures() {
        helper = new SimpleDirectedGraphHelper();
    }

    @Override
    protected void updateRankingScores() {
        Vector<Count> sortedPriors = new Vector<Count>(priorCandidates.values());
        Collections.sort(sortedPriors);
        HashSet priors = new HashSet();
        for (int i = sortedPriors.size() - 1; i >= Math.max(0, sortedPriors.size() - numberPriors); i--) priors.add(sortedPriors.get(i).v);
        ranker = new PageRankWithPriors((DirectedSparseGraph) helper.getGraph(), beta, priors, null);
        ranker.evaluate();
    }
}
