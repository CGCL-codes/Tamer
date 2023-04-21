package com.liferay.portlet.workflow.search;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import javax.portlet.RenderRequest;

/**
 * <a href="TaskDisplayTerms.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class TaskDisplayTerms extends DisplayTerms {

    public static final String INSTANCE_ID = "instanceId";

    public static final String TASK_NAME = "taskName";

    public static final String DEFINITION_NAME = "definitionName";

    public static final String ASSIGNED_TO = "assignedTo";

    public static final String CREATE_DATE_GT = "createDateGT";

    public static final String CREATE_DATE_LT = "createDateLT";

    public static final String START_DATE_GT = "startDateGT";

    public static final String START_DATE_LT = "startDateLT";

    public static final String END_DATE_GT = "endDateGT";

    public static final String END_DATE_LT = "endDateLT";

    public static final String HIDE_ENDED_TASKS = "hideEndedTasks";

    public TaskDisplayTerms(RenderRequest req) {
        super(req);
        instanceId = ParamUtil.getLong(req, INSTANCE_ID);
        taskName = ParamUtil.getString(req, TASK_NAME);
        definitionName = ParamUtil.getString(req, DEFINITION_NAME);
        assignedTo = ParamUtil.getString(req, ASSIGNED_TO);
        hideEndedTasks = ParamUtil.getBoolean(req, HIDE_ENDED_TASKS);
    }

    public long getInstanceId() {
        return instanceId;
    }

    public String getInstanceIdString() {
        if (instanceId != 0) {
            return String.valueOf(instanceId);
        } else {
            return StringPool.BLANK;
        }
    }

    public String getTaskName() {
        return taskName;
    }

    public String getDefinitionName() {
        return definitionName;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public String getCreateDateGT() {
        return createDateGT;
    }

    public String getCreateDateLT() {
        return createDateLT;
    }

    public String getStartDateGT() {
        return startDateGT;
    }

    public String getStartDateLT() {
        return startDateLT;
    }

    public String getEndDateGT() {
        return endDateGT;
    }

    public String getEndDateLT() {
        return endDateLT;
    }

    public boolean isHideEndedTasks() {
        return hideEndedTasks;
    }

    protected long instanceId;

    protected String taskName;

    protected String definitionName;

    protected String assignedTo;

    protected String createDateGT;

    protected String createDateLT;

    protected String startDateGT;

    protected String startDateLT;

    protected String endDateGT;

    protected String endDateLT;

    protected boolean hideEndedTasks;
}
