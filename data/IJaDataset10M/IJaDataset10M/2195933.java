package net.solarnetwork.central.dao;

import net.solarnetwork.central.domain.EntityMatch;
import net.solarnetwork.central.domain.PriceSource;
import net.solarnetwork.central.domain.SourceLocation;

/**
 * DAO API for PriceSource.
 * 
 * @author matt
 * @version $Revision: 2312 $
 */
public interface PriceSourceDao extends GenericDao<PriceSource, Long>, FilterableDao<EntityMatch, Long, SourceLocation> {

    /**
	 * Find a unique PriceSource for a given name.
	 * 
	 * @param name the PriceSource name
	 * @return the PriceSource, or <em>null</em> if not found
	 */
    PriceSource getPriceSourceForName(String sourceName);
}
