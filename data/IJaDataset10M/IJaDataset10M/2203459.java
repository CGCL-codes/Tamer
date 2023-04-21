package algorithms.dfbnb.heuristics;

import algorithms.dfbnb.InfNode;

public interface UtilityHeuristicFunction<E> {

    double h(double budget, InfNode<E> node);
}
