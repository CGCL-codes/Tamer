package dk.itu.maisun.wamp.runtime.activity.definition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** The abstract class which represents an activity definition. */
public abstract class AbstractActivityDefinition implements IActivityDefinition {

    /** The activity name. */
    protected String activityName;

    /** Conflicting activities. */
    protected Map<String, IActivityDefinition> conflicts;

    /** Activities that depend on this activity. */
    protected Map<String, IActivityDefinition> dependencies;

    /** Dynamic exclusion activities. */
    protected Map<String, IActivityDefinition> excludes;

    /** Dynamic inclusion activities. */
    protected Map<String, IActivityDefinition> includes;

    /** Whether the activity is included initially. */
    protected Boolean initIncluded;

    /** The maximal number of executions. */
    protected Integer maxNumberOfExecutions;

    /** The minimal number of executions. */
    protected Integer minNumberOfExecutions;

    /** Response activities. */
    protected Map<String, IActivityDefinition> responses;

    /** Strong Response activities. */
    protected Map<String, IActivityDefinition> strongResponses;

    /** The workflow schema name. */
    protected String workflowSchemaName;

    /** Instantiate a new abstract activity definition.
	 * @param wfSchemaName the workflow schema name
	 * @param activityName the activity name
	 * @param minNumberOfExecutions the minimal number of executions
	 * @param maxNumberOfExecutions the maximal number of executions
	 * @param initIncluded whether the activity is initially included */
    public AbstractActivityDefinition(String wfSchemaName, String activityName, Integer minNumberOfExecutions, Integer maxNumberOfExecutions, boolean initIncluded) {
        this.workflowSchemaName = wfSchemaName;
        this.activityName = activityName;
        this.minNumberOfExecutions = minNumberOfExecutions;
        this.maxNumberOfExecutions = maxNumberOfExecutions;
        this.initIncluded = initIncluded;
        this.dependencies = new HashMap<String, IActivityDefinition>();
        this.includes = new HashMap<String, IActivityDefinition>();
        this.excludes = new HashMap<String, IActivityDefinition>();
        this.conflicts = new HashMap<String, IActivityDefinition>();
        this.responses = new HashMap<String, IActivityDefinition>();
        this.strongResponses = new HashMap<String, IActivityDefinition>();
    }

    @Override
    public String getActivityName() {
        return this.activityName;
    }

    @Override
    public List<IActivityDefinition> getConflicts() {
        return new ArrayList<IActivityDefinition>(this.conflicts.values());
    }

    @Override
    public List<IActivityDefinition> getDependencies() {
        return new ArrayList<IActivityDefinition>(this.dependencies.values());
    }

    @Override
    public List<IActivityDefinition> getExcludes() {
        return new ArrayList<IActivityDefinition>(this.excludes.values());
    }

    @Override
    public String getExistenceConstraint() {
        String maxNumExec = this.maxNumberOfExecutions == -1 ? "*" : this.maxNumberOfExecutions + "";
        return "[" + this.minNumberOfExecutions + "..." + maxNumExec + "]";
    }

    @Override
    public List<IActivityDefinition> getIncludes() {
        return new ArrayList<IActivityDefinition>(this.includes.values());
    }

    @Override
    public Boolean getInitIncluded() {
        return initIncluded;
    }

    @Override
    public Integer getMaxNumberOfExecutions() {
        return this.maxNumberOfExecutions;
    }

    @Override
    public Integer getMinNumberOfExecutions() {
        return this.minNumberOfExecutions;
    }

    @Override
    public List<IActivityDefinition> getResponses() {
        return new ArrayList<IActivityDefinition>(this.responses.values());
    }

    @Override
    public List<IActivityDefinition> getStrongResponses() {
        return new ArrayList<IActivityDefinition>(this.strongResponses.values());
    }

    @Override
    public String getWorkflowSchemaName() {
        return workflowSchemaName;
    }

    @Override
    public void registerConflict(IActivityDefinition conflict) {
        this.conflicts.put(conflict.getActivityName(), conflict);
    }

    @Override
    public void registerDependency(IActivityDefinition dependency) {
        this.dependencies.put(dependency.getActivityName(), dependency);
    }

    @Override
    public void registerExclude(IActivityDefinition exclude) {
        this.excludes.put(exclude.getActivityName(), exclude);
    }

    @Override
    public void registerInclude(IActivityDefinition include) {
        this.includes.put(include.getActivityName(), include);
    }

    @Override
    public void registerResponse(IActivityDefinition response) {
        this.responses.put(response.getActivityName(), response);
    }

    @Override
    public void registerStrongResponse(IActivityDefinition strongResponse) {
        this.strongResponses.put(strongResponse.getActivityName(), strongResponse);
    }

    @Override
    public void setMaxNumberOfExecutions(Integer maxNumberOfExecutions) {
        this.maxNumberOfExecutions = maxNumberOfExecutions;
    }

    @Override
    public void setMinNumberOfExecutions(Integer minNumberOfExecutions) {
        this.minNumberOfExecutions = minNumberOfExecutions;
    }
}
