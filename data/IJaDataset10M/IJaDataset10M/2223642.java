package uk.ac.lkl.migen.system.server.handler.UserSet.ExpresserModel.TaskShortTermModel.AttributeValue;

import uk.ac.lkl.common.util.restlet.EntityMapper;
import uk.ac.lkl.common.util.restlet.server.EntityHandler;
import uk.ac.lkl.common.util.restlet.server.EntityId;
import uk.ac.lkl.migen.system.ai.um.TaskModelAttribute;
import uk.ac.lkl.migen.system.ai.um.TaskModelAttributeValue;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.server.UserSet;
import uk.ac.lkl.migen.system.server.manipulator.TaskShortTermModelTableManipulator;

public class AttributeHandler extends EntityHandler<TaskShortTermModelTableManipulator> {

    public AttributeHandler(TaskShortTermModelTableManipulator manipulator) {
        super(manipulator);
    }

    public TaskModelAttribute getAttribute(EntityId<UserSet> userSetId, EntityId<ExpresserModel> expresserModelId, EntityId<TaskModelAttributeValue> attributeValueId, EntityMapper mapper) {
        return null;
    }

    public void setAttribute(EntityId<UserSet> userSetId, EntityId<ExpresserModel> expresserModelId, EntityId<TaskModelAttributeValue> attributeValueId, TaskModelAttribute attribute, EntityMapper mapper) {
    }
}
