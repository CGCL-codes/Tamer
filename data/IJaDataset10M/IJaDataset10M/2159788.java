package dawnland02.model.action.controller;

import dawnland02.model.action.ActionFactory;
import dawnland02.model.action.ActionParam;
import dawnland02.model.entity.EntityInternal;
import dawnland02.model.task.Task;

/**
 * @author Petru Obreja (obrejap@yahoo.com)
 */
public class MoveActionController extends AbstractActionController {

    private Integer adiacentMatrixCellId;

    public MoveActionController(EntityInternal entity, Task task, Integer adiacentMatrixCellId) {
        super(entity, task);
        this.adiacentMatrixCellId = adiacentMatrixCellId;
        init();
    }

    @Override
    public void init() {
        action = ActionFactory.createMoveAction(adiacentMatrixCellId);
        action.setActionController(this);
        initialized = true;
    }

    @Override
    public void act() {
        switch(action.getCurrentStep()) {
            case ACTION_BEFORE_START:
                {
                    break;
                }
            case ACTION_INTERMEDIATE:
                {
                    entity.setCoordinates((Integer) action.getValueForActionParam(ActionParam.MOVE_COORDINATE_X), (Integer) action.getValueForActionParam(ActionParam.MOVE_COORDINATE_Y));
                    break;
                }
            case ACTION_AFTER_END:
                {
                    this.end();
                    break;
                }
        }
    }
}
