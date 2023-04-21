package net.solarnetwork.central.dao.ibatis;

import java.util.Map;
import net.solarnetwork.central.dao.HardwareDao;
import net.solarnetwork.central.domain.EntityMatch;
import net.solarnetwork.central.domain.Hardware;
import net.solarnetwork.central.domain.HardwareFilter;

/**
 * Ibatis implementation of {@link HardwareDao}.
 * 
 * @author matt
 * @version $Revision: 1807 $
 */
public class IbatisHardwareDao extends IbatisFilterableDaoSupport<Hardware, EntityMatch, HardwareFilter> implements HardwareDao {

    /**
	 * Default constructor.
	 */
    public IbatisHardwareDao() {
        super(Hardware.class, EntityMatch.class);
    }

    @Override
    protected void postProcessFilterProperties(HardwareFilter filter, Map<String, Object> sqlProps) {
        StringBuilder fts = new StringBuilder();
        spaceAppend(filter.getName(), fts);
        if (fts.length() > 0) {
            sqlProps.put("fts", fts.toString());
        }
    }
}
