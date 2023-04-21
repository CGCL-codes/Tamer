package org.dllearner.sparqlquerygenerator.operations.nbr;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.dllearner.sparqlquerygenerator.datastructures.QueryTree;
import org.dllearner.sparqlquerygenerator.operations.nbr.strategy.BruteForceNBRStrategy;
import org.dllearner.sparqlquerygenerator.operations.nbr.strategy.NBRStrategy;

/**
 * 
 * @author Lorenz Bühmann
 * @param <N>
 *
 */
public class NBRGeneratorImpl<N> implements NBRGenerator<N> {

    NBRStrategy<N> strategy;

    public NBRGeneratorImpl() {
        this.strategy = new BruteForceNBRStrategy<N>();
    }

    public NBRGeneratorImpl(NBRStrategy<N> strategy) {
        this.strategy = strategy;
    }

    @Override
    public QueryTree<N> getNBR(QueryTree<N> posExampleTree, QueryTree<N> negExampleTree) {
        return strategy.computeNBR(posExampleTree, Collections.singleton(negExampleTree));
    }

    @Override
    public QueryTree<N> getNBR(QueryTree<N> posExampleTree, Set<QueryTree<N>> negExampleTrees) {
        return strategy.computeNBR(posExampleTree, negExampleTrees);
    }

    @Override
    public List<QueryTree<N>> getNBRs(QueryTree<N> posExampleTree, QueryTree<N> negExampleTree) {
        return strategy.computeNBRs(posExampleTree, Collections.singleton(negExampleTree));
    }

    @Override
    public List<QueryTree<N>> getNBRs(QueryTree<N> posExampleTree, Set<QueryTree<N>> negExampleTrees) {
        return strategy.computeNBRs(posExampleTree, negExampleTrees);
    }

    @Override
    public List<QueryTree<N>> getNBRs(QueryTree<N> posExampleTree, QueryTree<N> negExampleTree, int limit) {
        return null;
    }

    @Override
    public List<QueryTree<N>> getNBRs(QueryTree<N> posExampleTree, Set<QueryTree<N>> negExampleTrees, int limit) {
        return null;
    }
}
