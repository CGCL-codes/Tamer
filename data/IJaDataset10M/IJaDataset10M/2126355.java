package org.easyrec.store.dao.domain;

import org.easyrec.model.core.RecommendedItemVO;
import org.easyrec.store.dao.BaseRecommendedItemDAO;

/**
 * This interface provides methods to store data into and read <code>RecommendedItem</code> entries from a SAT recommender database.
 * Typed version, uses java enums.
 * <p/>
 * <p><b>Company:&nbsp;</b>
 * SAT, Research Studios Austria</p>
 * <p/>
 * <p><b>Copyright:&nbsp;</b>
 * (c) 2007</p>
 * <p/>
 * <p><b>last modified:</b><br/>
 * $Author: sat-rsa $<br/>
 * $Date: 2012-02-23 10:01:50 -0500 (Thu, 23 Feb 2012) $<br/>
 * $Revision: 140 $</p>
 *
 * @author Roman Cerny
 */
public interface TypedRecommendedItemDAO extends BaseRecommendedItemDAO<RecommendedItemVO<Integer, String>, Integer> {
}
