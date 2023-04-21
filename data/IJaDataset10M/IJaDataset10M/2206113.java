package net.solarnetwork.central.dras.dao.ibatis;

import net.solarnetwork.central.dras.dao.EventExecutionInfoDao;
import net.solarnetwork.central.dras.domain.EventExecutionInfo;

/**
 * Ibatis implementation of {@link EventExecutionInfoDao}.
 * 
 * @author matt
 * @version $Revision: 1694 $
 */
public class IbatisEventExecutionInfoDao extends DrasIbatisGenericDaoSupport<EventExecutionInfo> implements EventExecutionInfoDao {

    /**
	 * Constructor.
	 */
    public IbatisEventExecutionInfoDao() {
        super(EventExecutionInfo.class);
    }
}
