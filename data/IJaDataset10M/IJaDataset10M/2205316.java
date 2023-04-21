package net.sf.solarnetwork.central.dao;

import java.util.List;
import org.joda.time.ReadableDateTime;
import org.joda.time.ReadableInterval;
import net.sf.solarnetwork.central.domain.DatumQueryCommand;

/**
 * Generic DAO API providing standardized data access methods.
 *
 * @author matt
 * @version $Revision: 415 $ $Date: 2009-11-03 17:59:25 -0500 (Tue, 03 Nov 2009) $
 * @param <T> the domain object type
 */
public interface DatumDao<T> {

    /**
	 * Get the class supported by this Dao.
	 * 
	 * @return class
	 */
    Class<? extends T> getDatumType();

    /**
	 * Get a datum by its primary key.
	 * 
	 * @param id the primary key
	 * @return the datum, or <em>null</em> if not found
	 */
    T getDatum(Long id);

    /**
	 * Store (create or update) a datum and return it's primary key.
	 * 
	 * @param datum the datum to persist
	 * @return the generated primary key
	 */
    Long storeDatum(T datum);

    /**
	 * Get a datum by a node ID and specific date.
	 * 
	 * @param nodeId the node ID
	 * @param date the date
	 * @return the PowerDatum, or <em>null</em> if not found
	 */
    T getDatumForDate(Long nodeId, ReadableDateTime date);

    /**
	 * Query for a list of aggregated datum objects.
	 * 
	 * <p>The returned domain objects are not generally persisted objects,
	 * they represent aggregated results, most likely aggregated over time.</p>
	 * 
	 * @param criteria the query criteria
	 * @return the query results
	 */
    List<T> getAggregatedDatum(DatumQueryCommand criteria);

    /**
	 * Get the interval of available data in the system.
	 * 
	 * @param nodeId the node ID to search for
	 * @return interval, or <em>null</em> if no data available
	 */
    ReadableInterval getReportableInterval(Long nodeId);

    /**
	 * Get the interval of available data in the system, across all nodes.
	 * 
	 * @return interval, or <em>null</em> if no data available
	 */
    ReadableInterval getReportableInterval();
}
