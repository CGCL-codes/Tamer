package com.luzan.common.pool;

import javax.persistence.*;

/**
 * PoolInfoView
 *
 * @author Victor Bondar
 */
@Entity
@NamedNativeQueries(value = { @NamedNativeQuery(hints = { @QueryHint(name = "org.hibernate.readOnly", value = "true") }, name = "VIEW.PoolInfoView.default", query = "select id as id, job_id as jobId, entity_name as entityName, state, " + "strategy_class_name as desStrategyName, deserialization_state as desState, error, " + "error_code as errorCode, lock_time IS NOT NULL as isLocked from poolitem", resultSetMapping = "RS.PoolInfoView") })
@SqlResultSetMapping(name = "RS.PoolInfoView", entities = { @EntityResult(entityClass = PoolInfoView.class, fields = { @FieldResult(name = "id", column = "id"), @FieldResult(name = "jobId", column = "jobId"), @FieldResult(name = "entityName", column = "entityName"), @FieldResult(name = "state", column = "state"), @FieldResult(name = "error", column = "error"), @FieldResult(name = "errorCode", column = "errorCode"), @FieldResult(name = "desStrategyName", column = "desStrategyName"), @FieldResult(name = "desState", column = "desState"), @FieldResult(name = "isLocked", column = "isLocked") }) })
public class PoolInfoView {

    Integer id;

    String jobId;

    String entityName;

    String state;

    String error;

    Integer errorCode;

    String desStrategyName;

    String desState;

    boolean isLocked;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(final String jobId) {
        this.jobId = jobId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(final String entityName) {
        this.entityName = entityName;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(final String error) {
        this.error = error;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(final Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getDesStrategyName() {
        return desStrategyName;
    }

    public void setDesStrategyName(final String desStrategyName) {
        this.desStrategyName = desStrategyName;
    }

    public String getDesState() {
        return desState;
    }

    public void setDesState(final String desState) {
        this.desState = desState;
    }

    public boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(final boolean locked) {
        isLocked = locked;
    }
}
