package org.itsocial.framework.persistence.lookup;

import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author raghav Mo
 */
public class ReferenceData {

    private String id;

    private String persistenceUnit;

    private String query;

    private String maxResults;

    private String description;

    private boolean refresh;

    private HashMap<String, Object> parameters = new HashMap<String, Object>();

    private long accessCount;

    private Date refreshedDatetime;

    private String resultType;

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersistenceUnit() {
        return persistenceUnit;
    }

    public void setPersistenceUnit(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Date getRefreshedDatetime() {
        return refreshedDatetime;
    }

    public void setRefreshedDatetime(Date refreshedDatetime) {
        this.refreshedDatetime = refreshedDatetime;
    }

    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, Object> parameter) {
        this.parameters = parameter;
    }

    public void addParameter(String key, Object value) {
        this.parameters.put(key, value);
    }

    public long getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(long accessCount) {
        this.accessCount = accessCount;
    }

    public String getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(String maxResults) {
        this.maxResults = maxResults;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    @Override
    public String toString() {
        return "ReferenceData{" + "id=" + id + ", persistenceUnit=" + persistenceUnit + ", query=" + query + ", maxResults=" + maxResults + ", description=" + description + ", refresh=" + refresh + ", parameters=" + parameters + ", accessCount=" + accessCount + ", refreshedDatetime=" + refreshedDatetime + ", resultType=" + resultType + '}';
    }
}
