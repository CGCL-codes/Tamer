package uk.ac.lkl.migen.system.server.handler.UserSet.ExpresserModel.TileDropOccurrence;

import uk.ac.lkl.common.util.restlet.EntityMapper;
import uk.ac.lkl.common.util.restlet.server.EntityHandler;
import uk.ac.lkl.common.util.restlet.server.EntityId;
import uk.ac.lkl.migen.system.ai.um.occurrence.TileDropOccurrence;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.ModelColor;
import uk.ac.lkl.migen.system.server.UserSet;
import uk.ac.lkl.migen.system.server.manipulator.TileDropOccurrenceTableManipulator;

public class ModelColorHandler extends EntityHandler<TileDropOccurrenceTableManipulator> {

    public ModelColorHandler(TileDropOccurrenceTableManipulator manipulator) {
        super(manipulator);
    }

    public ModelColor getModelColor(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<TileDropOccurrence> tileDropId, EntityMapper mapper) {
        return null;
    }

    public void setModelColor(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<TileDropOccurrence> tileDropId, ModelColor modelColor, EntityMapper mapper) {
    }
}
