package org.skeegenin.searKit.hierarchyEditPath;

import java.util.List;
import org.skeegenin.searKit.CollapsibleSearchNodeCompleter;
import org.skeegenin.searKit.CollapsibleSearchNodeExpander;
import org.skeegenin.searKit.CollapsibleTreeSetSearchQueue;
import org.skeegenin.searKit.SearchIterator;
import org.skeegenin.searKit.SearchNodeEstimateComparator;
import org.skeegenin.skuttil.CancellableIterator;

public class HierarchyEditPathSearch<TElement extends IHierarchyElement<TElement>> {

    public HierarchyEditPathSearch(List<TElement> firstElements, List<TElement> secondElements, double depthEditCostPerLevelPerUnit, double orderChangeCostPerMovePerUnit, int maxOrderChangeMoves, double elementMinPercentSame, int repartitionRetreatCount, long maxOptimizationMillis) {
        environment = new SearchEnvironment<TElement>(firstElements, secondElements, depthEditCostPerLevelPerUnit, orderChangeCostPerMovePerUnit, maxOrderChangeMoves, elementMinPercentSame, repartitionRetreatCount, maxOptimizationMillis);
        estimator = new Estimator<TElement>(environment);
    }

    public SearchEnvironment<TElement> getEnvironment() {
        return environment;
    }

    public CancellableIterator<SearchState<TElement>> findPaths(int maxSearchNodeCount) {
        SearchNodeBuilder<TElement> nodeBuilder = new SearchNodeBuilder<TElement>();
        CollapsibleSearchNodeExpander<SearchNode<TElement>, SearchState<TElement>> nodeExpander = new CollapsibleSearchNodeExpander<SearchNode<TElement>, SearchState<TElement>>();
        CollapsibleSearchNodeCompleter<SearchNode<TElement>> nodeCompleter = new CollapsibleSearchNodeCompleter<SearchNode<TElement>>();
        CollapsibleTreeSetSearchQueue<SearchNode<TElement>> queue = new CollapsibleTreeSetSearchQueue<SearchNode<TElement>>(new SearchNodeEstimateComparator<SearchNode<TElement>>(), maxSearchNodeCount);
        queue.addNode(nodeBuilder.createInitialNode(environment));
        environment.setSearchQueue(queue);
        SearchIterator<SearchState<TElement>, SearchNode<TElement>> searchIter = new SearchIterator<SearchState<TElement>, SearchNode<TElement>>(estimator, nodeBuilder, nodeExpander, nodeCompleter, queue);
        searchIter.setEstimateUpdater(estimator);
        searchIter.setAdjuster(environment);
        return searchIter;
    }

    private SearchEnvironment<TElement> environment;

    private Estimator<TElement> estimator;
}
