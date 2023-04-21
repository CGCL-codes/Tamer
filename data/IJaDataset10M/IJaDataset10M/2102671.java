package uk.ac.lkl.migen.system.server.handler.UserSet.ExpresserModel.BlockShape;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import uk.ac.lkl.common.util.restlet.EntityFilter;
import uk.ac.lkl.common.util.restlet.EntityMapper;
import uk.ac.lkl.common.util.restlet.RestletException;
import uk.ac.lkl.common.util.restlet.server.EntityHandler;
import uk.ac.lkl.common.util.restlet.server.EntityId;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.migen.system.expresser.model.ColorAllocationAttribute;
import uk.ac.lkl.migen.system.expresser.model.ExpresserModel;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.server.UserSet;
import uk.ac.lkl.migen.system.server.manipulator.ColorAllocationTableManipulator;

public class ColorAllocationHandler extends EntityHandler<ColorAllocationTableManipulator> {

    @SuppressWarnings("unused")
    private PreparedStatement addAllocationToShapeStatement;

    public ColorAllocationHandler(ColorAllocationTableManipulator manipulator) throws SQLException {
        super(manipulator);
    }

    public EntityId<ColorAllocationAttribute<IntegerValue>> addColorAllocation(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<BlockShape> shapeId, ColorAllocationAttribute<IntegerValue> colorAllocationAttribute, EntityMapper mapper) throws SQLException {
        return null;
    }

    public void removeColorAllocation(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<BlockShape> shapeId, EntityId<ColorAllocationAttribute<IntegerValue>> attributeId) {
    }

    public ColorAllocationAttribute<IntegerValue> getColorAllocation(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<BlockShape> shapeId, EntityId<ColorAllocationAttribute<IntegerValue>> colorAllocationAttributeId, EntityMapper mapper) throws SQLException, RestletException {
        return manipulator.selectObject(colorAllocationAttributeId, mapper);
    }

    public void setColorAllocation(EntityId<UserSet> userId, EntityId<ExpresserModel> modelId, EntityId<BlockShape> shapeId, EntityId<ColorAllocationAttribute<IntegerValue>> colorAllocationAttributeId, ColorAllocationAttribute<IntegerValue> colorAllocationAttribute, EntityMapper mapper) throws SQLException, RestletException {
        manipulator.updateObject(colorAllocationAttributeId, colorAllocationAttribute);
    }

    public List<ColorAllocationAttribute<IntegerValue>> getColorAllocationList(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<BlockShape> shapeId, EntityFilter<ColorAllocationAttribute<IntegerValue>> filter, EntityMapper mapper) {
        return null;
    }

    public List<ColorAllocationAttribute<IntegerValue>> getColorAllocationList(EntityId<UserSet> userSetId, EntityId<ExpresserModel> modelId, EntityId<BlockShape> shapeId, EntityMapper mapper) {
        return null;
    }
}
