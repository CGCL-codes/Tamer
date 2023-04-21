package onepoint.project.modules.project;

import onepoint.persistence.OpSiteLocatable;
import onepoint.project.modules.resource.OpResource;

/**
 * @author dfreis
 *
 */
public interface OpAssignmentIfc extends OpBaseEffortIfc, OpSiteLocatable {

    public static final String ASSIGNMENT = "OpAssignment";

    public static final String ASSIGNED = "Assigned";

    public static final String COMPLETE = "Complete";

    public static final String BASE_EFFORT = "BaseEffort";

    public static final String ACTUAL_EFFORT = "ActualEffort";

    public static final String BASE_PROCEEDS = "BaseProceeds";

    public static final String ACTUAL_PROCEEDS = "ActualProceeds";

    public static final String REMAINING_EFFORT = "RemainingEffort";

    public static final String BASE_COSTS = "BaseCosts";

    public static final String ACTUAL_COSTS = "ActualCosts";

    public static final String PROJECT_PLAN = "ProjectPlan";

    public static final String RESOURCE = "Resource";

    public static final String ACTIVITY = "Activity";

    public static final String WORK_RECORDS = "WorkRecords";

    public OpActivityIfc getActivity();

    public abstract void setAssigned(double assigned);

    public abstract double getAssigned();

    public abstract void setResource(OpResource resource);

    public abstract OpResource getResource();

    public abstract boolean isActive();

    public abstract OpAssignment getAssignment();
}
