package org.libreplan.web.planner.milestone;

import org.libreplan.business.planner.entities.TaskElement;
import org.libreplan.web.planner.order.PlanningStateCreator.PlanningState;
import org.zkoss.ganttz.extensions.ICommandOnTask;

/**
 * @author Óscar González Fernández <ogonzalez@igalia.com>
 *
 */
public interface IDeleteMilestoneCommand extends ICommandOnTask<TaskElement> {

    void setState(PlanningState planningState);
}
