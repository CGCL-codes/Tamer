package com.turnengine.client.local.group.command;

import com.javabi.command.IExecutableCommand;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.serializer.AbstractCommandDataSerializer;
import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import com.turnengine.client.local.group.bean.GroupSerializer;
import com.turnengine.client.local.group.bean.IGroup;
import java.io.IOException;

/**
 * The Get Group By Name Data Serializer.
 */
public class GetGroupByNameDataSerializer extends AbstractCommandDataSerializer<IGroup> implements IGetGroupByNameDataSerializer {

    /**
	 * Creates a new Get Group By Name Data Serializer.
	 */
    public GetGroupByNameDataSerializer() {
        super(GetGroupByName.COMMAND_ID);
    }

    @Override
    public IGroup readReturnValue(IDataReader reader) throws IOException {
        IGroup value = new GroupSerializer().readObject(reader);
        return value;
    }

    @Override
    public void writeReturnValue(IDataWriter writer, IGroup value) throws IOException {
        new GroupSerializer().writeObject(writer, value);
    }

    @Override
    public IExecutableCommand<IGroup> readRequest(IDataReader reader) throws IOException {
        long loginId = reader.readLong();
        int instanceId = reader.readInt();
        String name = reader.readString(true);
        GetGroupByName object = new GetGroupByName();
        object.setLoginId(loginId);
        object.setInstanceId(instanceId);
        object.setName(name);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<IGroup> argument) throws IOException {
        GetGroupByName object = (GetGroupByName) argument;
        long loginId = object.getLoginId();
        int instanceId = object.getInstanceId();
        String name = object.getName();
        writer.writeLong(loginId);
        writer.writeInt(instanceId);
        writer.writeString(name, true);
    }

    @Override
    public IExecutableCommandResponse<IGroup> newResponse() {
        return new GetGroupByNameResponse();
    }
}
