package net.sf.solarnetwork.node.dao;

import net.sf.solarnetwork.node.DayDatum;

/**
 * Data access object API for DayDatum objects.
 * 
 * @author matt.magoffin
 * @version $Revision: 108 $ $Date: 2008-08-14 19:17:07 -0400 (Thu, 14 Aug 2008) $
 */
public interface DayDatumDao {

    /**
	 * Persist a new DayDatum instance, or update an existing instance,
	 * and return its primary key.
	 * 
	 * @param datum the datum to store
	 * @return the primary key
	 */
    Long storeDayDatum(DayDatum datum);

    /**
	 * Find a specific DayDatum based on properties that uniquely identify
	 * a DayDatum.
	 * 
	 * <p>The properties that uniquely identify the datum are:</p>
	 * 
	 * <ul>
	 *   <li>day</li>
	 *   <li>tz</li>
	 *   <li>latitude (optional)</li>
	 *   <li>longitude (optional)</li>
	 * </ul>
	 * 
	 * <p>The {@code latitude} and {@code longitude} values are optional, 
	 * and only needed if latitude and longitude values are being captured
	 * and stored for the node capturing this data.</p>
	 * 
	 * <p>If more than one result is found, the first one will be returned.</p>
	 * 
	 * @param criteria a DayDatum with properties specified for the search criteria
	 * @return found DayDatum, or <em>null</em> if none found
	 */
    DayDatum getDayDatumForUniqueness(DayDatum criteria);
}
