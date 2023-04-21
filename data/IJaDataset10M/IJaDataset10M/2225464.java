package org.easyrec.store.dao;

import org.easyrec.model.core.transfer.TimeConstraintVO;
import org.easyrec.utils.spring.store.dao.TableCreatingDAO;
import java.util.Iterator;

/**
 * This interface provides methods to store data into and read <code>Recommendation</code> entries from an easyrec database.
 * Provides base methods and constants.
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
public interface BaseRecommendationDAO<R> extends TableCreatingDAO {

    public static final String DEFAULT_TABLE_NAME = "recommendation";

    public static final String DEFAULT_ID_COLUMN_NAME = "id";

    public static final String DEFAULT_TENANT_COLUMN_NAME = "tenantId";

    public static final String DEFAULT_USER_COLUMN_NAME = "userId";

    public static final String DEFAULT_QUERIED_ITEM_COLUMN_NAME = "queriedItemId";

    public static final String DEFAULT_QUERIED_ITEM_TYPE_COLUMN_NAME = "queriedItemTypeId";

    public static final String DEFAULT_QUERIED_ASSOC_TYPE_COLUMN_NAME = "queriedAssocTypeId";

    public static final String DEFAULT_RELATED_ACTION_TYPE_COLUMN_NAME = "relatedActionTypeId";

    public static final String DEFAULT_RECOMMENDATION_STRATEGY_COLUMN_NAME = "recommendationStrategy";

    public static final String DEFAULT_EXPLANATION_COLUMN_NAME = "explanation";

    public static final String DEFAULT_RECOMMENDATION_TIME_COLUMN_NAME = "recommendationTime";

    /**
     * returns the tenantId of the <code>recommendation</code> belonging to the given recommendationId
     *
     * @param useCache if set, an internal cache is used, else each call a new query is executed
     */
    public Integer getTenantIdOfRecommendationById(Integer recommendationId, boolean useCache);

    /**
     * inserts a <code>recommendation</code> to the database
     */
    public int insertRecommendation(R recommendation);

    /**
     * returns the <code>recommendation</code> belonging to the given recommendationId
     */
    public R loadRecommendation(Integer recommendationId);

    /**
     * returns an iterator over recommendations, using the given bulk size (to prevent an out of memory error)
     * if the queries resultset is empty, an empty iterator will be returned containing no elements
     */
    public Iterator<R> getRecommendationIterator(int bulkSize);

    /**
     * returns an iterator over recommendations, using the given bulk size (to prevent an out of memory error) and constraints
     * if the queries resultset is empty, an empty iterator will be returned containing no elements
     */
    public Iterator<R> getRecommendationIterator(int bulkSize, TimeConstraintVO timeConstraints);
}
