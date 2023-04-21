package com.googlecode.jazure.sdk.task.tracker;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import com.googlecode.jazure.sdk.task.tracker.criteria.BasicCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.CreatedTimeCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.JobCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.ParameterCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.ResultCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.StatusCriteria;
import com.googlecode.jazure.sdk.task.tracker.criteria.TypeCriteria;

public class TaskCondition {

    public static final TaskCondition NO_CONDITION = new TaskCondition(null, null, null, null, null, null, null);

    private List<JobCriteria> jobCriterias = new ArrayList<JobCriteria>();

    private List<StatusCriteria> statusCriterias = new ArrayList<StatusCriteria>();

    private List<TypeCriteria> typeCriterias = new ArrayList<TypeCriteria>();

    private List<CreatedTimeCriteria> createdTimeCriterias = new ArrayList<CreatedTimeCriteria>();

    private List<ParameterCriteria> parameterCriterias = new ArrayList<ParameterCriteria>();

    private List<ResultCriteria> resultCriterias = new ArrayList<ResultCriteria>();

    private final Paginater paginater;

    public TaskCondition(List<JobCriteria> jobCriterias, List<StatusCriteria> statusCriterias, List<TypeCriteria> typeCriterias, List<CreatedTimeCriteria> createdTimeCriterias, List<ParameterCriteria> parameterCriterias, List<ResultCriteria> resultCriterias, Paginater paginater) {
        this.jobCriterias = jobCriterias;
        this.statusCriterias = statusCriterias;
        this.typeCriterias = typeCriterias;
        this.createdTimeCriterias = createdTimeCriterias;
        this.parameterCriterias = parameterCriterias;
        this.resultCriterias = resultCriterias;
        this.paginater = paginater;
    }

    public Paginater getPaginater() {
        return paginater;
    }

    public boolean paginaterSensitive() {
        return paginater != null;
    }

    public List<ParameterCriteria> getParameterCriterias() {
        return parameterCriterias;
    }

    public List<ResultCriteria> getResultCriterias() {
        return resultCriterias;
    }

    public boolean hasParameterCriterias() {
        return CollectionUtils.isNotEmpty(parameterCriterias);
    }

    public boolean hasResultCriterias() {
        return CollectionUtils.isNotEmpty(resultCriterias);
    }

    public List<BasicCriteria> getNotNullBasicCriterias() {
        List<BasicCriteria> allBasicCriterias = new ArrayList<BasicCriteria>();
        addIfNotNull(allBasicCriterias, jobCriterias);
        addIfNotNull(allBasicCriterias, statusCriterias);
        addIfNotNull(allBasicCriterias, typeCriterias);
        addIfNotNull(allBasicCriterias, createdTimeCriterias);
        return allBasicCriterias;
    }

    private void addIfNotNull(List<BasicCriteria> allBasicCriterias, List<? extends BasicCriteria> basicCriterias) {
        if (basicCriterias != null && !basicCriterias.isEmpty()) {
            allBasicCriterias.addAll(basicCriterias);
        }
    }
}
