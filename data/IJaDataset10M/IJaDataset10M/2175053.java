package com.turnengine.client.local.property.command;

import com.javabi.command.IExecutableCommand;
import com.javabi.command.IExecutableCommandResponse;
import com.javabi.command.serializer.AbstractCommandDataSerializer;
import com.javabi.common.io.data.IDataReader;
import com.javabi.common.io.data.IDataWriter;
import java.io.IOException;

/**
 * The Set Local Property Data Serializer.
 */
public class SetLocalPropertyDataSerializer extends AbstractCommandDataSerializer<String> implements ISetLocalPropertyDataSerializer {

    /**
	 * Creates a new Set Local Property Data Serializer.
	 */
    public SetLocalPropertyDataSerializer() {
        super(SetLocalProperty.COMMAND_ID);
    }

    @Override
    public String readReturnValue(IDataReader reader) throws IOException {
        String value = reader.readString(true);
        return value;
    }

    @Override
    public void writeReturnValue(IDataWriter writer, String value) throws IOException {
        writer.writeString(value, true);
    }

    @Override
    public IExecutableCommand<String> readRequest(IDataReader reader) throws IOException {
        long loginId = reader.readLong();
        int instanceId = reader.readInt();
        String key = reader.readString(true);
        String value = reader.readString(true);
        SetLocalProperty object = new SetLocalProperty();
        object.setLoginId(loginId);
        object.setInstanceId(instanceId);
        object.setKey(key);
        object.setValue(value);
        return object;
    }

    @Override
    public void writeRequest(IDataWriter writer, IExecutableCommand<String> argument) throws IOException {
        SetLocalProperty object = (SetLocalProperty) argument;
        long loginId = object.getLoginId();
        int instanceId = object.getInstanceId();
        String key = object.getKey();
        String value = object.getValue();
        writer.writeLong(loginId);
        writer.writeInt(instanceId);
        writer.writeString(key, true);
        writer.writeString(value, true);
    }

    @Override
    public IExecutableCommandResponse<String> newResponse() {
        return new SetLocalPropertyResponse();
    }
}
