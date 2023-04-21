package org.exist.eclipse.auto.internal.model;

import java.util.ArrayList;

/**
 * This class represents the actual data model. It contains all the actual
 * queries as a collection and know the automation specific parameters.
 * 
 * @author Markus Tanner
 */
public class AutoModel implements IAutoModel {

    private ArrayList<QueryEntity> _queries;

    private int _threadCount;

    /**
	 * @return the queries
	 */
    public ArrayList<QueryEntity> getQueries() {
        return _queries;
    }

    /**
	 * Constructor class
	 */
    public AutoModel() {
        _queries = new ArrayList<QueryEntity>();
    }

    public Object[] getContents() {
        return _queries.toArray();
    }

    public void addQuery(QueryEntity query) {
        _queries.add(query);
    }

    /**
	 * Updates the model
	 */
    public void UpdateModel() {
        for (QueryEntity queryEntity : _queries) {
            queryEntity.setModel(this);
        }
    }

    public void removeQuery(QueryEntity query) {
        _queries.remove(query);
    }

    public void setThreadCount(int threadCount) {
        _threadCount = threadCount;
    }

    public int getThreadCount() {
        return _threadCount;
    }
}
