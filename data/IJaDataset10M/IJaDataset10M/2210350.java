package algorithms.dfbnb.elementOrder;

import java.util.Vector;
import algorithms.dfbnb.InfGroup;
import algorithms.dfbnb.InfNode;

public class HighUtilityPerCostFirst<E> implements ElementOrderingStrategyInterface<E> {

    @Override
    public E findBestCandidate(InfNode<E> node) {
        Vector<E> candidates = node.getCandidates();
        InfGroup<E> group = node.getGroup();
        E bestE = candidates.firstElement();
        double cost = group.getCostOf(bestE);
        if (cost == 0) return bestE;
        double utility = group.getUtilityOf(bestE);
        double maxratio = utility / cost;
        for (E e : candidates) {
            cost = group.getCostOf(e);
            utility = group.getUtilityOf(e);
            if (cost == 0) return e;
            double ratio = utility / cost;
            if ((ratio > maxratio) || ((ratio == maxratio) && (e.hashCode() > bestE.hashCode()))) {
                maxratio = ratio;
                bestE = e;
            }
        }
        return bestE;
    }
}
