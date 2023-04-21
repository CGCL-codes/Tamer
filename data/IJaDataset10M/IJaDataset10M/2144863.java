package com.turnengine.client.local.group.command;

import com.javabi.command.IExecutableCommand;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.serializer.AbstractCommandDataSerializer;
import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.turnengine.client.local.group.bean.GroupSerializer;
import com.turnengine.client.local.group.bean.IGroup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Get Group List By Faction Name Data Serializer.
 */
public class GetGroupListByFactionNameDataSerializer extends AbstractCommandDataSerializer<List<IGroup>> implements IGetGroupListByFactionNameDataSerializer {

    /**
	 * Creates a new Get Group List By Faction Name Data Serializer.
	 */
    public GetGroupListByFactionNameDataSerializer() {
        super(GetGroupListByFactionName.COMMAND_ID);
    }

    @Override
    public List<IGroup> readReturnValue(IDataReader reader) throws IOException {
        List<IGroup> value = reader.readObjectCollection(new ArrayList<IGroup>(), true, false, new GroupSerializer());
        return value;
    }

    @Override
    public void writeReturnValue(IDataWriter writer, List<IGroup> value) throws IOException {
        writer.writeObjectCollection(value, true, false, new GroupSerializer());
    }

    @Override
    public IExecutableCommand<List<IGroup>> readRequest(IDataReader reader) throws IOException {
        long loginId = reader.readLong();
        int instanceId = reader.readInt();
        String name = reader.readString(true);
        GetGroupListByFactionName object = new GetGroupListByFactionName();
        object.setLoginId(loginId);
        object.setInstanceId(instanceId);
        object.setName(name);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<List<IGroup>> argument) throws IOException {
        GetGroupListByFactionName object = (GetGroupListByFactionName) argument;
        long loginId = object.getLoginId();
        int instanceId = object.getInstanceId();
        String name = object.getName();
        writer.writeLong(loginId);
        writer.writeInt(instanceId);
        writer.writeString(name, true);
    }

    @Override
    public IExecutableCommandResponse<List<IGroup>> newResponse() {
        return new GetGroupListByFactionNameResponse();
    }
}
