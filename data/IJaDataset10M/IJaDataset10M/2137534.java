package org.hibernate.search.stat;

/**
 * Statistics SPI for the Search. This is essentially the "statistic collector" API.
 * 
 * @author Hardy Ferentschik
 */
public interface StatisticsImplementor {

    /**
	 * Callback for number of object loaded from the db.
	 * 
	 * @param numberOfObjectsLoaded  Number of objects loaded
	 * @param time time in nanoseconds to load the objects
	 */
    void objectLoadExecuted(long numberOfObjectsLoaded, long time);

    /**
	 * Callback for an executed Lucene search.
	 * 
	 * @param searchString executed query string
	 * @param time time in nanoseconds to execute the search
	 */
    void searchExecuted(String searchString, long time);
}
