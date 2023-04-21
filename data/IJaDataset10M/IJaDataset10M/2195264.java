package pt.utl.ist.lucene.filter;

import java.util.Map;

/**
 * @author Jorge Machado
 * @date 15/Ago/2008
 * @see pt.utl.ist.lucene.filter
 */
public interface ITimeDistancesWrapper {

    public Map<Integer, Long> getTimeDistances();

    public Long getTimeDistance(int doc);
}
