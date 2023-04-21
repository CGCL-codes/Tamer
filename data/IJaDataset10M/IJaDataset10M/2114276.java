package uk.ac.lkl.migen.system.server.handler.UserSet.ExpresserModel.GlobalAllocationChangeOccurrence;

import uk.ac.lkl.common.util.restlet.EntityMapper;
import uk.ac.lkl.common.util.restlet.server.EntityHandler;
import uk.ac.lkl.common.util.restlet.server.EntityId;
import uk.ac.lkl.migen.system.ai.um.occurrence.GlobalAllocationChangeOccurrence;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ModelColor;
import uk.ac.lkl.migen.system.server.UserSet;
import uk.ac.lkl.migen.system.server.manipulator.GlobalAllocationChangeOccurrenceTableManipulator;

public class ColorHandler extends EntityHandler<GlobalAllocationChangeOccurrenceTableManipulator> {

    public ColorHandler(GlobalAllocationChangeOccurrenceTableManipulator manipulator) {
        super(manipulator);
    }

    public ModelColor getColor(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<GlobalAllocationChangeOccurrence> globalAllocationChangeId, EntityMapper mapper) {
        return null;
    }

    public void setColor(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<GlobalAllocationChangeOccurrence> globalAllocationChangeId, ModelColor color, EntityMapper mapper) {
    }
}
