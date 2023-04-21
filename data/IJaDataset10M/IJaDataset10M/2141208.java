package org.easyrec.service;

import org.easyrec.model.core.transfer.TimeConstraintVO;
import java.util.Iterator;
import java.util.List;

/**
 * Base interface for RecommendationHistoryServices, describes methods to retrieve stored recommendations (from the recommender engine).
 * <p/>
 * <p><b>Company:&nbsp;</b>
 * SAT, Research Studios Austria</p>
 * <p/>
 * <p><b>Copyright:&nbsp;</b>
 * (c) 2007</p>
 * <p/>
 * <p><b>last modified:</b><br/>
 * $Author: sat-rsa $<br/>
 * $Date: 2011-08-12 10:46:14 -0400 (Fri, 12 Aug 2011) $<br/>
 * $Revision: 113 $</p>
 *
 * @author Roman Cerny
 */
public interface BaseRecommendationHistoryService<R, RI> {

    public int insertRecommendation(R recommendation);

    public Iterator<R> getRecommendationIterator(int bulkSize);

    public Iterator<R> getRecommendationIterator(int bulkSize, TimeConstraintVO timeConstraints);

    public Iterator<RI> getRecommendedItemIterator(int bulkSize);

    public List<RI> getRecommendedItems(TimeConstraintVO timeConstraints);

    public R loadRecommendation(Integer recommendationId);

    public List<RI> getRecommendedItemsOfRecommendation(Integer recommendationId);
}
