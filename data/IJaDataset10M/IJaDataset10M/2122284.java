package com.google.code.bbdsm.service;

import java.util.List;
import java.util.Map;
import blackboard.admin.data.datasource.DataSource;
import blackboard.admin.data.datasource.DataSourceObjectCount;
import blackboard.base.BbList;
import blackboard.data.ValidationException;
import blackboard.persist.PersistenceException;

public interface DataSourceManager {

    /**
	 * Load all available data sources.
	 *
	 * @throws PersistenceException
	 */
    List<DataSource> findAll() throws PersistenceException;

    /**
	 * Return the object counts within each data source.
	 * @return Keys are the Data Sources' batch_uid.
	 * @throws PersistenceException 
	 */
    Map<String, BbList> getObjectCounts() throws PersistenceException;

    /**
	 * Create a new Data Source Key
	 * @param ds
	 * @throws ValidationException 
	 * @throws PersistenceException 
	 */
    void create(com.google.code.bbdsm.domain.DataSource ds) throws PersistenceException, ValidationException;
}
