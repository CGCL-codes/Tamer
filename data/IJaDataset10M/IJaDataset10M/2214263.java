package com.continuent.tungsten.manager.core;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import com.continuent.tungsten.commons.config.TungstenProperties;
import com.continuent.tungsten.commons.utils.ResultFormatter;
import com.continuent.tungsten.manager.exception.ClusterManagerException;
import com.continuent.tungsten.manager.handler.event.EventIdentifier;
import com.continuent.tungsten.manager.jmx.DynamicMBeanExec;

public class RequestStatus implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private ActionResponse response;

    private SuccessCriterion criterion;

    private Exception exception;

    private EventIdentifier eventID;

    private Status status;

    private boolean isMember = true;

    private int successCount = 0;

    private int failCount = 0;

    private boolean async = false;

    public enum Status {

        EXECUTED, FAIL, SUCCESS
    }

    public RequestStatus(EventIdentifier eventID, SuccessCriterion criterion, ActionResponse response) {
        this.eventID = eventID;
        this.criterion = criterion;
        setResponse(response);
    }

    public RequestStatus(EventIdentifier eventID, SuccessCriterion criterion, boolean asyncFlag) {
        this.eventID = eventID;
        this.response = null;
        this.criterion = criterion;
        this.async = asyncFlag;
        this.status = Status.EXECUTED;
    }

    public RequestStatus(Exception exception) {
        this.eventID = null;
        this.response = null;
        this.status = Status.FAIL;
        this.exception = exception;
    }

    public EventIdentifier getEventID() {
        return eventID;
    }

    public void setEventID(EventIdentifier eventID) {
        this.eventID = eventID;
    }

    public boolean isComplete() {
        if (response != null) return response.isComplete();
        return false;
    }

    public ActionResponse getResponse() {
        return response;
    }

    public void setResponse(ActionResponse response) {
        this.response = response;
        if (response != null) {
            for (ClusterManagerResponse mgrResponse : response.getMemberResponses().values()) {
                if (mgrResponse.getStatus() == ClusterManagerResponse.Status.SUCCESS) {
                    this.successCount++;
                } else {
                    this.failCount++;
                }
            }
        }
        if (criterion == SuccessCriterion.ALL_MEMBERS && this.failCount == 0) {
            this.status = Status.SUCCESS;
        } else if (criterion == SuccessCriterion.ANY_MEMBER && this.successCount > 0) {
            this.status = Status.SUCCESS;
        } else if (criterion == SuccessCriterion.NO_MEMBERS) {
            this.status = Status.SUCCESS;
        } else {
            this.status = Status.FAIL;
        }
    }

    public boolean isAsync() {
        return async;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(ClusterManagerException exception) {
        this.exception = exception;
        this.status = Status.FAIL;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setFailed() {
        this.status = Status.FAIL;
    }

    public void setSuccess() {
        this.status = Status.SUCCESS;
    }

    public String toString() {
        return toString(false);
    }

    /**
     * @param forceIgnoreColon If true, "|" is ignored while formatting result.
     *            If false, "|" is used to generate table-like columns.
     */
    public String toString(boolean forceIgnoreColon) {
        StringBuilder builder = new StringBuilder();
        if (getStatus() != Status.SUCCESS) {
            builder.append(String.format("\nCLUSTER_MEMBER(%s)\n", isMember()));
            builder.append(String.format("STATUS(%s)\n", getStatus()));
            if (getException() != null && getEventID() == null) {
                builder.append(String.format("Exception: %s\nMessage: %s", getException().getClass().getSimpleName(), getException().getLocalizedMessage()));
                return (ResultFormatter.formatResult("MANAGER", builder.toString(), true, false));
            }
            if (getResponse() == null && getEventID() != null) {
                return ResultFormatter.formatResult(getEventID().getClusterManagerID().getMemberName(), builder.toString(), true, true);
            }
            for (ClusterManagerResponse response : getResponse().getMemberResponses().values()) {
                if (response.getStatus() == ClusterManagerResponse.Status.SUCCESS) {
                    builder.append(ResultFormatter.formatResult(response.getMemberName(), DynamicMBeanExec.formatResponse(response), true, true));
                } else if (response.getStatus() == ClusterManagerResponse.Status.FAIL_EXCEPTION) {
                    builder.append(ResultFormatter.formatResult(response.getMemberName(), response.getException().toString(), true, true));
                } else if (response.getStatus() == ClusterManagerResponse.Status.FAIL_MEMBER) {
                    setIsMember(false);
                    builder.append(ResultFormatter.formatResult(response.getMemberName(), response.getStatus().toString(), true, true));
                }
            }
            return builder.toString();
        }
        for (ClusterManagerResponse response : getResponse().getMemberResponses().values()) {
            if (response.getStatus() != ClusterManagerResponse.Status.SUCCESS) continue;
            boolean ignoreColon = true;
            if (!forceIgnoreColon) if (response.getResult() != null && response.getResult() instanceof Map || response.getResult() instanceof TungstenProperties) ignoreColon = false;
            builder.append(ResultFormatter.formatResult(response.getMemberName(), DynamicMBeanExec.formatResponse(response.getResult()), ignoreColon, true));
        }
        return builder.toString();
    }

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public SuccessCriterion getCriterion() {
        return criterion;
    }

    public void setCriterion(SuccessCriterion criterion) {
        this.criterion = criterion;
    }

    public Object chooseResponse() throws ClusterManagerException {
        if (status != Status.SUCCESS) {
            throw new ClusterManagerException(String.format("No response is available for failed request %s", this));
        }
        for (ClusterManagerResponse clusterManagerResponse : response.getMemberResponses().values()) {
            if (clusterManagerResponse.getStatus() == ClusterManagerResponse.Status.SUCCESS) {
                return clusterManagerResponse.getResult();
            }
        }
        throw new ClusterManagerException(String.format("No response was found for successful request %s", this));
    }

    /**
     * Processes multiple responses from command "session statistics" to create
     * a unique result aggregating various numbers returned
     * 
     * @param isRouterGatewayProtocol Whether or not to consider responses as
     *            router gateway ones
     * @return a single set of TungstenProperties aggregating numbers received
     *         from all connected components
     * @throws ClusterManagerException if no response was found
     */
    public TungstenProperties aggregateStatisticsResponses(boolean isRouterGatewayProtocol) throws ClusterManagerException {
        if (status != Status.SUCCESS) {
            throw new ClusterManagerException(String.format("No response is available for failed request %s", this));
        }
        TungstenProperties aggregate = new TungstenProperties();
        for (ClusterManagerResponse clusterManagerResponse : response.getMemberResponses().values()) {
            if (clusterManagerResponse.getStatus() == ClusterManagerResponse.Status.SUCCESS) {
                TungstenProperties result = (TungstenProperties) clusterManagerResponse.getResult();
                if (isRouterGatewayProtocol) for (String key : result.keyNames()) aggregate = aggregateStatistics((TungstenProperties) result.getObject(key), aggregate); else aggregate = aggregateStatistics(result, aggregate);
            }
        }
        if (aggregate.size() > 0) return aggregate;
        throw new ClusterManagerException(String.format("No response was found for successful request %s", this));
    }

    public Map<String, Object> allResponses() throws ClusterManagerException {
        if (status != Status.SUCCESS) {
            throw new ClusterManagerException(String.format("No response is available for failed request %s", this));
        }
        TreeMap<String, Object> responses = new TreeMap<String, Object>();
        for (ClusterManagerResponse clusterManagerResponse : response.getMemberResponses().values()) {
            if (clusterManagerResponse.getStatus() == ClusterManagerResponse.Status.SUCCESS) {
                responses.put(clusterManagerResponse.getMemberName(), clusterManagerResponse.getResult());
            }
        }
        return responses;
    }

    public boolean isMember() {
        return isMember;
    }

    public void setIsMember(boolean isMember) {
        this.isMember = isMember;
    }

    /**
     * Runs through all properties of the given newProps argument and adds each
     * value to the aggregate parameter corresponding property. Note that all
     * properties in both parameters must contain double or long values with
     * identical keys
     * 
     * @param newProps new set of properties which values should be added to the
     *            aggregate one - must not be null
     * @param aggregate original properties which values will receive the sum of
     *            itself plus the corresponding newProps value
     * @return the modified aggregate set of properties
     */
    public TungstenProperties aggregateStatistics(TungstenProperties newProps, TungstenProperties aggregate) {
        for (String key : newProps.keyNames()) {
            String value1 = aggregate.getString(key, "0", false);
            String value2 = newProps.getString(key);
            if (value1.contains(".") || value2.contains(".")) {
                Double dbl1 = aggregate.getDouble(key, "0.0", false);
                Double dbl2 = newProps.getDouble(key);
                aggregate.setDouble(key, dbl1 + dbl2);
            } else {
                Long lng1 = aggregate.getLong(key, "0", false);
                Long lng2 = newProps.getLong(key);
                aggregate.setLong(key, lng1 + lng2);
            }
        }
        return aggregate;
    }
}
