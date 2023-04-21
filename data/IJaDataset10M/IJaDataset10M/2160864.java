package uk.ac.lkl.expresser.server.objectify;

import java.util.ArrayList;
import java.util.List;
import uk.ac.lkl.common.util.value.IntegerValue;
import uk.ac.lkl.expresser.server.ServerUtils;
import uk.ac.lkl.migen.system.expresser.model.shape.block.BlockShape;
import uk.ac.lkl.migen.system.expresser.model.shape.block.PatternShape;
import com.googlecode.objectify.annotation.Unindexed;

@Unindexed
public class ServerGroupShape extends ServerShape {

    List<String> subShapeIds;

    int originalX;

    int originalY;

    public ServerGroupShape() {
    }

    public ServerGroupShape(int x, int y, String id, List<String> subShapeIds, String userKey) {
        super(x, y, id, userKey);
        originalX = x;
        originalY = y;
        this.subShapeIds = subShapeIds;
        setSubShapesParentId();
    }

    private void setSubShapesParentId() {
        DAO dao = ServerUtils.getDao();
        for (String subShapeId : subShapeIds) {
            ServerExpressedObject shape = dao.getShape(subShapeId);
            if (shape != null) {
                shape.setParentId(id);
                dao.persistObject(shape);
            } else {
                ServerUtils.severe("Expected to find a sub-shape with the id " + subShapeId);
            }
        }
    }

    @Override
    public ServerGroupShape copy(String idOfCopy) {
        DAO dao = ServerUtils.getDao();
        ArrayList<String> subShapeCopyIds = new ArrayList<String>();
        for (String subShapeId : subShapeIds) {
            ServerExpressedObject shape = dao.getShape(subShapeId);
            ServerExpressedObject subShapeCopy = shape.copy(ServerUtils.generateGUIDString());
            dao.persistObject(subShapeCopy);
            subShapeCopyIds.add(subShapeCopy.getId());
        }
        return new ServerGroupShape(x, y, idOfCopy, subShapeCopyIds, getProjectKey());
    }

    @Override
    public BlockShape getPatternShape() {
        DAO dao = ServerUtils.getDao();
        IntegerValue deltaXIntegerValue = new IntegerValue(x - originalX);
        IntegerValue deltaYIntegerValue = new IntegerValue(y - originalY);
        ArrayList<BlockShape> shapes = new ArrayList<BlockShape>();
        for (String subShapeId : subShapeIds) {
            ServerShape serverShape = dao.getShape(subShapeId);
            if (serverShape != null) {
                BlockShape blockShape = serverShape.getPatternShape();
                if (blockShape != null) {
                    blockShape.moveBy(deltaXIntegerValue, deltaYIntegerValue);
                    shapes.add(blockShape);
                } else {
                    ServerUtils.severe("No block shape generated from server shape.");
                }
            } else {
                ServerUtils.severe("Could not find sub-shape of a group shape with id " + subShapeId);
            }
        }
        PatternShape pattern = PatternShape.createPatternFromShapes(shapes, false, 1, 0, 0);
        pattern.setBuildingBlockStatus(PatternShape.TREAT_AS_A_BUILDING_BLOCK);
        pattern.moveTo(new IntegerValue(x), new IntegerValue(y));
        pattern.setUniqueId(id);
        pattern.getShape().setUniqueId(id);
        return pattern;
    }
}
