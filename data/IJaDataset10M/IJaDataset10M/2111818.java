package com.turnengine.client.local.unit.command;

import com.javabi.command.IExecutableCommand;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.serializer.AbstractCommandDataSerializer;
import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.turnengine.client.local.unit.bean.IUnit;
import com.turnengine.client.local.unit.bean.UnitSerializer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Get Units By Group Name Data Serializer.
 */
public class GetUnitsByGroupNameDataSerializer extends AbstractCommandDataSerializer<List<IUnit>> implements IGetUnitsByGroupNameDataSerializer {

    /**
	 * Creates a new Get Units By Group Name Data Serializer.
	 */
    public GetUnitsByGroupNameDataSerializer() {
        super(GetUnitsByGroupName.COMMAND_ID);
    }

    @Override
    public List<IUnit> readReturnValue(IDataReader reader) throws IOException {
        List<IUnit> value = reader.readObjectCollection(new ArrayList<IUnit>(), true, false, new UnitSerializer());
        return value;
    }

    @Override
    public void writeReturnValue(IDataWriter writer, List<IUnit> value) throws IOException {
        writer.writeObjectCollection(value, true, false, new UnitSerializer());
    }

    @Override
    public IExecutableCommand<List<IUnit>> readRequest(IDataReader reader) throws IOException {
        long loginId = reader.readLong();
        int instanceId = reader.readInt();
        String name = reader.readString(true);
        GetUnitsByGroupName object = new GetUnitsByGroupName();
        object.setLoginId(loginId);
        object.setInstanceId(instanceId);
        object.setName(name);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<List<IUnit>> argument) throws IOException {
        GetUnitsByGroupName object = (GetUnitsByGroupName) argument;
        long loginId = object.getLoginId();
        int instanceId = object.getInstanceId();
        String name = object.getName();
        writer.writeLong(loginId);
        writer.writeInt(instanceId);
        writer.writeString(name, true);
    }

    @Override
    public IExecutableCommandResponse<List<IUnit>> newResponse() {
        return new GetUnitsByGroupNameResponse();
    }
}
