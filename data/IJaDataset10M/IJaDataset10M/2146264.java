package pt.utl.ist.lucene.sort.sorters.models;

import pt.utl.ist.lucene.filter.ITimeDistancesWrapper;

/**
 * @author Jorge Machado
 * @date 15/Ago/2008
 * @see pt.utl.ist.lucene.sort
 */
public interface TimeDistancesScoreDocComparator extends LgteScoreDocComparator {

    public void addTimeDistancesWrapper(ITimeDistancesWrapper iTimeDistances);

    public ITimeDistancesWrapper getTimeDistancesWrapper();
}
