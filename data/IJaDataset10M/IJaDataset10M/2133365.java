package dawnland03.model.task.mock;

import dawnland03.model.action.Action;
import dawnland03.model.action.parameter.ActionInputParam;
import dawnland03.model.action.parameter.ActionOutputParam;
import dawnland03.model.action.ActionType;
import dawnland03.model.action.parameter.OutputParameters;
import dawnland03.model.entity.Entity;
import dawnland03.model.entity.attribute.PhysicalAttribute;
import dawnland03.model.task.BaseTask;
import java.util.Map;
import java.util.HashMap;

/**
 * User: Petru Obreja (obrejap@yahoo.com)
 * Date: Jan 22, 2010
 * Time: 1:22:33 AM
 */
public class ActionMock implements Action {

    private BaseTask parentBaseTask;

    public Entity getOwner() {
        return null;
    }

    public ActionType getActionType() {
        return null;
    }

    public Object getInputParam(ActionInputParam actionInputParam) {
        return null;
    }

    public OutputParameters getOutputParameters() {
        return null;
    }

    public Map<PhysicalAttribute, Integer> getPhysicalAttributeRequirements() {
        return null;
    }

    public Map<PhysicalAttribute, Double> getPhysicalAttributeMultipliers() {
        return null;
    }

    public void setParentBaseTask(BaseTask parentBaseTask) {
        this.parentBaseTask = parentBaseTask;
    }

    public void act() {
        parentBaseTask.signalActionEnd();
    }

    public Long getDuration() {
        return null;
    }
}
