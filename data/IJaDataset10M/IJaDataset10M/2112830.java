package com.mw3d.swt.util.command;

import com.mw3d.core.entity.DynamicEntity;
import com.mw3d.core.entity.data.Trigger;

/**
 * Class command for undo, redo, triggers creation
 * for the entity.
 * @author Tareq Doufish
 * Created on Jul 12, 2005
 */
public class TriggerCreationCommand implements Command {

    private DynamicEntity entity;

    private Trigger trigger;

    public TriggerCreationCommand(DynamicEntity entity, Trigger trigger) {
        this.entity = entity;
        this.trigger = trigger;
    }

    public void redoCommand() {
        entity.getEntityTriggers().addTrigger(trigger);
    }

    public void undoCommand() {
        entity.getEntityTriggers().removeTrigger(trigger);
    }
}
