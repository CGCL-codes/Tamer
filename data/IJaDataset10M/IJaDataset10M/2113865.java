package com.cred.industries.platform.dao;

import java.util.List;
import com.cred.industries.platform.business.objects.MetricsBO;

public interface IMetricsDAO {

    /**
	 * This function is very free form in that it will create the metric passed in for the db, 
	 * creating new metric types and new metric data value pairs. This is dangerous because the 
	 * metrics are not defined before hand so anyone can program a new metric without 
	 * checking to see if one already exists. As such this class should not be used directly 
	 * but always go though the facade. the facade will enforce this consistency.
	 * By default this will go into the cache to be written in a batch to the DB
	 * @param metric metric to insert into the database. 
	 */
    public abstract void insert(MetricsBO metric);

    /**
	 * Same as insert but you can control if you want to cache it. 
	 * @param metric  metric to insert into the database. 
	 * @param dontCache if you want to prevent it from caching your metric
	 */
    public abstract void insert(MetricsBO metric, boolean dontCache);

    /**
	 * see previous insert for details.
	 * @param connect the DB connection used to insert, assumed to be a session. Caller must manager
	 * the connection should use a transaction because there are inserts
	 * Across multiple tables. To ensure consistency we should use a transaction
	 * @param metric metric to insert into the database. 
	 */
    public abstract void insert(List<MetricsBO> metric);

    /**
	 * gets the Metrics BO matching the metrics instance ID. This is not
	 * useful since you don't usually want to look at individual metrics
	 * but mainly for testing
	 * @param metricInstanceId id of metric to find
	 * @return Metrics BO matching the metrics instance ID
	 */
    public abstract MetricsBO findMetric(int metricInstanceId);
}
