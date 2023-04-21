package com.turnengine.client.local.action.bean;

import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.turnengine.client.local.action.enums.ActionTargetType;
import com.turnengine.client.local.unit.enums.UnitRelation;
import com.turnengine.client.local.unit.enums.UnitType;
import java.io.IOException;

/**
 * The Action Target Serializer.
 */
public class ActionTargetSerializer implements IActionTargetSerializer {

    @Override
    public IActionTarget readObject(IDataReader reader) throws IOException {
        int actionId = reader.readInt();
        ActionTargetType targetType = reader.readEnum(ActionTargetType.class, true);
        UnitType type = reader.readEnum(UnitType.class, true);
        UnitRelation relation = reader.readEnum(UnitRelation.class, true);
        IActionTarget object = new ActionTarget();
        object.setActionId(actionId);
        object.setTargetType(targetType);
        object.setType(type);
        object.setRelation(relation);
        return object;
    }

    public void writeObject(IDataWriter writer, IActionTarget object) throws IOException {
        int actionId = object.getActionId();
        ActionTargetType targetType = object.getTargetType();
        UnitType type = object.getType();
        UnitRelation relation = object.getRelation();
        writer.writeInt(actionId);
        writer.writeEnum(targetType, true);
        writer.writeEnum(type, true);
        writer.writeEnum(relation, true);
    }
}
